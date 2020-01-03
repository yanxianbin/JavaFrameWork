package com.messageframe.receiveclient;

import com.constants.MessageConsumerType;

/**
 * @Classname ReceiveClient
 * @Description 消费客户端
 * @Date 2020/1/3 18:37
 * @Created by 125937
 */
public interface ReceiveClient {

    /**
     * 业务类型
     * @return
     */
    String businessType();

    /**
     * 执行
     * @param message
     * @return
     */
    boolean execute(String message);

    /**
     * 消费方式
     * @return
     */
    MessageConsumerType consumerType();
}
