package com.rabbitmq;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 发送到队列的实体
 */
@JsonInclude(value =JsonInclude.Include.NON_NULL )
public class MessageMode implements Serializable {

    /**
     * 消息元信息，如（队列名称、队列交换机、队列路由）
     */
    private MqPrincipal principal;

    /**
     * 消息体
     */
    private Object msg;

    /**
     * 消息ID
     */
    private Long messageId= System.currentTimeMillis();

    /**
     * 创建时间
     */
    private Timestamp createTime=new Timestamp(System.currentTimeMillis());

    /**
     * 消息来源，一般是多系统用到的时候，这里是服务名称
     */
    private String messageFrom;

    public MqPrincipal getPrincipal() {
        return principal;
    }

    public void setPrincipal(MqPrincipal principal) {
        this.principal = principal;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }
}
