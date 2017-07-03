package com.iflytek.voicecloud.itm.entity.config;

import com.iflytek.voicecloud.itm.entity.config.variable.Variable;

import java.util.Date;

/**
 * Created by jdshao on 2017/3/6
 */
public class VariableFilter {

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
     * 过滤器名称
     */
    public String name;

    /**
     * 过滤器绑定变量
     */
    public Variable variable;

    /**
     * 过滤器条件
     */
    public String condition;

    /**
     * 满足条件的值
     */
    public String value;

    /**
     * 内置变量类型
     */
    public String buildInType;

    /**
     * 过滤器绑定的Trigger
     */
    public Trigger trigger;

    /**
     * 记录时间
     */
    public Date registTime;

    /**
     * 更新时间 默认当前时间戳
     */
    public Date updateTime;

    public VariableFilter(String itmID, String containerID, String name, Variable variable, String condition, String value, String buildInType, Trigger trigger, Date registTime, Date updateTime) {
        this.itmID = itmID;
        this.containerID = containerID;
        this.name = name;
        this.variable = variable;
        this.condition = condition;
        this.value = value;
        this.buildInType = buildInType;
        this.trigger = trigger;
        this.registTime = registTime;
        this.updateTime = updateTime;
    }

    public VariableFilter() {
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

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBuildInType() {
        return buildInType;
    }

    public void setBuildInType(String buildInType) {
        this.buildInType = buildInType;
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

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
