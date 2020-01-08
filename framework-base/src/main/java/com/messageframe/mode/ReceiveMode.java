package com.messageframe.mode;

import com.entity.BaseEntity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Classname ReceiveMode
 * @Description 消息实体
 * @Date 2020/1/3 13:56
 * @Created by 125937
 */
public class ReceiveMode extends BaseEntity<Long> implements Serializable {

    /**
     * 开始执行时间
     */
    private Timestamp exeStartTime;

    /**
     * 执行结束时间
     */
    private Timestamp exeEndTime;

    /**
     * 消息体
     */
    private String message;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 执行次数
     */
    private Integer executeCount;

    /**
     * 执行耗时
     */
    private Long executeTimes;

    /**
     * 消息业务主键
     */
    private String businessNumber;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 执行状态
     */
    private String executeState;


    public Timestamp getExeStartTime() {
        return exeStartTime;
    }

    public void setExeStartTime(Timestamp exeStartTime) {
        this.exeStartTime = exeStartTime;
    }

    public Timestamp getExeEndTime() {
        return exeEndTime;
    }

    public void setExeEndTime(Timestamp exeEndTime) {
        this.exeEndTime = exeEndTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    public Long getExecuteTimes() {
        return executeTimes;
    }

    public void setExecuteTimes(Long executeTimes) {
        this.executeTimes = executeTimes;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public String getExecuteState() {
        return executeState;
    }

    public void setExecuteState(String executeState) {
        this.executeState = executeState;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
