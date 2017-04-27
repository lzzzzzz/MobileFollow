package com.lz.mobilefollow.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lz.mobilefollow.lzcore.util.LocationClient;
import com.lz.mobilefollow.utils.LocationUtils;

/**
 * Created by LZ on 2016/10/31.
 */

public class AddressService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        new Thread(new MyThread()).start();
       /* LocationClient lc=new LocationClient();
        lc.getLocationByNet(AddressService.this);
        lc.getLocationByGps(AddressService.this);*/
        LocationUtils.getInstance().locationInit(this);
        return super.onStartCommand(intent, flags, startId);
    }

    /**开启线程监听位置变化*/
   /* public class MyThread implements Runnable {
        @Override
        public void run() {
            try{


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };*/
}
