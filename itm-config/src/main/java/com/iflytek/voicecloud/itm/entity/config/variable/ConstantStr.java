package com.iflytek.voicecloud.itm.entity.config.variable;

import java.util.Date;

/**
 * 常量类型变量
 */
public class ConstantStr extends Variable {

    /**
     * 常量值
     */
    public String constantValue;

    public ConstantStr(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        super(id, itmID, containerID, name, type, registTime, updateTime);
    }

    public ConstantStr() {
    }

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }
}
