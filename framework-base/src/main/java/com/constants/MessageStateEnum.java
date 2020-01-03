package com.constants;

/**
 * @Classname MessageStateEnum
 * @Description 消息消费状态
 * @Date 2020/1/3 15:01
 * @Created by 125937
 */
public enum MessageStateEnum {
    INIT("10","初始化"),
    SUCCESS("20","成功"),
    FAILED("30","失败")
    ;

    MessageStateEnum(String code,String value){
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
