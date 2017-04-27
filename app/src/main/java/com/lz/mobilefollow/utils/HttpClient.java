package com.lz.mobilefollow.utils;

import android.content.Context;

import com.lz.mobilefollow.lzcore.framework.okhttp3.HttpClicentFactory;
import com.lz.mobilefollow.lzcore.framework.okhttp3.ResultCallback;
import com.lz.mobilefollow.lzcore.util.Tools;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by Administrator on 2015/12/5 0005.
 * <p/>
 * 网络代理
 */
public class HttpClient {


    private final static String TAG = "HttpClient";

    // API域名
//    public static final String API_BASE_DOCTOR = "http://app.1000mob.com";// app域名
    // API域名
    public static final String API_BASE_DOCTOR = "192.168.31.15:16080/sams";

    /**
     * 登陆接口
     */
    private static final String API_LOGIN = API_BASE_DOCTOR+"/appUserLogin";


    private static HttpClient mInstance;

    public static HttpClient getInstance() {
        synchronized (HttpClient.class) {
            if (mInstance == null) {
            }
        }
        return mInstance;
    }

    /**
     * 登陆
     *
     * @param context
     * @param email    邮箱
     * @param password 密码
     */
    public void login2(Context context, String email, String password,ResultCallback callback) {
        TreeMap<String, String> mParam = new TreeMap<String, String>();
        mParam.put("email", email);
        mParam.put("password", Tools.getMD5(password).toLowerCase(Locale.CHINA));
        HttpClicentFactory.getIstance().post(API_LOGIN,mParam,callback);
    }
}
