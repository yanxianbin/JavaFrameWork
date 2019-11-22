package com.consumer;

import com.annotations.EndPointListener;
import com.constants.MqConsumerResult;
import com.rabbitmq.MessageMode;
import com.sevice.MessageConsumer;

@EndPointListener(queueName = "userInfo-change-queue")
public class UserInfoChangeConsumer implements MessageConsumer {
    @Override
    public MqConsumerResult consumerExecute(MessageMode messageMode) {
        return MqConsumerResult.SUCCESS;
    }
}
