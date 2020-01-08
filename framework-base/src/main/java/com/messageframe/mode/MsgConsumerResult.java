package com.messageframe.mode;

import com.constants.MessageStateEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname MsgConsumerResult
 * @Description 消息消费结果
 * @Date 2020/1/6 14:55
 * @Created by 125937
 */
public class MsgConsumerResult implements Serializable {

    /**
     * 执行状态
     */
    private MessageStateEnum executeState;

    /**
     * 执行失败的数据
     */
    private Map<Long,String> errorData;

    /**
     * 执行成功的数据
     */
    private List<Long> successData;


    public MsgConsumerResult(){
        this.executeState=MessageStateEnum.FAILED;
        this.errorData=new HashMap<>();
        this.successData=new ArrayList<>();
    }

    public MsgConsumerResult(MessageStateEnum stateEnum){
        this.executeState=stateEnum;
        this.errorData=new HashMap<>();
        this.successData=new ArrayList<>();
    }

    public void addSuccessData(Long messageId){
        this.successData.add(messageId);
    }

    public void addErrorData(Long messageId,String errorReason){
        this.errorData.put(messageId,errorReason);
    }

    public MessageStateEnum getExecuteState() {
        return executeState;
    }

    public void setExecuteState(MessageStateEnum executeState) {
        this.executeState = executeState;
    }

    public Map<Long, String> getErrorData() {
        return errorData;
    }

    public void setErrorData(Map<Long, String> errorData) {
        this.errorData = errorData;
    }

    public List<Long> getSuccessData() {
        return successData;
    }

    public void setSuccessData(List<Long> successData) {
        this.successData = successData;
    }
}
