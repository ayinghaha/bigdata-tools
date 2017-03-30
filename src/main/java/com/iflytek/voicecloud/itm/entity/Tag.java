package com.iflytek.voicecloud.itm.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by jdshao on 2017/3/8
 */
public class Tag {

    /**
     * 主键id
     */
    private int id;

    /**
     * itm 用户标识
     */
    private String itmID;

    /**
     * itm 容器标识
     */
    private String containerID;

    /**
     * tag名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 行为信息，仅 Event 类型需要
     */
    private String action;

    /**
     * 类别信息，仅 Event 类型需要
     */
    private String category;

    /**
     * 标签模板字符串，仅 Event 类型需要
     */
    private String label;

    /**
     * 权重信息，非负整数，仅 Event 类型需要
     */
    private String value;

    /**
     * 是否为调试模式
     */
    private String isDebug;

    /**
     * 触发器
     */
    private List<Trigger> triggers;

    /**
     * 记录时间
     */
    public Date registTime;

    /**
     * 更新时间 默认当前时间戳
     */
    public Date updateTime;

    public Tag() {
    }

    public Tag(String itmID, String containerID, String name, String type, String action, String category, String label, String value, String isDebug, Date registTime, Date updateTime) {
        this.itmID = itmID;
        this.containerID = containerID;
        this.name = name;
        this.type = type;
        this.action = action;
        this.category = category;
        this.label = label;
        this.value = value;
        this.isDebug = isDebug;
        this.registTime = registTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItmID() {
        return itmID;
    }

    public void setItmID(String itmID) {
        this.itmID = itmID;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsDebug() {
        return isDebug;
    }

    public void setIsDebug(String isDebug) {
        this.isDebug = isDebug;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
