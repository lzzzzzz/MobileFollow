package com.lz.mobilefollow.lzcore.bridge.cache.sharePref;

import android.content.Context;

import com.lz.mobilefollow.lzcore.framework.cache.BaseSharedPreference;


/**
 * <用户信息缓存>
 *
 * @author caoyinfei
 * @version [版本号, 2016/6/6]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class EBSharedPrefUser extends BaseSharedPreference {
    /**
     * 登录名
     */
    public static final String USER_NAME = "user_name";

    public EBSharedPrefUser(Context context, String fileName) {
        super(context,fileName);
    }
}
