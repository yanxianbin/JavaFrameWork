package com.utils;

import com.constants.MqConsumerResult;
import com.google.common.base.Throwables;
import com.messageframe.mode.ReceiveMode;
import com.messageframe.receiveclient.MessageConsumerService;
import com.rabbitmq.MessageMode;
import com.rabbitmq.MqPrincipal;
import com.rabbitmq.MqServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class CommonUtils {

    private static MqServiceClient mqServiceClient;

    private static MessageConsumerService messageConsumerService;

    /**
     * 获取当前时间格式化输出
     * @param pattern
     * @return
     */
    public static String getNowDateFormat(String pattern){
        // 将时间对象格式化为字符串
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String strDate = format.format(now);
        return strDate;
    }

    /**
     * 发送消息到队列中
     *
     * @param routingKey
     * @param exchange
     * @param queueName
     * @param message
     * @return
     */
    public static Long sendMqMessage(String routingKey, String exchange, String queueName, Object message) {
        MessageMode messageMode = getMessageMode(routingKey, exchange, queueName, message);
        getMqServiceClient().sendMqMessage(routingKey, exchange, queueName, messageMode);
        return messageMode.getMessageId();
    }

    /**
     * 发送消息到队列中-延时队列
     *
     * @param routingKey
     * @param exchange
     * @param queueName
     * @param message
     * @return
     */
    public static Long sendMqMessage(String routingKey, String exchange, String queueName, Object message,Long ttl) {
        MessageMode messageMode = getMessageMode(routingKey, exchange, queueName, message);
        getMqServiceClient().sendMqMessage(routingKey, exchange, queueName, messageMode,ttl);
        return messageMode.getMessageId();
    }

    public static MqConsumerResult receiveCommonMessage(MessageMode messageMode){
        if (StringUtils.isEmpty(messageMode.getMsg())) {
            return MqConsumerResult.SUCCESS;
        }
        log.info("receiveCommonMessage queueName:{} message:{}", messageMode.getPrincipal().getQueueName(), messageMode.getMsg());
        ReceiveMode receiveMode = JsonUtils.serializable(messageMode.getMsg(), ReceiveMode.class);
        try {
            boolean isSuccess = getMessageConsumerService().consumer(receiveMode);
            if (isSuccess) {
                return MqConsumerResult.SUCCESS;
            }
        }catch (Exception ex){
            log.error("receiveCommonMessage error:{}",getExceptionMsg(ex));
        }
        return MqConsumerResult.FAILED;
    }

    public static Long ifNull(Long value){
        return Objects.isNull(value) ? 0 : value;
    }

    public static Integer ifNull(Integer value){
        return Objects.isNull(value) ? 0 : value;
    }

    /**
     * 获取异常信息，报空指针时则获取堆栈信息
     *
     * @param ex
     * @return
     */
    public static String getExceptionMsg(Exception ex) {
        String errorMsg = ex.getMessage();
        if (ex instanceof NullPointerException) {
            errorMsg = Throwables.getStackTraceAsString(ex);
        }
        return errorMsg;
    }

    /**
     * Rsa 加密
     * @param data
     * @return
     */
    public static String encodeByRsa(String data){
        String pubKey=SpringUtils.getPropertiesValue("rsa.publickey");
        try {
            return SecurityUtils.encryptByPublicKey(data, pubKey);
        }catch (Exception ex){
            log.error("encodeByRsa, error:{}",getExceptionMsg(ex),ex);
        }
        return null;
    }

    /**
     * 消费消息通用方法
     * @param messageMode
     * @param consumer
     * @return
     */
    public static MqConsumerResult messageConsumer(MessageMode messageMode,Function<String,MqConsumerResult> consumer){
        MqConsumerResult mqConsumerResult=MqConsumerResult.FAILED;
        log.info("messageConsumer queueName:{} message:{}",messageMode.getPrincipal().getQueueName(),messageMode.getMsg());
        if(StringUtils.isEmpty(messageMode.getMsg())){
            return MqConsumerResult.SUCCESS;
        }
        try{
            mqConsumerResult=consumer.apply(messageMode.getMsg());
            return mqConsumerResult;
        }catch (Exception ex){
            log.error("messageConsumer queueName:{} message:{} error:{}",messageMode.getPrincipal().getQueueName(),messageMode.getMsg(),getExceptionMsg(ex),ex);
            return mqConsumerResult;
        }
    }

    /**
     * 构建消息体
     * @param routingKey
     * @param exchange
     * @param queueName
     * @param message
     * @return
     */
    private static MessageMode getMessageMode(String routingKey, String exchange, String queueName, Object message) {
        MqPrincipal orginal = new MqPrincipal();
        orginal.setExchange(exchange);
        orginal.setRoutingKey(routingKey);
        orginal.setQueueName(queueName);
        MessageMode messageMode = new MessageMode();
        messageMode.setPrincipal(orginal);
        messageMode.setMsg(JsonUtils.deSerializable(message));
        return messageMode;
    }

    private static MqServiceClient getMqServiceClient() {
        if (mqServiceClient == null) {
            mqServiceClient = SpringUtils.getBean(MqServiceClient.class);
        }
        return mqServiceClient;
    }

    private static MessageConsumerService getMessageConsumerService() {
        if (messageConsumerService == null) {
            messageConsumerService = SpringUtils.getBean(MessageConsumerService.class);
        }
        return messageConsumerService;
    }
}
