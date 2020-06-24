package com.convertutils;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 *
 */
public class StringToPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text){
        Properties properties=new Properties();
        try {
            properties.load(new StringReader(text));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //临时存储
        setValue(properties);
    }
}
