package com.service.eventbus;

import org.springframework.context.ApplicationEvent;

/**
 * @Classname PowerChangeEvent
 * @Description TODO
 * @Date 2020/5/21 16:32
 * @Created by 125937
 */
public class PowerChangeEvent extends ApplicationEvent {
    public PowerChangeEvent(Object source) {
        super(source);
    }
}
