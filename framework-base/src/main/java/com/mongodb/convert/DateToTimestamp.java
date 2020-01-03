package com.mongodb.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Classname DateToTimestamp
 * @Description date 转 timespan
 * @Date 2020/1/3 9:46
 * @Created by 125937
 */
@ReadingConverter
public class DateToTimestamp implements Converter<Date,Timestamp> {

    @Override
    public Timestamp convert(Date date) {
        Timestamp ts = new Timestamp(date.getTime());
        return ts;
    }
}
