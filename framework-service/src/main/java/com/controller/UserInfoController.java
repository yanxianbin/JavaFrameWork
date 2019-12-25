package com.controller;

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
        return JsonUtils.deSerializable(numbers);
    }

}
