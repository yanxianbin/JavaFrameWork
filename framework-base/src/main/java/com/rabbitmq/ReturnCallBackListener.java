package com.rabbitmq;

import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @Classname ReturnCallBackListener
 * @Description TODO
 * @Date 2019/12/31 11:03
 * @Created by 125937
 */
@Slf4j
public class ReturnCallBackListener implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.info("ReturnCallBackListener returnedMessage message:{} i:{} s:{} s1:{} s2:{}", JsonUtils.deSerializable(message),i,s,s1,s2);
    }
}
