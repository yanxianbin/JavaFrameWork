package com.bean;

import mode.UserInfo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class SpringIocAutowiredDemo {

    @Bean
    public static UserInfo getUserInfo(){
        UserInfo userInfo=new UserInfo();
        userInfo.setId(System.nanoTime());
        userInfo.setAge(20);
        userInfo.setAddress("广东省深圳市宝安区");
        userInfo.setName("颜显斌");
        return userInfo;
    }

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        AutowiredAnnotationBeanPostProcessor postProcessor=new AutowiredAnnotationBeanPostProcessor();
        postProcessor.setBeanFactory(beanFactory);
        BeanDefinitionBuilder beanDefinitionBuilder=BeanDefinitionBuilder.genericBeanDefinition(UserInfo.class)
                .addPropertyValue("name","眼线笔呢")
                .addPropertyValue("id",System.nanoTime()).setScope(BeanDefinition.SCOPE_PROTOTYPE);
        beanFactory.registerBeanDefinition("userInfo",beanDefinitionBuilder.getBeanDefinition());
        BeanDefinition bmd= beanFactory.getBeanDefinition("userInfo");
        for(int i=0;i<4;i++) {
            UserInfo userInfo = beanFactory.getBean("userInfo", UserInfo.class);
            System.out.println(userInfo);
        }
    }
}
