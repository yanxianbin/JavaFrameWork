package com.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtils {
    /**
     * 发送消息到MQ
     * @param msg
     * @return
     */
    private Long SendMessageToMq(String exchange,String routingKey,String queueName,Object msg){
        return 1L;
    }

    /**
     * 获取当前时间格式化输出
     * @param pattern
     * @return
     */
    public static String getNowDateFormat(String pattern){
        // 将时间对象格式化为字符串
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String strDate = format.format(now);
        return strDate;
    }
}
