package com.controller;

import com.messageframe.service.ReceiveMessageService;
import com.mongodb.mode.PageInfo;
import com.entity.UserInfo;
import com.service.UserInfoService;
import com.utils.IdGeneratorClient;
import com.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.service.UserInfoRemotingService;

import java.util.List;

@RestController
public class UserInfoController implements UserInfoRemotingService {

    @Override
    public Boolean isAdminUser(String userName) {
        return true;
    }

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ReceiveMessageService receiveService;

    @Override
    public String getNumber(String numberCode, Integer size) {
        String number= IdGeneratorClient.getNumber(numberCode);
        List<String> numbers=IdGeneratorClient.getNumberList(numberCode,size);
        numbers.add(number);
        //userInfoService.saveUser();
        UserInfo userInfo=userInfoService.findById(662356873630060544L);
        List<UserInfo> list=userInfoService.selectAll(userInfo);
        UserInfo queryT=new UserInfo();
        queryT.setUserName("yxb");
        PageInfo<UserInfo> pageInfo= userInfoService.searchPage(queryT,size,2);

        receiveService.receiveMessage(userInfo,"userinfo",userInfo.getId().toString());

        //CommonUtils.sendMqMessage(Constants.USER_INFO_CHANGE_ROUTING_KEY,Constants.USER_INFO_CHANGE_EXCHANGE,Constants.USER_INFO_CHANGE_QUEUE,"testUserChange");
        //CommonUtils.sendMqMessage(Constants.USER_AGE_CHANGE_ROUTING_KEY,Constants.USER_AGE_CHANGE_EXCHANGE,Constants.USER_AGE_CHANGE_QUEUE,"testAgeChange",6000L);
        return JsonUtils.deSerializable(numbers)+"|"+JsonUtils.deSerializable(pageInfo.getRows());
    }

}
