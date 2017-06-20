package com.iflytek.voicecloud.itm.entity.analysis;

import java.util.Date;

/**
 *  t_overall_* 数据实体类
 */
public class OverAllData {

    private long id;

    private Date updateTime;

    private String containerId;

    private String webType;

    private int pv;

    private int uv;

    @Override
    public String toString() {
        return "OverAllData{" +
                "id=" + id +
                ", containerId='" + containerId + '\'' +
                ", webType='" + webType + '\'' +
                ", pv=" + pv +
                ", uv=" + uv +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
