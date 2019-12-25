package com.idgenerator;

import ch.qos.logback.core.util.TimeUtil;
import com.annotation.NumbGenConfigAnnotation;
import com.idgenerator.mode.NumberGeneratorMode;
import com.redis.RedisTemplateService;
import com.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 生成业务编码通用类
 */
@Component
public class NumberGeneratorService {

    @Autowired
    private RedisTemplateService redisTemplateService;

    /**
     * 存储业务数据
     */
    private final String NUMBER_CODE_KEY="NUMBER_CODE_HAS_SET";

    /**
     * 注册业务编码
     *
     * @param configMap
     */
    public void registerNumberConfig(Map<String, Object> configMap) {
        if (configMap != null && !configMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : configMap.entrySet()) {
                NumberGeneratorMode configValue = parseAnnotation(entry.getValue());
                redisTemplateService.hashSet(NUMBER_CODE_KEY, configValue.getNumberCode(), configValue);
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
        NumberGeneratorMode mode=(NumberGeneratorMode)redisTemplateService.hashGet(NUMBER_CODE_KEY,numberCode);
        Assert.notNull(mode,"业务编码未注册");
        List<String> numberList=new ArrayList<>();
        String dateFormat=CommonUtils.getNowDateFormat(mode.getCodeFormat());
        String redisKey=mode.getNumberCode()+ dateFormat;
        for(int i=0;i<size;i++){
           long id= redisTemplateService.increment(redisKey,mode.getStep());
           String number=String.format(mode.getNumberFormat(),dateFormat,String.format("%%s%s%s",mode.getSequenceLength(),mode.getLeftPadChar(),id));
           numberList.add(number);
        }
        return numberList;
    }

    /**
     * 解析注解上的值
     * @param obj
     * @return
     */
    private NumberGeneratorMode parseAnnotation(Object obj){
        NumbGenConfigAnnotation configAnnotation= obj.getClass().getAnnotation(NumbGenConfigAnnotation.class);
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
