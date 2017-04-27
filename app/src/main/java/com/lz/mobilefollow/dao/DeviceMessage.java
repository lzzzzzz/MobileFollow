package com.lz.mobilefollow.dao;

import com.lz.mobilefollow.lzcore.base.BaseApplication;
import com.lz.mobilefollow.lzcore.util.SharedPreferenceUtils;

/**
 * Created by LZ on 2016/11/1.
 */

public class DeviceMessage {
    private DeviceEntity deviceInfo;//硬件信息
    String user_id= String.valueOf(SharedPreferenceUtils.getSPInt(BaseApplication.getCurrentContext(),"user_id"));
    private String authen_secret_key= user_id;
    private long timeStamp;//时间戳
    private String extraMessage;//附加信息



    public DeviceEntity getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceEntity deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getAuthen_secret_key() {
        return authen_secret_key;
    }

    public void setAuthen_secret_key(String authen_secret_key) {
        this.authen_secret_key = authen_secret_key;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }
}
