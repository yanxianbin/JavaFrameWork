package com.controller;

import com.idgenerator.NumberGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.service.UserInfoService;
import com.redis.RedisTemplateService;

@RestController
public class UserInfoController implements UserInfoService {

    @Autowired
    private RedisTemplateService redisTemplateService;

    @Autowired
    private NumberGeneratorService numberGeneratorService;

    @Override
    public Boolean isAdminUser(String userName) {
        return true;
    }

    @Override
    public String getNumber(String numberCode, Integer size) {
        String number=numberGeneratorService.nextNumber(numberCode);
        return number;
    }

}
