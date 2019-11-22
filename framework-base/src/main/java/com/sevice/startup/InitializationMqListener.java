package com.sevice.startup;

import com.annotations.EndPointListener;
import com.sevice.MessageConsumer;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class InitializationMqListener implements ApplicationRunner, ApplicationContextAware {

    /**
     * 线程安全的Map
     */
    private static final Map<String,MessageConsumer> consumerMap=new ConcurrentHashMap<>();
    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(Map.Entry<String,MessageConsumer> entry: consumerMap.entrySet()){
            System.out.println("application started queueName:"+entry.getKey());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] queueEndpoints = applicationContext.getBeanNamesForAnnotation(EndPointListener.class);
        List<String> queueList = Arrays.asList(queueEndpoints);

        for(String bean : queueList){
            MessageConsumer consumer=(MessageConsumer) applicationContext.getBean(bean);
            EndPointListener endPointListener= consumer.getClass().getAnnotation(EndPointListener.class);
            String queueName= endPointListener.queueName();
            consumerMap.put(queueName,consumer);
        }
    }

    /**
     * 获取消费者
     * @param queueName
     * @return
     */
    public static MessageConsumer getConsumer(String queueName){
        return consumerMap.get(queueName);
    }
}
