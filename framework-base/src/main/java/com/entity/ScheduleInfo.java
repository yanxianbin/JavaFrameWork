package com.entity;

/**
 * @Classname ScheduleInfo
 * @Description 调度任务注册实体类
 * @Date 2020/1/8 10:48
 * @Created by 125937
 */
public class ScheduleInfo extends BaseEntity<Long> {

    private String taskName;

    private String taskDesc;

    private String taskGroup;

    private String serviceName;

    private String executeParam;

    private String cronCondition;

    private String taskState;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getExecuteParam() {
        return executeParam;
    }

    public void setExecuteParam(String executeParam) {
        this.executeParam = executeParam;
    }

    public String getCronCondition() {
        return cronCondition;
    }

    public void setCronCondition(String cronCondition) {
        this.cronCondition = cronCondition;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }
}
