package com.iflytek.voicecloud.itm.entity.variable;

import java.util.Date;

/**
 * javaScript变量 类型变量
 */
public class JSVar extends Variable {

    /**
     * 应用的JavaScript变量
     */
    public String refJsVar;

    public JSVar(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        super(id, itmID, containerID, name, type, registTime, updateTime);
    }

    public JSVar() {
    }

    public String getRefJsVar() {
        return refJsVar;
    }

    public void setRefJsVar(String refJsVar) {
        this.refJsVar = refJsVar;
    }
}
