package com.iflytek.voicecloud.itm.entity.analysis;

import java.util.Date;

/**
 * Created by jdshao on 2017/6/27
 */
public class PageUrl {

    /**
     * 主键id
     */
    private int id;

    /**
     * 数据时间
     */
    private Date startTime;

    /**
     * containerId
     */
    private String containerId;

    /**
     * 页面类型
     */
    private String webType;

    /**
     * 页面url
     */
    private String pageUrl;

    /**
     * pv 值
     */
    private int pv;

    /**
     * vv 值
     */
    private int vv;

    /**
     * 平均访问时长
     */
    private float avgDetentionTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getWebType() {
        return webType;
    }

    public void setWebType(String webType) {
        this.webType = webType;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getVv() {
        return vv;
    }

    public void setVv(int vv) {
        this.vv = vv;
    }

    public float getAvgDetentionTime() {
        return avgDetentionTime;
    }

    public void setAvgDetentionTime(float avgDetentionTime) {
        this.avgDetentionTime = avgDetentionTime;
    }
}
