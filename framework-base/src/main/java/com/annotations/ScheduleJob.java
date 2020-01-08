package com.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Classname ScheduleJob
 * @Description 任务调度注解
 * @Date 2020/1/8 11:07
 * @Created by 125937
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface ScheduleJob {
    /**
     * 任务描述
     * @return
     */
    String taskDesc() default "";

    /**
     * 执行参数
     * @return
     */
    String executeParam() default "";

    /**
     * cron表达式
     * @return
     */
    String cronCondition() default "";
}
