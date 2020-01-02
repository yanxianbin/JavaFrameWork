package com.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname JsonUtils
 * @Description Json帮助类
 * @Date 2019/12/25 10:09
 * @Created by 125937
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper objectMapper;

    public static String deSerializable(Object object){
        try {
            String objStr = getObjectMapper().writeValueAsString(object);
            return objStr;
        }catch (Exception ex){
            log.error("JsonUtils deSerializable error",ex);
            return null;
        }
    }

    public static <T> T serializable(String json, Class<T> tClass) {
        try {
            T value = getObjectMapper().readValue(json, tClass);
            return value;
        } catch (Exception ex) {
            log.error("JsonUtils serializable error", ex);
            return null;
        }
    }

    public static <T> T serializable(byte[] src, Class<T> tClass) {
        try {
            T value = getObjectMapper().readValue(src, tClass);
            return value;
        } catch (Exception ex) {
            log.error("JsonUtils serializable error", ex);
            return null;
        }
    }

    private static ObjectMapper getObjectMapper(){
        if(objectMapper==null){
            objectMapper=new ObjectMapper();
        }
        return objectMapper;
    }
}
