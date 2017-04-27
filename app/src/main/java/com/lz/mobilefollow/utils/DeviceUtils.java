package com.lz.mobilefollow.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.lz.mobilefollow.dao.DeviceEntity;
import com.lz.mobilefollow.lzcore.constant.Constant;
import com.lz.mobilefollow.lzcore.constant.MessageConfig;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;
import com.lz.mobilefollow.lzcore.util.SharedPreferenceUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by LZ on 2016/10/14.
 * 手机信息手机工具类
 */

public class DeviceUtils {

    public static DeviceEntity getDeviceInfo(Context context) {
        DeviceEntity device = new DeviceEntity();
        TelephonyManager mTm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String imei = mTm.getDeviceId();
        String imsi = mTm.getSubscriberId();
        String mtype = DeviceUtils.getOSName(); // 手机型号
        String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
        if(!TextUtils.isEmpty(imei)){
            device.setImei(imei);
        }else{
            device.setImei("");
        }
        if(!TextUtils.isEmpty(imsi)){
            device.setImsi(imsi);
        }else{
            device.setImsi("");
        }
        if(!TextUtils.isEmpty(mtype)){
            device.setOsName(mtype);
        }else{
            device.setOsName("");
        }
        if(!TextUtils.isEmpty(numer)){
            device.setPhoneNumber(numer);
        }else{
            device.setPhoneNumber("");
        }
        device.setPhoneNumber(numer==null?"":numer);
        String longitude= SharedPreferenceUtils.getSPString(context, MessageConfig.SP_LOCATION_LONGITUDE);
        String latitude=SharedPreferenceUtils.getSPString(context, MessageConfig.SP_LOCATION_LATITUDE);
        device.setLatitude(latitude==null?"0.00":latitude);
        device.setLongitude(longitude==null?"0.00":longitude);
        int networkName=IntenetUtil.getNetworkState(context);
        device.setNetWork(networkName);
       String platform= Constant.PLATFORM;
        if(!TextUtils.isEmpty(platform)){
            device.setPlatform(platform);
        }else{
            device.setPlatform("");
        }
        String osVersion=DeviceUtils.getOSVersion();
        if(!TextUtils.isEmpty(osVersion)){
            device.setOsVersion(osVersion);
        }else{
            device.setOsVersion("");
        }
        device.setAppVersion(DeviceUtils.getAppVersion(context));
        device.setTimeStmp(System.currentTimeMillis());

        device.setVendor(DeviceUtils.getPhoneVendor());
        device.setPhoneIp(getPhoneIpAddress(context));
        device.setPhoneMac(getWlanMAC(context));
        return device;
    }

    private static Location getLocation(Context context) {
        String locationProvider = "";
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            LogUtils.d( "#没有可用的位置提供器");
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Location location = locationManager.getLastKnownLocation(locationProvider);
            return location;
        }else{
            LogUtils.d("#未获取位置权限");
            return null;
        }
    }

    /**获取app版本号*/
    public static int getAppVersion(Context context){
        PackageManager manager;

        PackageInfo info = null;
        manager = context.getPackageManager();
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
        return info.versionCode;
    }
    /**手机型号*/
    public static String getOSName(){
        return android.os.Build.MODEL;
    }

    /** 获取手机厂商 vendor */
    public static String getPhoneVendor() {
        return android.os.Build.MANUFACTURER;
    }

    /**安卓版本号*/
    public static String getOSVersion(){
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取当前网络类型
     *
     * @return 无线网络返回wifi 手机网络返回mobile 没有网络返回null
     */
    public static String getNetworkType(Context context) {
        String netType = "null";
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            netType = "mobile";
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = "wifi";
        }
        return netType;
    }

    /**获取ip地址*/
    public static String getPhoneIpAddress(Context context) {
        String nType = getNetworkType(context);
        if ("mobile".equals(nType)) {
            return getLocalIpAddress();
        } else if ("wifi".equals(nType)) {
            return getWifiIpAddr(context);
        }
        return null;
    }

    /**
     * 获取WiFi环境下的ip地址
     *
     * @return
     */
    public static String getWifiIpAddr(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }
    /**ip地址解析*/
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }
    /**
     * 获取移动数据下的IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && /* !inetAddress.isLinkLocalAddress() */inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
//			LogUtils.e("WifiPreference IpAddress", ex.t);
        }
        return null;
    }

    /**
     * 获 取 wifi mac
     *
     * @param context
     * @return
     */
    public static String getWlanMAC(Context context) {
        WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_szWLANMAC;
    }
}
