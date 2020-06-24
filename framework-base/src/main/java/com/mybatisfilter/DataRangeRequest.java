package com.mybatisfilter;

import java.io.Serializable;

/**
 * @Classname DataRangeRequest
 * @Description TODO
 * @Date 2020/5/28 18:15
 * @Created by 125937
 */
public class DataRangeRequest implements Serializable {

    private String beanName;

    private String serviceName;

    private String colName;

    private String dataKey;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }
}
