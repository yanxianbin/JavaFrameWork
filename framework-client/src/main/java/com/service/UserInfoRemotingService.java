package com.service;

import com.entity.UserInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/")
public interface UserInfoRemotingService {

    @RequestMapping("/isAdminUser")
    Boolean isAdminUser(@RequestParam("userName")String userName);

    @RequestMapping("/getNumber/{numberCode}/{size}")
    String getNumber(@PathVariable("numberCode")String numberCode,@PathVariable("size")Integer size);

    @RequestMapping("/getuserinfo/{pageIndex}")
    List<UserInfo> getUserInfo(@PathVariable("pageIndex")Integer numberCode);
}
