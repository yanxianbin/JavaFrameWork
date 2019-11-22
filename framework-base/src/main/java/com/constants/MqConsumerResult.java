package com.constants;

/**
 * 消息消费状态 枚举
 */
public enum MqConsumerResult {
    SUCCESS("success","成功"),
    FAILED("failed","失败"),
    RETRY("retry","重试")
    ;
    private String code;

    private String value;

    MqConsumerResult(String code,String value){
        this.code=code;
        this.value=value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
