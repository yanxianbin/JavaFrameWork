package com.schedule.client;

import com.entity.ScheduleInfo;

import java.io.Serializable;

/**
 * @Classname ScheduleRequest
 * @Description 任务调度请求实体
 * @Date 2020/1/8 15:08
 * @Created by 125937
 */
public class ScheduleRequest implements Serializable {

    private Long scheduleId;

    private ScheduleInfo taskInfo;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public ScheduleInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(ScheduleInfo taskInfo) {
        this.taskInfo = taskInfo;
    }
}
