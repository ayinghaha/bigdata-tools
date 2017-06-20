package com.iflytek.voicecloud.itm.entity.config.variable;

import java.util.Date;

/**
 * Variable 变量 基类
 */
public class Variable {

    /**
     * 主键id
     */
    public int id;

    /**
     * itm 用户标识
     */
    public String itmID;

    /**
     * itm 容器标识
     */
    public String containerID;

    /**
     * 变量名称
     */
    public String name;

    /**
     * 变量类型 枚举值
     */
    public String type;

    /**
     * 记录时间
     */
    public Date registTime;

    /**
     * 更新时间 默认当前时间戳
     */
    public Date updateTime;

    public Variable(String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        this.itmID = itmID;
        this.containerID = containerID;
        this.name = name;
        this.type = type;
        this.registTime = registTime;
        this.updateTime = updateTime;
    }

    public Variable(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        this.id = id;
        this.itmID = itmID;
        this.containerID = containerID;
        this.name = name;
        this.type = type;
        this.registTime = registTime;
        this.updateTime = updateTime;
    }

    public Variable() {
    }

    @Override
    public String toString() {
        return "Variable{" +
                "id=" + id +
                ", itmID='" + itmID + '\'' +
                ", containerID='" + containerID + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", registTime=" + registTime +
                ", updateTime=" + updateTime +
                '}';
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
