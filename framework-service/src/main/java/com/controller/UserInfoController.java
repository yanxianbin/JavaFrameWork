package com.controller;

import com.constants.Constants;
import com.utils.CommonUtils;
import com.utils.IdGeneratorClient;
import com.utils.JsonUtils;
import org.springframework.web.bind.annotation.RestController;
import com.service.UserInfoService;

import java.util.List;

@RestController
public class UserInfoController implements UserInfoService {

    @Override
    public Boolean isAdminUser(String userName) {
        return true;
    }

    @Override
    public String getNumber(String numberCode, Integer size) {
        String number= IdGeneratorClient.getNumber(numberCode);
        List<String> numbers=IdGeneratorClient.getNumberList(numberCode,size);
        numbers.add(number);
        CommonUtils.sendMqMessage(Constants.USER_INFO_CHANGE_ROUTING_KEY,Constants.USER_INFO_CHANGE_EXCHANGE,Constants.USER_INFO_CHANGE_QUEUE,"testUserChange");
        return JsonUtils.deSerializable(numbers);
    }

}
