package com.lz.mobilefollow.lzcore.framework.log;

import android.util.Log;

/**
 * 打印信息功能封装
 * 可关闭所有打印
 *
 * @author lkk
 */
public class LogUtils {

    /**
     * true 允许输出打印信息， false  关闭所有打印信息
     */
    private static boolean isLog = true;

    private static String TAG = "Logger";

    public static int v(String tag, String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.v(tag, msg);
    }

    public static int v(String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.v(TAG, msg);
    }

    public static int d(String tag, String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.d(tag, msg);
    }

    public static int d(String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.d(TAG, msg);
    }

    public static int i(String tag, String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.i(tag, msg);
    }

    public static int i(String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.i(TAG, msg);
    }

    public static int w(String tag, String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.w(tag, msg);
    }

    public static int w(String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.w(TAG, msg);
    }

    public static int e(String tag, String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.e(tag, msg);
    }

    public static int e(String msg) {
        // TODO Auto-generated method stub
        if (!isLog) return 0;
        return Log.e(TAG, msg);
    }

}
