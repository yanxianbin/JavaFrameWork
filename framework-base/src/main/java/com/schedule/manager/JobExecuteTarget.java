package com.schedule.manager;

import com.bo.ResponseData;
import com.entity.ScheduleInfo;
import com.entity.ScheduleLog;
import com.schedule.client.ScheduleRequest;
import com.service.ScheduleLogService;
import com.utils.CommonUtils;
import com.utils.IdGeneratorClient;
import com.utils.JsonUtils;
import com.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.sql.Timestamp;

/**
 * @Classname JobExecuteTarget
 * @Description TODO
 * @Date 2020/1/8 15:58
 * @Created by 125937
 */
@Slf4j
public class JobExecuteTarget implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("JobExecuteTarget taskName:{} taskParam:{}", jobExecutionContext.getJobDetail().getKey().getName(),
                jobExecutionContext.getJobDetail().getJobDataMap().get("param"));
        ScheduleInfo scheduleInfo = JsonUtils.serializable(String.valueOf(jobExecutionContext.getJobDetail().getJobDataMap().get("param")), ScheduleInfo.class);

        ScheduleLog scheduleLog = new ScheduleLog();
        scheduleLog.setScheduleStart(new Timestamp(System.currentTimeMillis()));
        scheduleLog.setTaskName(scheduleInfo.getTaskName());
        scheduleLog.setTaskId(scheduleInfo.getId());
        scheduleLog.setId(IdGeneratorClient.getId());
        scheduleLog.setExecuteState("失败");
        try {
            RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
            ScheduleRequest request = new ScheduleRequest();
            request.setScheduleId(scheduleLog.getId());
            request.setTaskInfo(scheduleInfo);

            String url=String.format("http://%s/schedule/executeTask",scheduleInfo.getServiceName());
            ResponseEntity<ResponseData> responseData = restTemplate.postForEntity(url, request, ResponseData.class);
            if(responseData.getStatusCode().equals(HttpStatus.OK)){
                scheduleLog.setExecuteState("成功");
                scheduleLog.setExecuteLog(JsonUtils.deSerializable(responseData.getBody()));
            }else{
                scheduleLog.setFailedReason(responseData.getBody().getMsg());
            }
        } catch (Exception ex) {
            scheduleLog.setFailedReason(CommonUtils.getExceptionMsg(ex));
        }finally {
            scheduleLog.setScheduleEnd(new Timestamp(System.currentTimeMillis()));
            scheduleLog.setScheduleType("自动");
            ScheduleLogService scheduleLogService = SpringUtils.getBean(ScheduleLogService.class);
            scheduleLogService.insert(scheduleLog);
        }
    }
}
