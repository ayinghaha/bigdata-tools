package com.iflytek.voicecloud.itm.entity.config.variable;

import java.util.Date;

/**
 * 数据传输层变量类型 变量
 */
public class DataLayerVar extends Variable {

    /**
     * 引用的数据层变量名
     */
    public String refDataLayerVar;

    /**
     * 数据层版本号
     */
    public String dataLayerVersion;

    /**
     * 数据层变量默认值
     */
    public String defaultValue;

    public DataLayerVar(int id, String itmID, String containerID, String name, String type, Date registTime, Date updateTime) {
        super(id, itmID, containerID, name, type, registTime, updateTime);
    }

    public DataLayerVar() {
    }

    @Override
    public String toString() {
        return "DataLayerVar{" +
                "id=" + id +
                ", itmID='" + itmID + '\'' +
                ", containerID='" + containerID + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                "refDataLayerVar='" + refDataLayerVar + '\'' +
                ", dataLayerVersion='" + dataLayerVersion + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }

    public String getRefDataLayerVar() {
        return refDataLayerVar;
    }

    public void setRefDataLayerVar(String refDataLayerVar) {
        this.refDataLayerVar = refDataLayerVar;
    }

    public String getDataLayerVersion() {
        return dataLayerVersion;
    }

    public void setDataLayerVersion(String dataLayerVersion) {
        this.dataLayerVersion = dataLayerVersion;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
