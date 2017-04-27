package com.lz.mobilefollow.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.lz.mobilefollow.lzcore.constant.Constant;
import com.lz.mobilefollow.lzcore.util.LocationClient;
import com.lz.mobilefollow.utils.MessageFactory;

/**
 * Created by LZ on 2016/10/13.
 */

public class FollowMoService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationClient lc=new LocationClient();
        lc.getLocationByGps(this);
        lc.getLocationByNet(this);
        MessageFactory.sendDeviceMesssage(FollowMoService.this,Constant.TEST_EXTRA_MESSAGE);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = Constant.COMMIT_TIME_MILLIS;//任务轮询时长
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
//        startForeground();
        return START_STICKY;
    }
}
