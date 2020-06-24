package com.bean;

import mode.UserInfo;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.validation.DataBinder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;

public class DataBinderDemo {
    public static void main(String[] args) throws IntrospectionException {

        UserInfo userInfo=new UserInfo();

        DataBinder dataBinder=new DataBinder(userInfo,"userInfo");
        Map<String,Object> valueMap=new HashMap<>();
        valueMap.put("name","颜显斌");
        valueMap.put("age",30);
        valueMap.put("address","湖南省茶陵县浣溪镇荷塘村");
        valueMap.put("id",System.currentTimeMillis());
        PropertyValues propertyValues=new MutablePropertyValues(valueMap);
        //不忽略未知字段  默认为 true
        //dataBinder.setIgnoreUnknownFields(false);
        dataBinder.setRequiredFields("id","name","age");
        dataBinder.bind(propertyValues);
        System.out.println("userInfo="+userInfo.toString());
        System.out.println("BandingResult="+dataBinder.getBindingResult());

        BeanInfo beanInfo= Introspector.getBeanInfo(UserInfo.class);
    }
}
