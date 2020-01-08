package com.annotations;

import java.lang.annotation.*;

/**
 * @Classname DataSecurityFilter
 * @Description 过滤敏感字段注解
 * @Date 2020/1/6 16:48
 * @Created by 125937
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSecurityFilter {
    /**
     * 包含的字段
     * @return
     */
    String[] includes() default {};
}
