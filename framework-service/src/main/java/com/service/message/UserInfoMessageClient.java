package com.service.message;

import com.constants.MessageStateEnum;
import com.constants.MqConsumerResult;
import com.entity.UserInfo;
import com.messageframe.mode.MsgConsumerResult;
import com.messageframe.receiveclient.ReceiveClient;
import com.redis.redissonlock.LockCallback;
import com.service.UserInfoService;
import com.utils.CommonUtils;
import com.utils.JsonUtils;
import com.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

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

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public MsgConsumerResult execute(LinkedHashMap<Long, String> message) {
        log.info("UserInfoMessageClient execute message:{}",JsonUtils.deSerializable(message));

        String messageStr=message.entrySet().stream().findFirst().get().getValue();
        UserInfo userInfo= JsonUtils.serializable(messageStr,UserInfo.class);
        String redisKey = "Lock:UserInfo" + userInfo.getUserName();
        RedisUtils.tryLock(() -> {
            userInfoService.update(userInfo);
            return true;
        }, redisKey, 10);

        return new MsgConsumerResult(MessageStateEnum.SUCCESS);
    }
}
