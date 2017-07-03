package com.iflytek.voicecloud.itm.entity.config.variable;

import java.util.Date;

/**
 * cookie 类型变量
 */
public class Cookie extends Variable {

    /**
     * 是否显示url解码  true 解码 false 不解码
     */
    public String needDecode;

    /**
     * cookie 名称
     */
    public String cookieName;

    public Cookie(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        super(id, itmID, containerID, name, type, registTime, updateTime);
    }

    public Cookie() {
    }

    public String getNeedDecode() {
        return needDecode;
    }

    public void setNeedDecode(String needDecode) {
        this.needDecode = needDecode;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
