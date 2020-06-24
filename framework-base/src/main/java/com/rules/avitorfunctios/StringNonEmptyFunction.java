package com.rules.avitorfunctios;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.springframework.util.StringUtils;

import java.util.Map;

public class StringNonEmptyFunction extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        String value= FunctionUtils.getStringValue(arg1,env);
        return AviatorBoolean.valueOf(StringUtils.hasText(value));
    }

    public String getName() {
        return "string.nonEmpty";
    }
}
