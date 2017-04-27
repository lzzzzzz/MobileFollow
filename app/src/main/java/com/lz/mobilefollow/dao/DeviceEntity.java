package com.lz.mobilefollow.dao;

/**
 * Created by LZ on 2016/10/13.
 * 手机信息实体类
 */
public class DeviceEntity {

    private String imei;//平台imei码
    private String phoneNumber;//手机号码
    private int netWork;//网络状态
    private String longitude;//经度
    private String latitude;//纬度
    private String imsi;

    private String platform;//平台信息
    private String osName;//手机型号
    private String osVersion;//安卓版本号
    private int appVersion;//app版本
    private long timeStmp;//时间戳

    private String vendor;//手机型号
    private String phoneIp;//手机网络ip地址
    private String phoneMac;//mac地址

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getNetWork() {
        return netWork;
    }

    public void setNetWork(int netWork) {
        this.netWork = netWork;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public long getTimeStmp() {
        return timeStmp;
    }

    public void setTimeStmp(long timeStmp) {
        this.timeStmp = timeStmp;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getPhoneIp() {
        return phoneIp;
    }

    public void setPhoneIp(String phoneIp) {
        this.phoneIp = phoneIp;
    }

    public String getPhoneMac() {
        return phoneMac;
    }

    public void setPhoneMac(String phoneMac) {
        this.phoneMac = phoneMac;
    }
}
