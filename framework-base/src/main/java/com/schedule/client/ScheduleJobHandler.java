package com.schedule.client;

import com.entity.ScheduleInfo;

/**
 * @Classname ScheduleJobHandler
 * @Description 调度任务接口
 * @Date 2020/1/8 11:19
 * @Created by 125937
 */
public interface ScheduleJobHandler {
   void execute(Long logId, ScheduleInfo scheduleInfo);
}
