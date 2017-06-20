package com.iflytek.voicecloud.itm.entity.config.variable;

import java.util.Date;

/**
 * DOM元素类型变量
 */
public class DOMElement extends Variable {

    /**
     * 选择方法 元素ID CSS选择器
     */
    public String selectType;

    /**
     * 元素标识 id或class
     */
    public String selectParam;

    /**
     * 元素属性名
     */
    public String attrName;

    public DOMElement(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        super(id, itmID, containerID, name, type, registTime, updateTime);
    }

    public DOMElement() {
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getSelectParam() {
        return selectParam;
    }

    public void setSelectParam(String selectParam) {
        this.selectParam = selectParam;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
}
