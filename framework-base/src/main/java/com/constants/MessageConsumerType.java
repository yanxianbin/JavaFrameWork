package com.constants;

/**
 * @Classname MessageConsumerType
 * @Description 消息消费方式
 * @Date 2020/1/3 18:39
 * @Created by 125937
 */
public enum MessageConsumerType {
    SINGLE("10","单条消费"),
    SORT_LIST("20","按照消息顺序"),
    LAST("30","最后一条")
    ;

    MessageConsumerType(String code,String value){
        this.code=code;
        this.value=value;
    }
    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
