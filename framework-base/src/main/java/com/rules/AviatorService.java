package com.rules;

import com.rules.mode.MainMode;
import com.rules.mode.SubMode;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Classname AviatorService
 * @Description TODO
 * @Date 2020/3/26 17:19
 * @Created by 125937
 */
public class AviatorService {


    /**
     * 规则信息
     */
    static Map<String,String> ruleInfo=new LinkedHashMap(){
        {
            put("isPhone(subMode_phone) && isPhone(subMode2_phone)","电话号码错误");
            put("Objects.nonNull(subMode) && Objects.nonNull(subMode2)","subMode、subMode2 不能为空");
            put("string.nonEmpty(billNumber)","业务单号不能为空");
            put("string.length(subMode_address)<=128","寄件地址长度错误");
        }
    };

    public static void main(String[] args) {
        MainMode mainMode=new MainMode();
        mainMode.setInvalidFlag("10");
        mainMode.setVirtualFlag("10");
        SubMode subMode=new SubMode();
        subMode.setPhone("02023343434342339234");
        subMode.setAddress("陕西省渭南市潼关县城关街道布施河村湖滨路17号 上海上海市青浦区华新镇朱长村华南路613号 上海上海市青浦区华新镇朱长村华南路613号上海上海市青浦区华新镇朱长村华南路613号上海上海市青浦区华新镇朱长村华南路613号上海上海市青浦区华新镇朱长村华南路613号上海上海市青浦区华新镇朱长村华南路613号上海上海市青浦区华新镇朱长村华南路613号上海上海市青浦区华新镇朱长村华南路613号");
        mainMode.setSubMode(subMode);
        Tuple2<Boolean, String>  tuple2=AviatorUtils.validParam(mainMode,ruleInfo);
        System.out.println(tuple2._2);
    }
}
