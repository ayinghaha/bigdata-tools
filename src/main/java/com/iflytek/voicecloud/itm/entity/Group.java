package com.iflytek.voicecloud.itm.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by jdshao on 2017/4/18
 */
public class Group {

    private int id;

    /**
     * 用户组名 客户的概念
     */
    private String name;

    /**
     * 所在企业
     */
    private String company;

    /**
     * 客户备注 用户组备注
     */
    private String remark;

    /**
     * 用户组对应的itmID
     */
    private String itmID;


    /**
     * 分析后台token
     */
    private String token;

    /**
     * 记录时间
     */
    private Date registTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;

    /**
     * 用户列表
     */
    private List<User> users;

    public Group() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getItmID() {
        return itmID;
    }

    public void setItmID(String itmID) {
        this.itmID = itmID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
