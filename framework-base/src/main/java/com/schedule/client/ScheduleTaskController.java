package com.schedule.client;

import com.bo.ResponseData;
import com.entity.ScheduleLog;
import com.service.ScheduleLogService;
import com.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Classname ScheduleTaskController
 * @Description 调度任务通用接口
 * @Date 2020/1/8 11:21
 * @Created by 125937
 */
@RestController
@Slf4j
public class ScheduleTaskController {

    @Autowired
    @Qualifier(value = "scheduleThreadPoolTaskExecutor")
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private ScheduleLogService scheduleLogService;

    @RequestMapping(value = "/schedule/executeTask", method = {RequestMethod.POST})
    public ResponseData<Boolean> executeTask(@RequestBody ScheduleRequest request) {
        ResponseData<Boolean> responseData = new ResponseData<>();
        if(true) {
            return responseData;
        }
        log.info("executeTask scheduleId:{} taskName:{} param:{}", request.getScheduleId(), request.getTaskInfo().getTaskName(), request.getTaskInfo().getExecuteParam());
        ScheduleJobHandler scheduleJobHandler = ScheduleContainer.getScheduleJobHandler(request.getTaskInfo().getTaskName());
        if (Objects.nonNull(scheduleJobHandler)) {
            executor.execute(() -> {
                ScheduleLog scheduleLog = scheduleLogService.findById(request.getScheduleId());
                try {
                    long startTime = System.currentTimeMillis();
                    if (Objects.nonNull(scheduleLog)) {
                        scheduleLog.setExecuteStart(new Timestamp(startTime));
                        scheduleLog.setExecuteState("成功");
                    }
                    scheduleJobHandler.execute(request.getScheduleId(), request.getTaskInfo());
                    log.info("taskName:{} times:{}ms", request.getTaskInfo().getTaskName(), System.currentTimeMillis() - startTime);
                } catch (Exception ex) {
                    if (Objects.nonNull(scheduleLog)) {
                        scheduleLog.setFailedReason(CommonUtils.getExceptionMsg(ex));
                        scheduleLog.setExecuteState("失败");
                    }
                    log.error("taskName:{} ex:{}", request.getTaskInfo().getTaskName(), CommonUtils.getExceptionMsg(ex), ex);
                } finally {
                    if (Objects.nonNull(scheduleLog)) {
                        scheduleLog.setExecuteEnd(new Timestamp(System.currentTimeMillis()));
                        scheduleLogService.update(scheduleLog);
                    }
                }
            });
        }
        responseData.setData(true).ok();
        return responseData;
    }
}
