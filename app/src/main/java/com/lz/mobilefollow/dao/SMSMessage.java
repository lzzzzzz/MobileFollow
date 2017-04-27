package com.lz.mobilefollow.dao;


/**
 * Created by LZ on 2016/11/1.
 */

public class SMSMessage {
    private String fromUser;
    private String tomobile;
    private String message;
    private long timeStmp;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getTomobile() {
        return tomobile;
    }

    public void setTomobile(String tomobile) {
        this.tomobile = tomobile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStmp() {
        return timeStmp;
    }

    public void setTimeStmp(long timeStmp) {
        this.timeStmp = timeStmp;
    }
}
