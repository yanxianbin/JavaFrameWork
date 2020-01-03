package com.constants;

/**
 * @Classname ResponseCode
 * @Description TODO
 * @Date 2020/1/3 11:01
 * @Created by 125937
 */
public enum ResponseCode implements AppCode {
     SELECT_DATA_ERROR(10001,"数据查询失败"),
     UPDATE_DATA_ERROR(10002,"数据更新失败"),
     DELETE_DATA_ERROR(10003,"数据删除失败"),
     INSERT_DATA_ERROR(10004,"数据新增失败"),
    ;
    private int code;
    private String message;
    ResponseCode(int code,String message){
        this.code=code;
        this.message=message;
    }
    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void setCode(int var1) {
      this.code=var1;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String var1) {
        this.message=var1;
    }
}
