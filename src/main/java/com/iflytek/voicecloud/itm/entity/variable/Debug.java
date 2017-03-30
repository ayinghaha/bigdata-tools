package com.iflytek.voicecloud.itm.entity.variable;

import java.util.Date;

/**
 * Debug类型变量
 */
public class Debug extends Variable {

    public Debug(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        super(id, itmID, containerID, name, type, registTime, updateTime);
    }

    public Debug() {
    }

}
