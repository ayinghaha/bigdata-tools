package com.iflytek.voicecloud.itm.entity.config;

import java.util.Date;

/**
 * Created by jdshao on 2017/4/6
 */
public class Container {

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
     * Container 名称
     */
    private String name;

    /**
     * 类型 Web IOS Android
     */
    private String type;

    /**
     * 记录时间
     */
    public Date registTime;

    /**
     * 更新时间 默认当前时间戳
     */
    public Date updateTime;

    public Container() {
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
