package com.lz.mobilefollow.dao;

/**
 * Created by LZ on 2016/10/13.
 * 接收消息类
 */

public class ResiveMessage {

    private int messageType;//消息类型
    private int commandType;//指令类型
    private String command;//指令
    private String extraMessage;//附加信息
    private String remark;//备注

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
