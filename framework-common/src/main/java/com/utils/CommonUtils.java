package com.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Classname CommonUtils
 * @Description 通用帮助类
 * @Date 2019/12/25 14:11
 * @Created by 125937
 */
public class CommonUtils {

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
