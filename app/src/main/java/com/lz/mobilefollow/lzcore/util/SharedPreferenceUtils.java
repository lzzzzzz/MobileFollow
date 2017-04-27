package com.lz.mobilefollow.lzcore.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.lz.mobilefollow.lzcore.constant.MessageConfig;

/**
 * Created by LZ on 2016/10/31.
 */

public class SharedPreferenceUtils {

    /**
     * 共享存储，存储boolean类型
     *
     * @param context
     * @param key
     * @param value
     *            boolean
     */
    public static void setSPBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MessageConfig.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 共享存储，存储String类型
     *
     * @param context
     * @param key
     * @param value
     *            String
     */
    public static void setSPString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MessageConfig.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 共享存储，存储int类型
     *
     * @param context
     * @param key
     * @param value
     *            int
     */
    public static void setSPInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MessageConfig.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 共享存储，存储long类型
     *
     * @param context
     * @param key
     * @param value
     *            long
     */
    public static void setSPLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(
                MessageConfig.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 共享存储，读取指定key的boolean值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getSPBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                MessageConfig.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(key, false);
        return value;
    }

    /**
     * 共享存储，读取指定key的String值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getSPString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                MessageConfig.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    /**
     * 共享存储，读取指定key的int值
     *
     * @param context
     * @param key
     * @return
     */
    public static int getSPInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                MessageConfig.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    /**
     * 共享存储，读取指定key的long值
     *
     * @param context
     * @param key
     * @return
     */
    public static long getSPLong(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                MessageConfig.SHAREDPREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getLong(MessageConfig.SHAREDPREFERENCE_NAME, 0);
    }
}
