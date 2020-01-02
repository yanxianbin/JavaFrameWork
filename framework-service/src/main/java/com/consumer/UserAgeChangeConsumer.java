package com.consumer;

import com.annotations.MqListenerEndPointDelay;
import com.constants.Constants;
import com.constants.MqConsumerResult;
import com.rabbitmq.MessageMode;
import com.sevice.MessageConsumer;
import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname UserAgeChangeConsumer
 * @Description TODO
 * @Date 2019/12/31 12:08
 * @Created by 125937
 */
@MqListenerEndPointDelay(queueName = Constants.USER_AGE_CHANGE_QUEUE)
@Slf4j
public class UserAgeChangeConsumer implements MessageConsumer {
    @Override
    public MqConsumerResult consumerExecute(MessageMode messageMode) {
        log.info("UserAgeChangeConsumer message:{} queue:{}", JsonUtils.deSerializable(messageMode),messageMode.getPrincipal().getQueueName());
        return MqConsumerResult.FAILED;
    }
}
