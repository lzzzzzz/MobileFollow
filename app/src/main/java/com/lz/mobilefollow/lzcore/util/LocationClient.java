package com.lz.mobilefollow.lzcore.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.lz.mobilefollow.lzcore.constant.MessageConfig;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;

import java.util.Iterator;

/**
 * Created by LZ on 2016/10/31.
 */

public class LocationClient {

    public void getLocationByNet(final Context context) {
        try{
            LocationManager locationManager = null;
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    LogUtils.d("---------------longitude&latitude:"+longitude+"&"+latitude);
                    if(longitude<=0&&latitude<=0){
                        return;
                    }
                    SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LONGITUDE, String.valueOf(longitude));
                    SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LATITUDE, String.valueOf(latitude));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getLocationByGps(final Context context) {
        try{
            final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {

                /**
                 * 位置信息变化时触发
                 */
                public void onLocationChanged(Location location) {
                    LogUtils.i("时间：" + location.getTime());
                    LogUtils.i("经度：" + location.getLongitude());
                    LogUtils.i("纬度：" + location.getLatitude());
                    LogUtils.i("海拔：" + location.getAltitude());
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    LogUtils.d("---------------longitude&latitude:"+longitude+"&"+latitude);
                    if(longitude<=0&&latitude<=0){
                        return;
                    }
                    SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LONGITUDE, String.valueOf(longitude));
                    SharedPreferenceUtils.setSPString(context, MessageConfig.SP_LOCATION_LATITUDE, String.valueOf(latitude));
                }

                /**
                 * GPS状态变化时触发
                 */
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    switch (status) {
                        //GPS状态为可见时
                        case LocationProvider.AVAILABLE:
                            LogUtils.i("当前GPS状态为可见状态");
                            break;
                        //GPS状态为服务区外时
                        case LocationProvider.OUT_OF_SERVICE:
                            LogUtils.i("当前GPS状态为服务区外状态");
                            break;
                        //GPS状态为暂停服务时
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            LogUtils.i("当前GPS状态为暂停服务状态");
                            break;
                    }
                }

                /**
                 * GPS开启时触发
                 */
                public void onProviderEnabled(String provider) {
                }

                /**
                 * GPS禁用时触发
                 */
                public void onProviderDisabled(String provider) {
                }
            };

            //状态监听
            GpsStatus.Listener listener = new GpsStatus.Listener() {
                public void onGpsStatusChanged(int event) {
                    switch (event) {
                        //第一次定位
                        case GpsStatus.GPS_EVENT_FIRST_FIX:
                            LogUtils.i("第一次定位");
                            break;
                        //卫星状态改变
                        case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                            LogUtils.i("卫星状态改变");
                            //获取当前状态
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                            //获取卫星颗数的默认最大值
                            int maxSatellites = gpsStatus.getMaxSatellites();
                            //创建一个迭代器保存所有卫星
                            Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                            int count = 0;
                            while (iters.hasNext() && count <= maxSatellites) {
                                GpsSatellite s = iters.next();
                                count++;
                            }
                            System.out.println("搜索到："+count+"颗卫星");
                            break;
                        //定位启动
                        case GpsStatus.GPS_EVENT_STARTED:
                            LogUtils.i( "定位启动");
                            break;
                        //定位结束
                        case GpsStatus.GPS_EVENT_STOPPED:
                            LogUtils.i( "定位结束");
                            break;
                    }
                };
            };
            //判断GPS是否正常启动
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LogUtils.e("--->gps关闭 无法获取位置信息");
                return;
            }
            //为获取地理位置信息时设置查询条件
            String bestProvider = locationManager.getBestProvider(getCriteria(), true);
            //获取位置信息
            //如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            //监听状态
            locationManager.addGpsStatusListener(listener);
            //绑定监听，有4个参数
            //参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
            //参数2，位置信息更新周期，单位毫秒
            //参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
            //参数4，监听
            //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

            // 1秒更新一次，或最小位移变化超过1米更新一次；
            //注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            //位置监听
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 返回查询条件
     * @return
     */
    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }


}
