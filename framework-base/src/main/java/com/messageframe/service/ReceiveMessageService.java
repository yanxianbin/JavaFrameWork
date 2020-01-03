package com.messageframe.service;

import com.constants.Constants;
import com.constants.MessageStateEnum;
import com.messageframe.mode.ReceiveMode;
import com.mongodb.MongoGenericService;
import com.utils.CommonUtils;
import com.utils.JsonUtils;
import com.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Classname ReceiveService
 * @Description TODO
 * @Date 2020/1/3 14:13
 * @Created by 125937
 */
@Service
public class ReceiveMessageService extends MongoGenericService<ReceiveMode> {
    public ReceiveMessageService(@Autowired ReceiveMessageDao genDaoMap) {
        super(genDaoMap);
    }

    /**
     * 默认的队列数据
     */
    private Integer DEFAULT_QUEUE_COUNT=1;

    /**
     * 接入消息
     * @param messageObj
     * @param businessType
     * @param businessNumber
     */
    public void receiveMessage(Object messageObj, String businessType, String businessNumber) {
        ReceiveMode receiveMode = new ReceiveMode();
        receiveMode.setBusinessNumber(businessNumber);
        receiveMode.setMessage(JsonUtils.deSerializable(messageObj));
        receiveMode.setBusinessType(businessType);
        receiveMode.setExecuteState(MessageStateEnum.INIT.getCode());
        insert(receiveMode);
        sendMessage(receiveMode);
    }

    /**
     * 发送消息到队列中
     * @param receiveMode
     */
    private void sendMessage(ReceiveMode receiveMode) {
        String exchange = Constants.MESSAGE_FRAME_EXCHANGE_A;
        String routingKey = Constants.MESSAGE_FRAME_ROUTINGKEY_A;
        String queueName = Constants.MESSAGE_FRAME_QUEUE_A;
        String configQueue= SpringUtils.getPropertiesValue("spring.message.queuesize");
        Integer queueCount=DEFAULT_QUEUE_COUNT;
        if(!StringUtils.isEmpty(configQueue)){
            queueCount=Integer.valueOf(configQueue);
        }
        long index = receiveMode.getId() % queueCount;
        if (index > 0) {
            exchange += "-" + index;
            routingKey += "-" + index;
            queueName += "-" + index;
        }
        CommonUtils.sendMqMessage(routingKey, exchange, queueName, receiveMode);
    }
}
