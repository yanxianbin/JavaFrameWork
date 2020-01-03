package com.messageframe.receiveclient;

import com.constants.ApplicationException;
import com.constants.MessageStateEnum;
import com.messageframe.mode.ReceiveMode;
import com.messageframe.service.ReceiveMessageService;
import com.startup.InitializationMqListener;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * @Classname MessageConsumerService
 * @Description 消息消费类
 * @Date 2020/1/3 17:27
 * @Created by 125937
 */
@Service
public class MessageConsumerService {

    @Autowired
    private ReceiveMessageService receiveService;

    public boolean consumer(ReceiveMode message){
        boolean isSuccess = false;
        ReceiveMode entity = receiveService.findById(message.getId());
        if (Objects.nonNull(entity)) {
            long startTime = System.currentTimeMillis();
            entity.setExeStartTime(new Timestamp(System.currentTimeMillis()));
            ReceiveClient client = InitializationMqListener.getMessageClient(message.getBusinessType());
            if (Objects.isNull(client)) {
                entity.setFailReason(String.format("业务类型：%s 客户端未注册", message.getBusinessType()));
            } else {
                try {
                    isSuccess = client.execute(message.getMessage());
                } catch (Exception ex) {
                    entity.setFailReason(CommonUtils.getExceptionMsg(ex));
                }
            }
            entity.setExecuteState(isSuccess ? MessageStateEnum.SUCCESS.getCode() : MessageStateEnum.FAILED.getCode());
            entity.setExeEndTime(new Timestamp(System.currentTimeMillis()));
            entity.setExecuteCount(CommonUtils.ifNull(entity.getExecuteCount()) + 1);
            entity.setExecuteTimes(System.currentTimeMillis() - startTime);
            receiveService.update(entity);
      }
      return isSuccess;
    }

//    private List<ReceiveClient> scanClient(){
//
//    }
}
