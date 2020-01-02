package com.annotations;

import java.lang.annotation.*;

/**
 * @Classname MqLisetenerEndPointDelay
 * @Description 延时队列监听注解
 * @Date 2019/12/31 12:10
 * @Created by 125937
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@MqListenerEndPoint
public @interface MqListenerEndPointDelay {

    /**
     * 队列名称
     * @return
     */
    String queueName() default "";
}
