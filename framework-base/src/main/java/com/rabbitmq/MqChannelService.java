package com.rabbitmq;

import com.constants.MqConsumerResult;
import com.rabbitmq.client.Channel;
import com.sevice.MessageConsumer;
import com.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @Classname MqChannelService
 * @Description 消费终端
 * @Date 2019/12/27 16:14
 * @Created by 125937
 */
@Slf4j
public class MqChannelService implements ChannelAwareMessageListener {

    /**
     * 消费者
     */
    private MessageConsumer messageConsumer;

    /**
     * 队列名称
     */
    private String queueName;

    private RabbitTemplate rabbitTemplate;

    private int maxRetryCount;

    private String RETRY_HEADER = "x-retry-count";

    public MqChannelService(String queueName, RabbitTemplate rabbitTemplate, int maxRetryCount, MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
        this.queueName = queueName;
        this.rabbitTemplate = rabbitTemplate;
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            MessageMode messageMode = JsonUtils.serializable(message.getBody(), MessageMode.class);
            if (Objects.isNull(messageMode)) {
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            }
            MqConsumerResult result = messageConsumer.consumerExecute(messageMode);
            //不等于成功的情况，判断重试次数是否已用完
            if (!result.getCode().equals(MqConsumerResult.SUCCESS.getCode())) {
                Integer retryCount = (Integer) message.getMessageProperties().getHeaders().get(RETRY_HEADER);
                if (Objects.isNull(retryCount)) {
                    retryCount = Integer.valueOf(0);
                }
                if (retryCount < maxRetryCount) {
                  message.getMessageProperties().getHeaders().put(RETRY_HEADER,retryCount+1);
                  //优先级
                  Integer priority= message.getMessageProperties().getPriority();
                  //设置优先级
                  message.getMessageProperties().setPriority(priority-1);
                  rabbitTemplate.send(message.getMessageProperties().getReceivedExchange(),message.getMessageProperties().getReceivedRoutingKey(),message,new CorrelationData(String.valueOf(messageMode.getMessageId())));
                  //手动设置应答
                  channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                  log.info("MqChannelService onMessage queueName:{} messageId:{} retryCount:{}",queueName,message.getMessageProperties().getDeliveryTag(),retryCount);
                }else{
                    log.error("MqChannelService onMessage retryCount>{} queueName:{} messageId:{} error",retryCount,queueName,message.getMessageProperties().getDeliveryTag());
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
                }
            } else {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("{} consumer error", queueName, e);
        }
    }
}
