package com.iflytek.voicecloud.itm.entity;

import java.util.Date;

/**
 * Created by jdshao on 2017/4/19
 */
public class UserGroupLink {

    private int id;

    /**
     * 绑定用户组
     */
    private Group group;

    /**
     * 绑定用户
     */
    private User user;

    /**
     * 插入时间
     */
    private Date registDate;

    public UserGroupLink() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }
}
