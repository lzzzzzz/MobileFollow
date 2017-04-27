package com.lz.mobilefollow.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.lz.mobilefollow.activity.MainActivity;

/**
 * Created by LZ on 2016/10/14.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, FollowMoService.class);
        context.startService(i);
    }
}
