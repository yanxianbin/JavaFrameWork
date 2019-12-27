package com.utils;

import com.rabbitmq.MessageMode;
import com.rabbitmq.MqPrincipal;
import com.rabbitmq.MqServiceClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils {

    private static MqServiceClient mqServiceClient;

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
        MqPrincipal orginal = new MqPrincipal();
        orginal.setExchange(exchange);
        orginal.setRoutingKey(routingKey);
        orginal.setQueueName(queueName);
        MessageMode messageMode = new MessageMode();
        messageMode.setPrincipal(orginal);
        messageMode.setMsg(message);
        getMqServiceClient().sendMqMessage(routingKey, exchange, queueName, messageMode);
        return messageMode.getMessageId();
    }

    private static MqServiceClient getMqServiceClient() {
        if (mqServiceClient == null) {
            mqServiceClient = SpringUtils.getBean(MqServiceClient.class);
        }
        return mqServiceClient;
    }
}
