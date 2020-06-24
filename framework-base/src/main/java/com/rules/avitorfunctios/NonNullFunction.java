package com.rules.avitorfunctios;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;
import java.util.Objects;

public class NonNullFunction extends AbstractFunction {
    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        Object value= FunctionUtils.getJavaObject(arg1,env);
        Boolean bl= Objects.nonNull(value);
        return AviatorBoolean.valueOf(bl);
    }

    public String getName() {
        return "Objects.nonNull";
    }
}
