package com.iflytek.voicecloud.itm.entity.analysis;

import java.util.Date;

/**
 *  t_overall_* 数据实体类
 */
public class OverAllData {

    /**
     * 数据时间
     */
    private Date startTime;

    /**
     * containerId
     */
    private String containerId;

    /**
     * 用户访问web页面，使用的终端类型，分为:pc,pad,phone,other
     */
    private String webType;

    /**
     * pv
     */
    private int pv;

    /**
     * uv 新用户数
     */
    private int uv;

    /**
     * 老用户数
     */
    private int uvOld;

    /**
     * 独立ip数
     */
    private int ipNum;

    /**
     * vv 数
     */
    private int vv;

    /**
     * 跳出量，用户访问一个网站的页面深度为1
     */
    private int depth1;

    /**
     * 二跳量，用户访问一个网站的深度大于等于2
     */
    private int devpthOver2;

    /**
     * 平均停留时间
     */
    private float avgDetentionTime;

    /**
     * 平均访问深度
     */
    private int avgPageDepth;

    @Override
    public String toString() {
        return "OverAllData{" +
                "startTime=" + startTime +
                ", containerId='" + containerId + '\'' +
                ", webType='" + webType + '\'' +
                ", pv=" + pv +
                ", uv=" + uv +
                ", uvOld=" + uvOld +
                ", ipNum=" + ipNum +
                ", vv=" + vv +
                ", depth1=" + depth1 +
                ", devpthOver2=" + devpthOver2 +
                ", avgDetentionTime=" + avgDetentionTime +
                ", avgPageDepth=" + avgPageDepth +
                '}';
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

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getUvOld() {
        return uvOld;
    }

    public void setUvOld(int uvOld) {
        this.uvOld = uvOld;
    }

    public int getIpNum() {
        return ipNum;
    }

    public void setIpNum(int ipNum) {
        this.ipNum = ipNum;
    }

    public int getVv() {
        return vv;
    }

    public void setVv(int vv) {
        this.vv = vv;
    }

    public int getDepth1() {
        return depth1;
    }

    public void setDepth1(int depth1) {
        this.depth1 = depth1;
    }

    public int getDevpthOver2() {
        return devpthOver2;
    }

    public void setDevpthOver2(int devpthOver2) {
        this.devpthOver2 = devpthOver2;
    }

    public float getAvgDetentionTime() {
        return avgDetentionTime;
    }

    public void setAvgDetentionTime(float avgDetentionTime) {
        this.avgDetentionTime = avgDetentionTime;
    }

    public int getAvgPageDepth() {
        return avgPageDepth;
    }

    public void setAvgPageDepth(int avgPageDepth) {
        this.avgPageDepth = avgPageDepth;
    }
}
