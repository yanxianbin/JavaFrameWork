package com.utils;

import java.sql.Timestamp;
import java.util.UUID;

public class IdGeneratorClient {

    /**
     * 获取字符ID
     * @return
     */
    private static String getGuidId(){
        String id= UUID.randomUUID().toString().replace("-","");
        return id;
    }

    /**
     * 获取数值ID
     * @return
     */
    private static Long getLongId(){
        Long timespan=new Timestamp(System.currentTimeMillis()).getTime();
        return timespan;
    }
}
