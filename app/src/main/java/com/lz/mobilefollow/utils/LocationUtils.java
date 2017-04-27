package com.lz.mobilefollow.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.lz.mobilefollow.lzcore.constant.MessageConfig;
import com.lz.mobilefollow.lzcore.util.SharedPreferenceUtils;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by LZ on 2017/3/23.
 */

public class LocationUtils {

    private static LocationUtils instance;
    private LocationManager locationManager;// 位置服务
    private Location location;// 位置

    private Context context;

    public static LocationUtils getInstance(){
        if(null==instance){
            instance=new LocationUtils();
        }
        return instance;
    }
    //这个就是地理位置初始化，主要通过调用其他方法获取经纬度，并设置到location
    public void locationInit(Context context) {
        this.context=context;
        try {
            // 获取系统服务
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            // 判断GPS是否可以获取地理位置，如果可以，展示位置，
            // 如果GPS不行，再判断network，还是获取不到，那就报错
            if (locationInitByGPS() || locationInitByNETWORK()) {
                // 上面两个只是获取经纬度的，获取经纬度location后，再去调用谷歌解析来获取地理位置名称
                //showLocation(location);
            }
        } catch (Exception e) {
        }
    }

    // GPS去获取location经纬度
    public boolean locationInitByGPS() {
        // 没有GPS，直接返回
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 0, locationListener);
        location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LONGITUDE, String.valueOf(location.getLongitude()));
            SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LATITUDE, String.valueOf(location.getLatitude()));
            return true;//设置location成功，返回true
        } else {
            return false;
        }
    }
    // network去获取location经纬度
    public boolean locationInitByNETWORK() {
        // 没有NETWORK，直接返回
        if (!locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        location = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LONGITUDE, String.valueOf(location.getLongitude()));
            SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LATITUDE, String.valueOf(location.getLatitude()));
            return true;
        } else {
            return false;
        }
    }
    //监听地理位置变化，地理位置变化时，能够重置location
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
           // tv_show.setText("更新失败失败");
        }

        @Override
        public void onLocationChanged(Location loc) {
            if (loc != null) {
                location = loc;
                //showLocation(location);
                SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LONGITUDE, String.valueOf(location.getLongitude()));
                SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LATITUDE, String.valueOf(location.getLatitude()));
            }
        }
    };
}
