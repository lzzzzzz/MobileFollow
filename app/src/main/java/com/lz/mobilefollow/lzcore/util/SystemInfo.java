package com.lz.mobilefollow.lzcore.util;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.lz.mobilefollow.lzcore.framework.log.LogUtils;

public class SystemInfo {

    private final static String TAG = "SystemInfo";

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    @SuppressLint("InlinedApi")
    public static void debugDensity(Context context) {
        final DisplayMetrics dm = getDeviceDisplayMetrics(context);
        LogUtils.d(TAG, "dm.density: " + dm.density);
        LogUtils.d(TAG, "dm.densityDpi: " + dm.densityDpi);
        LogUtils.d(TAG, "dm.heightPixels: " + dm.heightPixels);
        LogUtils.d(TAG, "dm.scaledDensity: " + dm.scaledDensity);
        LogUtils.d(TAG, "dm.widthPixels: " + dm.widthPixels);
        LogUtils.d(TAG, "dm.xdpi: " + dm.xdpi);
        LogUtils.d(TAG, "dm.ydpi: " + dm.ydpi);
        LogUtils.d(TAG, "dm.DENSITY_DEFAULT: " + DisplayMetrics.DENSITY_DEFAULT);
        LogUtils.d(TAG, "dm.DENSITY_HIGH: " + DisplayMetrics.DENSITY_HIGH);
        LogUtils.d(TAG, "dm.DENSITY_LOW: " + DisplayMetrics.DENSITY_LOW);
        LogUtils.d(TAG, "dm.DENSITY_MEDIUM: " + DisplayMetrics.DENSITY_MEDIUM);
        LogUtils.d(TAG, "dm.DENSITY_TV: " + DisplayMetrics.DENSITY_TV);
        LogUtils.d(TAG, "dm.DENSITY_XHIGH: " + DisplayMetrics.DENSITY_XHIGH);
    }

    public static DisplayMetrics getDeviceDisplayMetrics(Context context) {
        android.view.WindowManager windowsManager = (android.view.WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        android.view.Display display = windowsManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        return outMetrics;
    }

    /**
     * 检查是否与网络可用
     *
     * @return
     */

    public boolean isNetworkAvailable(Context activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 判断应用程序是否后台运行
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
