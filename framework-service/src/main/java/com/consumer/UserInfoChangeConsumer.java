package com.consumer;

import com.annotations.MqListenerEndPoint;
import com.constants.Constants;
import com.constants.MqConsumerResult;
import com.entity.UserInfo;
import com.rabbitmq.MessageMode;
import com.rabbitmq.MessageConsumer;
import com.service.UserInfoService;
import com.utils.CommonUtils;
import com.utils.JsonUtils;
import com.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@MqListenerEndPoint(queueName = Constants.USER_INFO_CHANGE_QUEUE)
@Slf4j
public class UserInfoChangeConsumer implements MessageConsumer {
    @Autowired
    private UserInfoService userInfoService;

    @Override
    public MqConsumerResult consumerExecute(MessageMode messageMode) {
        return CommonUtils.messageConsumer(messageMode, (s) -> {
            UserInfo userInfo = JsonUtils.serializable(s, UserInfo.class);
            String redisKey = "Lock:UserInfo" + userInfo.getUserName();
            RedisUtils.tryLock(() -> {
                userInfoService.update(userInfo);
                return true;
            }, redisKey, 10);
            return MqConsumerResult.SUCCESS;
        });
    }
}
