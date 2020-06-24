package com.convertutils;


import java.beans.PropertyEditor;

public class PropertyEditorDemo {
    public static void main(String[] args) {
        //模拟Spring FrameWork 操作
        String text="name=颜显斌";
        PropertyEditor propertiesEditor=new StringToPropertyEditor();
        propertiesEditor.setAsText(text);
    }
}
