package com.startup;

import com.annotations.EndPointListener;
import com.annotations.NumbGenConfigAnnotation;
import com.idgenerator.NumberGeneratorService;
import com.sevice.MessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class InitializationMqListener implements ApplicationRunner, ApplicationContextAware {

    /**
     * 线程安全的Map
     */
    private static final Map<String,MessageConsumer> consumerMap=new ConcurrentHashMap<>();

    private final Map<String,Object> numberConfigMap=new ConcurrentHashMap<>();

    @Autowired
    private NumberGeneratorService numberGeneratorService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for(Map.Entry<String,MessageConsumer> entry: consumerMap.entrySet()){
            log.info("application started queueName:{}",entry.getKey());
        }
        numberGeneratorService.registerNumberConfig(numberConfigMap);
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
        initNumberGenConfig(applicationContext);
    }

    /**
     * 获取消费者
     * @param queueName
     * @return
     */
    public static MessageConsumer getConsumer(String queueName){
        return consumerMap.get(queueName);
    }

    /**
     * 注册业务编码
     * @param context
     */
    private void initNumberGenConfig(ApplicationContext context){
        Map<String, Object> configs=context.getBeansWithAnnotation(NumbGenConfigAnnotation.class);
        for(Map.Entry<String,Object> entry:configs.entrySet()){
            numberConfigMap.put(entry.getKey(),entry.getValue());
        }
    }
}
