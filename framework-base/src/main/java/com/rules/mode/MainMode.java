package com.rules.mode;

import java.io.Serializable;

/**
 * @Classname MainMode
 * @Description TODO
 * @Date 2020/5/25 16:31
 * @Created by 125937
 */
public class MainMode implements Serializable {

    private String invalidFlag;

    private String billNumber;

    private String virtualFlag;

    private SubMode subMode;

    private SubMode2 subMode2;

    public String getInvalidFlag() {
        return invalidFlag;
    }

    public void setInvalidFlag(String invalidFlag) {
        this.invalidFlag = invalidFlag;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getVirtualFlag() {
        return virtualFlag;
    }

    public void setVirtualFlag(String virtualFlag) {
        this.virtualFlag = virtualFlag;
    }

    public SubMode getSubMode() {
        return subMode;
    }

    public void setSubMode(SubMode subMode) {
        this.subMode = subMode;
    }

    public SubMode2 getSubMode2() {
        return subMode2;
    }

    public void setSubMode2(SubMode2 subMode2) {
        this.subMode2 = subMode2;
    }
}
