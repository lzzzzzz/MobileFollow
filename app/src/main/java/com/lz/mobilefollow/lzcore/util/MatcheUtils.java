package com.lz.mobilefollow.lzcore.util;

import android.graphics.MaskFilter;
import android.text.TextUtils;

public class MatcheUtils {

    /**
     * 匹配手机号 进行验证
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        String telRegex = "[1]\\d{10}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static boolean isPhoneCode(String mobiles) {
        String telRegex = "\\d{6}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

	/**
	 * 匹配字符转 ","切割
	 * 
	 * @param str
	 * @param str
	 */
	public static String[] splitShow(String str) {
		if (str != null) {
			return str.split("\\,");
		} else {
			return new String[0];
		}
	}

    /**
     * 手机号中间四位密文显示
     *
     * @param tel
     * @return
     */
    public static String getPassTel(String tel) {
        String phone = tel;
        if (tel != null && tel.length() == 11) {
            phone = tel.substring(0, 3);
            int length = tel.length();
            phone = phone + "****" + tel.substring(length - 4, length);
        }
        return phone;
    }

	/**
	 * 匹配 ‘-’
	 * @param time
	 * @return
	 */
	public static String[] splitTime(String time){
		if (time != null) {
			return time.split("-");
		} else {
			return new String[0];
		}
	}

    /**邮箱正则判断*/
    public static boolean isEmail(String str){
        if(null==str||"".equals(str)){
            return false;
        }else{
            String emailMachStr="^[A-Za-zd]+([-_.][A-Za-zd]+)*@([A-Za-zd]+[-.])+[A-Za-zd]{2,5}$ ";
            return str.matches(emailMachStr);
        }
    }
}
