package com.rules;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import io.vavr.Tuple2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Classname AviatorUtils
 * @Description 计算帮助类
 * @Date 2020/5/26 18:37
 * @Created by 125937
 */
@Component
@Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AviatorUtils {

    private static final String ATTR_SPLIT_CHAR="_";

    @PostConstruct
    private void aviatorInit(){
        AviatorEvaluator.addFunction(new IsPhoneFunction());
        AviatorEvaluator.addFunction(new nonNullFunction());
        AviatorEvaluator.addFunction(new stringNonEmptyFunction());
        AviatorEvaluator.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL,true);
    }

    /**
     * 验证参数通用类
     * @param data 数据
     * @param checkRuleMap 校验规则
     * @param <T> 数据
     * @return
     */
    public <T> Tuple2<Boolean, String> validParam(T data, Map<String, String> checkRuleMap) {
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
    private <T> void getParamObjectMap(T data, Map<String, Object> valueData, String keyPre) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
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

     class IsPhoneFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            String value= FunctionUtils.getStringValue(arg1,env);
            Boolean bl=true;
            if(StringUtils.hasText(value)){
                String regx="\\d{11,64}";
                bl= Pattern.matches(regx, value);
            }
            return AviatorBoolean.valueOf(bl);
        }

        public String getName() {
            return "isPhone";
        }
    }

     class nonNullFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            Object value=FunctionUtils.getJavaObject(arg1,env);
            Boolean bl=Objects.nonNull(value);
            return AviatorBoolean.valueOf(bl);
        }

        public String getName() {
            return "Objects.nonNull";
        }
    }

     class stringNonEmptyFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            String value=FunctionUtils.getStringValue(arg1,env);
            return AviatorBoolean.valueOf(StringUtils.hasText(value));
        }

        public String getName() {
            return "string.nonEmpty";
        }
    }
}
