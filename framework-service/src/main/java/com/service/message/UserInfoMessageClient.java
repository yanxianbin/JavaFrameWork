package com.service.message;

import com.constants.MessageConsumerType;
import com.messageframe.receiveclient.ReceiveClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Classname UserInfoMessageClient
 * @Description TODO
 * @Date 2020/1/3 19:21
 * @Created by 125937
 */
@Component
@Slf4j
public class UserInfoMessageClient implements ReceiveClient {
    @Override
    public String businessType() {
        return "userinfo";
    }

    @Override
    public boolean execute(String message) {
        log.info("UserInfoMessageClient execute message:{}",message);
        return true;
    }

    @Override
    public MessageConsumerType consumerType() {
        return MessageConsumerType.SINGLE;
    }
}
