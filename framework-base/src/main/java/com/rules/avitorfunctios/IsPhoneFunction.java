package com.rules.avitorfunctios;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class IsPhoneFunction extends AbstractFunction {
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
