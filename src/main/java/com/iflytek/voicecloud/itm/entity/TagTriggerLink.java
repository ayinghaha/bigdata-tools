package com.iflytek.voicecloud.itm.entity;

import java.util.Date;

/**
 * Created by jdshao on 2017/3/8
 */
public class TagTriggerLink {

    /**
     * 主键id
     */
    private int id;

    /**
     * 绑定的tag对象
     */
    private Tag tag;

    /**
     * 绑定的Trigger对象
     */
    private Trigger trigger;

    /**
     * 插入时间
     */
    private Date registTime;

    public TagTriggerLink() {
    }

    public TagTriggerLink(Tag tag, Trigger trigger, Date registTime) {
        this.tag = tag;
        this.trigger = trigger;
        this.registTime = registTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }
}
