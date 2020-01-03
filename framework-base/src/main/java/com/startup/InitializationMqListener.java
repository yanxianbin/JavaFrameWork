package com.startup;

import com.annotations.MqListenerEndPoint;
import com.annotations.MqListenerEndPointDelay;
import com.annotations.SerialNumberAnnotation;
import com.idgenerator.NumberGeneratorService;
import com.messageframe.receiveclient.ReceiveClient;
import com.rabbitmq.MqServiceClient;
import com.service.MessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
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

    /**
     * 消息框架消费者
     */
    private static final Map<String, ReceiveClient> messageConsumerMap=new ConcurrentHashMap<>();

    private final Map<String,Object> numberConfigMap=new ConcurrentHashMap<>();

    @Autowired
    private NumberGeneratorService numberGeneratorService;

    @Autowired
    private MqServiceClient mqListenerInitService;

    //延时队列后缀
    private static final String DLQ_DELAY_SUFFIX=".DELAY";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //启动Mq监听
        mqListenerInitService.initListenerContainer(consumerMap);
        numberGeneratorService.registerNumberConfig(numberConfigMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] queueEndpoints = applicationContext.getBeanNamesForAnnotation(MqListenerEndPoint.class);
        List<String> queueList = Arrays.asList(queueEndpoints);

        for(String bean : queueList){
            MessageConsumer consumer=(MessageConsumer) applicationContext.getBean(bean);
            Annotation annotation=consumer.getClass().getAnnotations()[0];
            String queueName="";
            if(annotation instanceof MqListenerEndPoint) {
                MqListenerEndPoint endPointListener = (MqListenerEndPoint)annotation;
                queueName= endPointListener.queueName();
            }else {
                MqListenerEndPointDelay endPointListener = (MqListenerEndPointDelay)annotation;
                queueName = endPointListener.queueName();
                queueName += DLQ_DELAY_SUFFIX;
            }
            consumerMap.put(queueName,consumer);
        }
        initNumberGenConfig(applicationContext);
        initMsgConsumer(applicationContext);
    }

    private void initMsgConsumer(ApplicationContext applicationContext){
        String[] clients=applicationContext.getBeanNamesForType(ReceiveClient.class);
        if(clients!=null && clients.length>0) {
            for (String beanName : clients) {
                ReceiveClient consumer=(ReceiveClient) applicationContext.getBean(beanName);
                messageConsumerMap.put(consumer.businessType(),consumer);
            }
        }
    }

    /**
     * 获取消息框架消费者
     * @param businessType
     * @return
     */
    public static ReceiveClient getMessageClient(String businessType){
        return messageConsumerMap.get(businessType);
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
        Map<String, Object> configs=context.getBeansWithAnnotation(SerialNumberAnnotation.class);
        for(Map.Entry<String,Object> entry:configs.entrySet()){
            numberConfigMap.put(entry.getKey(),entry.getValue());
        }
    }
}
