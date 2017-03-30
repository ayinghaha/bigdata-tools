package com.iflytek.voicecloud.itm.entity.variable;

import java.util.Date;

/**
 * 随机整数 类型变量
 */
public class RandomNum extends Variable {

    public RandomNum(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        super(id, itmID, containerID, name, type, registTime, updateTime);
    }

    public RandomNum() {
    }
}
