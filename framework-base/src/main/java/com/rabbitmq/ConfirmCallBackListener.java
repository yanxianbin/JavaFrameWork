package com.rabbitmq;

import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Classname ConfirmCallBackListener
 * @Description TODO
 * @Date 2019/12/31 11:02
 * @Created by 125937
 */
@Slf4j
public class ConfirmCallBackListener implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        //log.info("ConfirmCallBackListener confirm correlationData:{} b:{} s:{} ", JsonUtils.deSerializable(correlationData),b,s);
    }
}
