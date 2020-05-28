package com.service.eventbus;

import org.springframework.context.ApplicationEvent;

/**
 * @Classname UserInfoChangeEvent
 * @Description 用户变化事件
 * @Date 2020/5/21 16:10
 * @Created by 125937
 */
public class UserInfoChangeEvent extends ApplicationEvent {
    public UserInfoChangeEvent(Object source) {
        super(source);
    }
}
