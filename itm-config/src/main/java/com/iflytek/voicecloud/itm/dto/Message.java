package com.iflytek.voicecloud.itm.dto;

/**
 * Created by jdshao on 2017/2/23
 */
public class Message {

    private int state;
    private Object data;

    public Message() {
    }

    public Message(int state, Object data) {
        this.state = state;
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
