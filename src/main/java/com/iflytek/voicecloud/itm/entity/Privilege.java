package com.iflytek.voicecloud.itm.entity;

import java.io.Serializable;

/**
 * 页面权限类
 */
public class Privilege implements Serializable  {

    /**
     * 页面名
     */
    private String name;

    /**
     * 权限值 权限值为0时表示非叶子节点
     */
    private int value;

    /**
     * 是否包含当前权限 1 包含 -1 不包含
     */
    private int checked;

    /**
     * 子权限节点
     */
    private Privilege[] children;

    public Privilege(String name, int value, int checked) {
        this.name = name;
        this.value = value;
        this.checked = checked;
        this.children = new Privilege[]{};
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public Privilege[] getChildren() {
        return children;
    }

    public void setChildren(Privilege[] children) {
        this.children = children;
    }
}
