package com.iflytek.voicecloud.itm.entity.config.variable;

import java.util.Date;

/**
 * 自定义js类型变量
 */
public class CustomJS extends Variable {

    /**
     * 自定义js代码
     */
    public String jsScript;

    public CustomJS(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        super(id, itmID, containerID, name, type, registTime, updateTime);
    }

    public CustomJS() {
    }

    public String getJsScript() {
        return jsScript;
    }

    public void setJsScript(String jsScript) {
        this.jsScript = jsScript;
    }
}
