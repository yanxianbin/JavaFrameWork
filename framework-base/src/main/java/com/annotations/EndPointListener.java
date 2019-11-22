package com.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 监听队列
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface EndPointListener {
    /**
     * 队列名称
     * @return
     */
    String queueName() default "";
}
