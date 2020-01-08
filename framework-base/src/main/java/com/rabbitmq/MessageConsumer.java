package com.rabbitmq;

import com.constants.MqConsumerResult;
import com.rabbitmq.MessageMode;

/**
 * 消息消费接口
 */
public interface MessageConsumer {
    MqConsumerResult consumerExecute(MessageMode messageMode);
}
