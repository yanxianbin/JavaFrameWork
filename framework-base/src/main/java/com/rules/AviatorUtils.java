package com.rules;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.rules.avitorfunctios.IsPhoneFunction;
import com.rules.avitorfunctios.NonNullFunction;
import com.rules.avitorfunctios.StringNonEmptyFunction;
import io.vavr.Tuple2;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Classname AviatorUtils
 * @Description 计算帮助类
 * @Date 2020/5/26 18:37
 * @Created by 125937
 */
public class AviatorUtils {

    private static final String ATTR_SPLIT_CHAR="_";

    static {
        AviatorEvaluator.addFunction(new NonNullFunction());
        AviatorEvaluator.addFunction(new StringNonEmptyFunction());
        AviatorEvaluator.addFunction(new IsPhoneFunction());
        AviatorEvaluator.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL,true);
        //设置计算优先
        AviatorEvaluator.setOption(Options.OPTIMIZE_LEVEL,AviatorEvaluator.EVAL);
    }

    /**
     * 验证参数通用类
     * @param data 数据
     * @param checkRuleMap 校验规则
     * @param <T> 数据
     * @return
     */
    public static  <T> Tuple2<Boolean, String> validParam(T data, Map<String, String> checkRuleMap) {
        Assert.notNull(data,"数据不能为空");
        Assert.notNull(data.getClass().getClassLoader(),"不支持Java原生类型");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Map<String, Object> paramObject = new HashMap<>();
            long startTime = System.currentTimeMillis();
            getParamObjectMap(data, paramObject, null);
            System.out.printf("getParamObjectMap times:%d ms %n", (System.currentTimeMillis() - startTime));
            for (Map.Entry<String, String> entry : checkRuleMap.entrySet()) {
                Object result = AviatorEvaluator.execute(entry.getKey(), paramObject);
                if (!(Boolean) result) {
                    stringBuilder.append(entry.getValue()).append(";");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Tuple2(stringBuilder.length() < 1, stringBuilder.toString());
    }

    /**
     * 把Entity 转换成 Map
     * @param data
     * @param valueData
     * @param keyPre
     * @param <T>
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private static  <T> void getParamObjectMap(T data, Map<String, Object> valueData, String keyPre) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        if(Objects.isNull(data)){
            return;
        }
        Field[] fields=data.getClass().getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            if (!StringUtils.isEmpty(keyPre)) {
                key = keyPre + ATTR_SPLIT_CHAR + key;
            }
            field.setAccessible(true);
            Object val = field.get(data);
            // Java的内置类，是没有ClassLoader的
            if (field.getType().getClassLoader()!=null) {
                valueData.put(key, val);
                if(Objects.isNull(val)){
                    val=field.getType().getDeclaredConstructor().newInstance();
                }
                getParamObjectMap(val,valueData, field.getName());
            } else if (val instanceof Collection) {
                Collection list = (Collection) val;
                if (!CollectionUtils.isEmpty(list)) {
                    for(Object entity : list){
                        getParamObjectMap(entity, valueData,field.getName());
                    }
                }
            } else {
                if (!valueData.containsKey(field.getName())) {
                    valueData.put(key, val);
                }
            }
        }
    }
}
