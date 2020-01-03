package com.idgenerator;

import com.annotations.SerialNumberAnnotation;
import com.idgenerator.mode.NumberGeneratorMode;
import com.utils.CommonUtils;
import com.utils.RedisUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生成业务编码通用类
 */
@Component
public class NumberGeneratorService {

    /**
     * 存储业务数据
     */
    private final String NUMBER_CODE_KEY="NUMBER_CODE_HAS_SET";

    private final String REDIS_KEY_REPLACE_REGEX = "(\\{([^-|^:|^])*\\})";

    /**
     * 注册业务编码
     *
     * @param configMap
     */
    public void registerNumberConfig(Map<String, Object> configMap) {
        if (configMap != null && !configMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : configMap.entrySet()) {
                NumberGeneratorMode configValue = parseAnnotation(entry.getValue());
                RedisUtils.hashSet(NUMBER_CODE_KEY, configValue.getNumberCode(), configValue);
            }
        }
    }

    /**
     * 获取下一个编码
     * @param numberCode
     * @return
     */
    public String nextNumber(String numberCode){
       return nextNumbers(numberCode,1).get(0);
    }

    /**
     * 获取下一个编码
     * @param numberCode
     * @param size
     * @return
     */
    public List<String> nextNumbers(String numberCode, long size){
        NumberGeneratorMode mode=(NumberGeneratorMode)RedisUtils.hashGet(NUMBER_CODE_KEY,numberCode);
        Assert.notNull(mode,"业务编码未注册");
        List<String> numberList=new ArrayList<>();
        String dateFormat=CommonUtils.getNowDateFormat(mode.getCodeFormat());
        String redisKey=mode.getNumberCode()+ dateFormat;
        for(int i=0;i<size;i++){
           long id= RedisUtils.increment(redisKey,mode.getStep());
            RedisUtils.expireKey(redisKey);
           String formatStr="%".concat(mode.getLeftPadChar()).concat(mode.getSequenceLength()+"").concat("d");
           String seqStr=String.format(formatStr,id);
           String number=buildKey(mode.getNumberFormat(),dateFormat,seqStr);
           numberList.add(number);
        }
        return numberList;
    }

    /**
     * 构建编码
     * @param expression
     * @param args
     * @return
     */
    private String buildKey(String expression, String... args){
        String key = expression;
        if(ArrayUtils.isNotEmpty(args)){
            for (String value : args) {
                key = RegExUtils.replaceFirst(key, REDIS_KEY_REPLACE_REGEX, value);
            }
        }
        return key;
    }

    /**
     * 解析注解上的值
     * @param obj
     * @return
     */
    private NumberGeneratorMode parseAnnotation(Object obj){
        SerialNumberAnnotation configAnnotation= obj.getClass().getAnnotation(SerialNumberAnnotation.class);
        NumberGeneratorMode mode=new NumberGeneratorMode();
        mode.setCodeFormat(configAnnotation.codeFormat());
        mode.setLeftPadChar(configAnnotation.leftPadChar());
        mode.setNumberCode(configAnnotation.numberCode());
        mode.setNumberDesc(configAnnotation.numberDesc());
        mode.setStartSequence(configAnnotation.startSequence());
        mode.setSequenceLength(configAnnotation.sequenceLength());
        mode.setNumberFormat(configAnnotation.numberFormat());
        mode.setStep(configAnnotation.step());
        return mode;
    }
}
