package entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class BaseEntity<T> implements Serializable {
    private T id;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String createBy;

    private String updateBy;

    private int enabledFlag=1;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public int getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(int enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
}
