package com.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * @Classname MqConfig
 * @Description Mq配置类
 * @Date 2019/12/27 15:22
 * @Created by 125937
 */
@Configuration
@Slf4j
public class MqConfig {

    @Value(value = "${spring.rabbitmq.addresses:127.0.0.1}")
    private String hostAddress;

    @Value(value = "${spring.rabbitmq.port:5672}")
    private int port;

    @Value(value = "${spring.rabbitmq.username:admin}")
    private String userName;

    @Value(value = "${spring.rabbitmq.password:admin}")
    private String passWord;

    @Value(value = "${spring.rabbitmq.virtual-host:/yxb}")
    private String virtualHost;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(hostAddress);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(passWord);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        log.info("RabbitAdmin启动了。。。");
        //设置启动spring容器时自动加载这个类(这个参数现在默认已经是true，可以不用设置)
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
}
