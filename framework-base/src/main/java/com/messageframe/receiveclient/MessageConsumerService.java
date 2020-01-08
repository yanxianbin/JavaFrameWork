package com.messageframe.receiveclient;

import com.constants.MessageConsumerType;
import com.constants.MessageStateEnum;
import com.messageframe.mode.MsgConsumerResult;
import com.messageframe.mode.ReceiveMode;
import com.messageframe.service.ReceiveMessageService;
import com.startup.InitializationMqListener;
import com.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Classname MessageConsumerService
 * @Description 消息消费类
 * @Date 2020/1/3 17:27
 * @Created by 125937
 */
@Service
@Slf4j
public class MessageConsumerService {

    @Autowired
    private ReceiveMessageService receiveService;

    /**
     * 消费消息
     * @param message
     * @return
     */
    public boolean consumer(ReceiveMode message) {
        MsgConsumerResult result = new MsgConsumerResult();
        ReceiveMode entity = receiveService.findById(message.getId());
        if (Objects.nonNull(entity)) {
            long startTime = System.currentTimeMillis();
            entity.setExeStartTime(new Timestamp(System.currentTimeMillis()));
            ReceiveClient client = InitializationMqListener.getMessageClient(message.getBusinessType());
            if (Objects.isNull(client)) {
                entity.setFailReason(String.format("业务类型：%s 客户端未注册", message.getBusinessType()));
                receiveService.update(entity);
            } else {
                try {
                    //调用对应的消费者
                    result = executeMessage(message, client);
                    updateOtherMessage(result, startTime);
                } catch (Exception ex) {
                    entity.setFailReason(CommonUtils.getExceptionMsg(ex));
                    receiveService.update(entity);
                }
            }
        }
        return result.getExecuteState().equals(MessageStateEnum.SUCCESS);
    }


    /**
     * 调用消费者
     *
     * @param message
     * @param client
     * @return
     */
    private MsgConsumerResult executeMessage(ReceiveMode message, ReceiveClient client) {
        MsgConsumerResult result =null;
        if (client.businessType().equals(MessageConsumerType.LAST)) {
            ReceiveMode lastMessage = getLastMessage(message.getBusinessNumber(), message.getBusinessType());
            LinkedHashMap<Long, String> linkedMap = new LinkedHashMap<>();
            linkedMap.put(lastMessage.getId(), lastMessage.getMessage());
            result = client.execute(linkedMap);
        } else if (client.businessType().equals(MessageConsumerType.SORT_LIST)) {
            List<ReceiveMode> messageList = sortList(message.getBusinessNumber(), message.getBusinessType(), null);
            if (!CollectionUtils.isEmpty(messageList)) {
                LinkedHashMap<Long, String> linkedHashMap = new LinkedHashMap<>();
                messageList.forEach(x -> linkedHashMap.put(x.getId(), x.getMessage()));
                result = client.execute(linkedHashMap);
            }
        } else {
            LinkedHashMap<Long, String> linkedMap = new LinkedHashMap<>();
            linkedMap.put(message.getId(), message.getMessage());
            result=client.execute(linkedMap);
        }
        return result;
    }


    /**
     * 更新消息
     * @param result
     */
    private void updateOtherMessage(MsgConsumerResult result,long startTime) {
        if (!CollectionUtils.isEmpty(result.getSuccessData())) {
            for (Long id : result.getSuccessData()) {
                ReceiveMode entity = receiveService.findById(id);
                entity.setExecuteState(MessageStateEnum.SUCCESS.getCode());
                entity.setExeStartTime(new Timestamp(startTime));
                entity.setExeEndTime(new Timestamp(System.currentTimeMillis()));
                entity.setExecuteCount(CommonUtils.ifNull(entity.getExecuteCount()) + 1);
                entity.setExecuteTimes(System.currentTimeMillis() - startTime);
                receiveService.update(entity);
            }
        }
        if (!result.getErrorData().isEmpty()) {
            for (Map.Entry<Long, String> error : result.getErrorData().entrySet()) {
                ReceiveMode entity = receiveService.findById(error.getKey());
                entity.setExecuteState(MessageStateEnum.FAILED.getCode());
                entity.setExeStartTime(new Timestamp(startTime));
                entity.setExeEndTime(new Timestamp(System.currentTimeMillis()));
                entity.setExecuteCount(CommonUtils.ifNull(entity.getExecuteCount()) + 1);
                entity.setExecuteTimes(System.currentTimeMillis() - startTime);
                entity.setFailReason(error.getValue());
                receiveService.update(entity);
            }
        }
    }

    /**
     * 按照顺序回放
     * @param businessNumber
     * @param businessType
     * @return
     */
    private List<ReceiveMode> sortList(String businessNumber,String businessType,String state){
        Criteria criteria=Criteria.where("businessNumber").is(businessNumber).and("businessType").is(businessType);
        if(!StringUtils.isEmpty(state)){
            criteria=criteria.and("executeState").is(state);
        }
        Query query=new Query(criteria);
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC,"createTime")));
        List<ReceiveMode> receiveModeList= receiveService.find(query);
        if(CollectionUtils.isEmpty(receiveModeList)){
            return receiveModeList;
        }
        return null;
    }

    /**
     * 获取最后一条记录
     * @param businessNumber
     * @param businessType
     * @return
     */
    private ReceiveMode getLastMessage(String businessNumber,String businessType){
        Criteria criteria=Criteria.where("businessNumber").is(businessNumber).and("businessType").is(businessType);
        Query query=new Query(criteria);
        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC,"createTime")));
        query.limit(1);
        List<ReceiveMode> receiveModeList= receiveService.find(query);
        if(CollectionUtils.isEmpty(receiveModeList)){
            return receiveModeList.get(0);
        }
        return null;
    }
}
