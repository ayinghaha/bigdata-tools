package com.iflytek.voicecloud.itm.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by jdshao on 2017/3/7
 */
public class Trigger {

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
     * 触发器名称
     */
    public String name;

    /**
     * 触发器类型
     */
    public String type;

    /**
     * 延迟时间
     */
    public String delay;

    /**
     * 过滤器列表
     */
    public List<VariableFilter> variableFilters;

    /**
     * 记录时间
     */
    public Date registTime;

    /**
     * 更新时间 默认当前时间戳
     */
    public Date updateTime;

    public Trigger() {
    }

    public Trigger(String itmID, String containerID, String name, String type, String delay, Date registTime, Date updateTime) {
        this.itmID = itmID;
        this.containerID = containerID;
        this.name = name;
        this.type = type;
        this.delay = delay;
        this.registTime = registTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Trigger{" +
                "id=" + id +
                ", itmID='" + itmID + '\'' +
                ", containerID='" + containerID + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", variableFilters=" + variableFilters +
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

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public List<VariableFilter> getVariableFilters() {
        return variableFilters;
    }

    public void setVariableFilters(List<VariableFilter> variableFilters) {
        this.variableFilters = variableFilters;
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
