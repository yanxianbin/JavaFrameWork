package com.convertutils;

import mode.UserInfoRequest;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomPropertyEditorRegister implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(UserInfoRequest.class,"context",new StringToPropertyEditor());
    }
}
