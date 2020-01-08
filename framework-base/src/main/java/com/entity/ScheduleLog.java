package com.entity;

import com.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @Classname ScheduleLog
 * @Description 调度日志
 * @Date 2020/1/8 16:35
 * @Created by 125937
 */
public class ScheduleLog extends BaseEntity<Long> {

    private String taskName;

    private Long taskId;

    private Timestamp scheduleStart;

    private Timestamp scheduleEnd;

    private Timestamp executeStart;

    private Timestamp executeEnd;

    private String scheduleState;

    private String executeState;

    private String failedReason;

    private String executeLog;

    private String scheduleType;

    public Timestamp getScheduleStart() {
        return scheduleStart;
    }

    public void setScheduleStart(Timestamp scheduleStart) {
        this.scheduleStart = scheduleStart;
    }

    public Timestamp getScheduleEnd() {
        return scheduleEnd;
    }

    public void setScheduleEnd(Timestamp scheduleEnd) {
        this.scheduleEnd = scheduleEnd;
    }

    public Timestamp getExecuteStart() {
        return executeStart;
    }

    public void setExecuteStart(Timestamp executeStart) {
        this.executeStart = executeStart;
    }

    public Timestamp getExecuteEnd() {
        return executeEnd;
    }

    public void setExecuteEnd(Timestamp executeEnd) {
        this.executeEnd = executeEnd;
    }

    public String getScheduleState() {
        return scheduleState;
    }

    public void setScheduleState(String scheduleState) {
        this.scheduleState = scheduleState;
    }

    public String getExecuteState() {
        return executeState;
    }

    public void setExecuteState(String executeState) {
        this.executeState = executeState;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }

    public String getExecuteLog() {
        return executeLog;
    }

    public void setExecuteLog(String executeLog) {
        this.executeLog = executeLog;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
