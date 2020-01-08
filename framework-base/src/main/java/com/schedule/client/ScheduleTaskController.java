package com.schedule.client;

import com.bo.ResponseData;
import com.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/schedule/executeTask")
    public ResponseData<Boolean> executeTask(@RequestBody ScheduleRequest request){
        ResponseData<Boolean> responseData=new ResponseData<>();
        log.info("executeTask scheduleId:{} taskName:{} param:{}",request.getScheduleId(),request.getTaskInfo().getTaskName(),request.getTaskInfo().getExecuteParam());
        ScheduleJobHandler scheduleJobHandler=ScheduleContainer.getScheduleJobHandler(request.getTaskInfo().getTaskName());
        if(Objects.nonNull(scheduleJobHandler)) {
            executor.execute(() -> {
                try {
                    long startTime=System.currentTimeMillis();
                    scheduleJobHandler.execute(request.getScheduleId(), request.getTaskInfo());
                    log.info("taskName:{} times:{}ms",request.getTaskInfo().getTaskName(),System.currentTimeMillis()-startTime);
                }catch (Exception ex){
                    log.error("taskName:{} ex:{}",request.getTaskInfo().getTaskName(), CommonUtils.getExceptionMsg(ex),ex);
                }
            });
        }
        responseData.setData(true).ok();
        return responseData;
    }
}
