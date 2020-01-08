package com.consumer;

import com.annotations.MqListenerEndPointDelay;
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

/**
 * @Classname UserAgeChangeConsumer
 * @Description TODO
 * @Date 2019/12/31 12:08
 * @Created by 125937
 */
@MqListenerEndPointDelay(queueName = Constants.USER_AGE_CHANGE_QUEUE)
@Slf4j
public class UserAgeChangeConsumer implements MessageConsumer {
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
