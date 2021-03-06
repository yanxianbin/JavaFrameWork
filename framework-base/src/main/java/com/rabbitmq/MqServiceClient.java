package com.rabbitmq;

import com.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Classname MqListenerInitService
 * @Description Mq消费者启动监听
 * @Date 2019/12/27 15:19
 * @Created by 125937
 */
@Component
@Slf4j
public class MqServiceClient {

    @Value(value = "${spring.rabbitmq.listener.simple.concurrency:1}")
    private int concurrency;

    @Value(value = "${spring.rabbitmq.listener.simple.max-concurrency:8}")
    private int maxConcurrency;

    @Value(value = "${spring.rabbitmq.maxretrys:10}")
    private int maxRetryCount;

    private static final String X_MAX_PRIORITY_HEADER = "x-max-priority";
    private static final int DEFAULT_MAX_PRIORITY = 100;
    private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
    private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    private static final String DLQ_ROUTING_EXCHANGE = "comm.deal.exchange";
    private static final String DLQ_NAME = "comm.deal.queue";
    //延时队列-过期时间
    private static final String X_MESSAGE_TTL_HEADER = "x-message-ttl";
    //延时队列后缀
    private static final String DLQ_DELAY_SUFFIX= Constants.DLQ_DELAY_SUFFIX;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 发送消息到队列中
     * @param routingKey
     * @param exchangeName
     * @param queueName
     * @param messageMode
     */
    public void sendMqMessage(String routingKey, String exchangeName, String queueName, MessageMode messageMode) {
        if (messageMode.getAutoDeclare()) {
            createDealQueue();
            DirectExchange ex = new DirectExchange(exchangeName);
            rabbitAdmin.declareExchange(ex);
            Queue q = createQueue(queueName);
            rabbitAdmin.declareQueue(q);
            rabbitAdmin.declareBinding(BindingBuilder.bind(q).to(ex).with(routingKey));
        }
        rabbitTemplate.convertAndSend(exchangeName, routingKey, messageMode, message -> {
            //设置消息的优先级
            message.getMessageProperties().setPriority(messageMode.getPriority());
            //持久化
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        },new CorrelationData(String.valueOf(messageMode.getMessageId())));
    }

    /**
     * 发送消息到队列中-延迟队列
     * @param routingKey
     * @param exchangeName
     * @param queueName
     * @param messageMode
     */
    public void sendMqMessage(String routingKey, String exchangeName, String queueName, MessageMode messageMode,Long ttl) {
        if (messageMode.getAutoDeclare()) {
            createDelayQueue(queueName,exchangeName,routingKey);
            DirectExchange ex = new DirectExchange(exchangeName);
            rabbitAdmin.declareExchange(ex);
            Queue q = createQueue(queueName,exchangeName,routingKey,ttl);
            rabbitAdmin.declareQueue(q);
            rabbitAdmin.declareBinding(BindingBuilder.bind(q).to(ex).with(routingKey));
        }
        rabbitTemplate.convertAndSend(exchangeName, routingKey, messageMode, message -> {
            //设置消息的优先级
            message.getMessageProperties().setPriority(messageMode.getPriority());
            //持久化
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        },new CorrelationData(String.valueOf(messageMode.getMessageId())));
    }

    /**
     * 初始化容器监听
     * @param consumerMap
     */
    public void initListenerContainer(Map<String, MessageConsumer> consumerMap){
        if(consumerMap==null || consumerMap.isEmpty()) {
            return;
        }
        for(Map.Entry<String,MessageConsumer> entry : consumerMap.entrySet()) {
            //启动的时候就注册队列，这里没绑定，发送消息的时候会绑定路由和交换机
            rabbitAdmin.declareQueue(createQueue(entry.getKey()));
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
            log.info("MqListenerInitService initListenerContainer queue:{}",entry.getKey());
            container.setQueueNames(entry.getKey());
            //设置当前的消费者数量
            container.setConcurrentConsumers(concurrency);
            if(maxConcurrency>0) {
                container.setMaxConcurrentConsumers(maxConcurrency);
            }
            //设置是否重回队列
            container.setDefaultRequeueRejected(false);
            //是否开启事务
            container.setChannelTransacted(false);
            //设置手动签收
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
            container.setAmqpAdmin(rabbitAdmin);
            //设置监听外露
            container.setExposeListenerChannel(true);
            //设置消息监听
            container.setMessageListener(new MqChannelService(entry.getKey(),rabbitTemplate,maxRetryCount,entry.getValue()));
            //启动容器
            container.start();
        }
    }

    /**
     * 创建队列
     * @param queue
     * @return
     */
    private Queue createQueue(String queue) {
        createDealQueue();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(X_DEAD_LETTER_EXCHANGE, DLQ_ROUTING_EXCHANGE);//设置死信交换机
        param.put(X_DEAD_LETTER_ROUTING_KEY, DLQ_NAME);//设置死信routingKey
        param.put(X_MAX_PRIORITY_HEADER, DEFAULT_MAX_PRIORITY);
        return new Queue(queue,true, false, false, param);
    }

    /**
     * 创建队列
     * @param queue
     * @return
     */
    private Queue createQueue(String queue,String exchange,String routingKey,Long ttl) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(X_DEAD_LETTER_EXCHANGE, exchange+DLQ_DELAY_SUFFIX);//设置死信交换机
        param.put(X_DEAD_LETTER_ROUTING_KEY, routingKey+DLQ_DELAY_SUFFIX);//设置死信routingKey
        param.put(X_MAX_PRIORITY_HEADER, DEFAULT_MAX_PRIORITY);
        if(Objects.isNull(ttl)){
            ttl=6000L;
        }
        param.put(X_MESSAGE_TTL_HEADER,ttl);
        return new Queue(queue,true, false, false, param);
    }

    /**
     * 创建死信队列
     */
    private void createDelayQueue(String queue,String exchange,String routingKey){
        final DirectExchange delayExchange = new DirectExchange(exchange+DLQ_DELAY_SUFFIX);
        final Queue delayQueue = new Queue(queue+DLQ_DELAY_SUFFIX);
        final Binding binding = BindingBuilder.bind(delayQueue).to(delayExchange).with(routingKey+DLQ_DELAY_SUFFIX);
        rabbitAdmin.declareExchange(delayExchange);
        rabbitAdmin.declareQueue(delayQueue);
        rabbitAdmin.declareBinding(binding);
        log.info(binding.toString());
    }

    /**
     * 创建死信队列
     */
    private void createDealQueue(){
        final DirectExchange exchange = new DirectExchange(DLQ_ROUTING_EXCHANGE);
        final Queue queue = new Queue(DLQ_NAME);
        final Binding binding = BindingBuilder.bind(queue).to(exchange).with(DLQ_NAME);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
        log.info(binding.toString());
    }
}
