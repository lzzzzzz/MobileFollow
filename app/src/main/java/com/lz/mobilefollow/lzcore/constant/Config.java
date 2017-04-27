package com.lz.mobilefollow.lzcore.constant;

import android.os.Environment;

/**
 * 全局配置文件
 * public static 全局变量
 *
 * @author lkk
 */
public class Config {
    /**是否使用https true:使用https请求 false:不使用https请求*/
    public final static boolean HTTPS_USEFUL_FLAG = false;
    /**
     * 拍照缓存路径
     */
    public final static String CAMERA_PGOTO_CACHE_PATH = Environment.getExternalStorageDirectory() + "/cache/camera/photopicture.jpg";
    /**
     * SharedPreferences token
     */
    public final static String SHARED_TOKEN = "^cache?token";
    /**
     * 用户id本地缓存 文件名
     */
    public final static String SHARED_DRIVER_ID_FILE_NAME = "usercache";

    /**
     * 设备平台
     */
    public final static String PLATFORM = "android";

}
