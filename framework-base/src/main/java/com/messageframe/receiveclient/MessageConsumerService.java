package com.messageframe.receiveclient;

import com.constants.MessageConsumerType;
import com.constants.MessageStateEnum;
import com.messageframe.mode.ReceiveMode;
import com.messageframe.service.ReceiveMessageService;
import com.startup.InitializationMqListener;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    /**
     * 消费消息
     * @param message
     * @return
     */
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
                    //调用对应的消费者
                    isSuccess = executeMessage(message,client);
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

    private void updateMessageState(ReceiveMode message,ReceiveClient client){
        if(client.businessType().equals(MessageConsumerType.LAST)){

        }
    }

    /**
     * 调用消费者
     * @param message
     * @param client
     * @return
     */
    private boolean executeMessage(ReceiveMode message,ReceiveClient client){
        Boolean bl=false;
        if(client.businessType().equals(MessageConsumerType.LAST)){
           String lastMessage= getLastMessage(message.getBusinessNumber(),message.getBusinessType());
           bl=client.execute(lastMessage);
        }else if(client.businessType().equals(MessageConsumerType.LIST)){
            List<String> messageList=sortList(message.getBusinessNumber(),message.getBusinessType());
            for(String sortMessage : messageList){
                bl=client.execute(sortMessage);
            }
        }else{
            client.execute(message.getMessage());
        }
        return bl;
    }

    /**
     * 按照顺序回放
     * @param businessNumber
     * @param businessType
     * @return
     */
    private List<String> sortList(String businessNumber,String businessType){
        Criteria criteria=Criteria.where("businessNumber").is(businessNumber).and("businessType").is(businessType);
        Query query=new Query(criteria);
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC,"createTime")));
        List<ReceiveMode> receiveModeList= receiveService.find(query);
        if(CollectionUtils.isEmpty(receiveModeList)){
            return receiveModeList.stream().map(x->x.getMessage()).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取最后一条记录
     * @param businessNumber
     * @param businessType
     * @return
     */
    private String getLastMessage(String businessNumber,String businessType){
        Criteria criteria=Criteria.where("businessNumber").is(businessNumber).and("businessType").is(businessType);
        Query query=new Query(criteria);
        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC,"createTime")));
        query.limit(1);
        List<ReceiveMode> receiveModeList= receiveService.find(query);
        if(CollectionUtils.isEmpty(receiveModeList)){
            return receiveModeList.get(0).getMessage();
        }
        return null;
    }
}
