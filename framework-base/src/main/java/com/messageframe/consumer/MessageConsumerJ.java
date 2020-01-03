package com.messageframe.consumer;

import com.annotations.MqListenerEndPoint;
import com.constants.Constants;
import com.constants.MqConsumerResult;
import com.rabbitmq.MessageMode;
import com.service.MessageConsumer;
import com.utils.CommonUtils;

/**
 * @Classname MessageConsumer
 * @Description 通用消息消费者
 * @Date 2020/1/3 15:10
 * @Created by 125937
 */
@MqListenerEndPoint(queueName = Constants.MESSAGE_FRAME_QUEUE_J)
public class MessageConsumerJ implements MessageConsumer {

    @Override
    public MqConsumerResult consumerExecute(MessageMode messageMode){
        return CommonUtils.receiveCommonMessage(messageMode);
    }
}
