package com.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import java.util.Map;

/**
 * Spring 工具类
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SuppressWarnings("unchecked")
public class SpringUtils implements ApplicationContextAware, EmbeddedValueResolverAware {

    private static ApplicationContext applicationContext;

    private static StringValueResolver stringValueResolver;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        if(applicationContext.containsBean(beanName)){
            return (T) applicationContext.getBean(beanName);
        }else{
            return null;
        }
    }

    public static <T> T getBean(Class<T> bean) {
       return applicationContext.getBean(bean);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> baseType){
        return applicationContext.getBeansOfType(baseType);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.stringValueResolver=stringValueResolver;
    }

    /**
     * 动态获取配置文件中的值
     * @param name
     * @return
     */
    public static String getPropertiesValue(String name) {
        try {
            name = "${" + name + "}";
            return stringValueResolver.resolveStringValue(name);
        } catch (Exception e) {
            // 获取失败则返回null
            return null;
        }
    }

    /**
     * 发布事件
     * @param event
     */
    public static void publisherEvent(ApplicationEvent event){
        applicationContext.publishEvent(event);
    }
}
