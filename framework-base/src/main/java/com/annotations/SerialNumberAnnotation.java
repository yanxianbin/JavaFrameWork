package com.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 生成业务编码通用注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface SerialNumberAnnotation {
    /**
     * 业务编码
     */
    String numberCode() default "";

    /**
     * 业务编码描述
     */
    String numberDesc() default "";

    /**
     * 增长序列
     */
    int step() default 1;

    /**
     * 编码格式 如：JD{0}-{1} 仅支持两个占位符
     */
    String numberFormat() default "";

    /**
     * 编码模板，如：yyyyMM 目前仅支持日期
     */
    String codeFormat() default "yyyyMMdd";

    /**
     * 增长位长度，不够前面补 leftPadChar
     */
    int sequenceLength() default 8;

    /**
     * 序列长度不够时补位符
     */
    String leftPadChar() default "0";

    /**
     * 起始序列号
     */
    long startSequence() default 1L;
}
