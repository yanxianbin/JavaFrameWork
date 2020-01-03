package com.consumer;

import com.annotations.MqListenerEndPoint;
import com.constants.Constants;
import com.constants.MqConsumerResult;
import com.rabbitmq.MessageMode;
import com.service.MessageConsumer;
import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

@MqListenerEndPoint(queueName = Constants.USER_INFO_CHANGE_QUEUE)
@Slf4j
public class UserInfoChangeConsumer implements MessageConsumer {
    @Override
    public MqConsumerResult consumerExecute(MessageMode messageMode) {
        log.info("UserInfoChangeConsumer message:{} queue:{}", JsonUtils.deSerializable(messageMode),messageMode.getPrincipal().getQueueName());
        return MqConsumerResult.SUCCESS;
    }
}
