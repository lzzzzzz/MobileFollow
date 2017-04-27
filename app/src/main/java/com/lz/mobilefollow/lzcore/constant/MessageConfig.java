package com.lz.mobilefollow.lzcore.constant;

/**
 * <全局常量>
 *
 * @author lz
 * @version [版本号, 2016/10/26]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MessageConfig {

    /**
     * 返回请求成功码
     */
    public static final String SUCESS_CODE_END = "000000";

    /**
     * 返回请求失败码
     */
    public static final String FAILED_CODE_END = "000001";

    /**
     * 请求失败展示信息
     */
    public static final String ERROR_MESSAGE = "请求失败，请稍后再试";


    public static final String SHAREDPREFERENCE_NAME ="MOBILE_FOLLOW_SP";

    public static final String SP_LOCATION_LONGITUDE="longitude";//经度保存值名称

    public static final String SP_LOCATION_LATITUDE="latitude";//纬度保存值名称


    /**接受信息类型*/
    public static final int MESSAGE_RESIVE_TYPE_CODE_ALARM=111;//响铃信息
    public static final int MESSAGE_RESIVE_TYPE_CODE_CLOSE_APP=112;//关闭应用（关闭后必须手动开启）
    public static final int MESSAGE_RESIVE_TYPE_CODE_UPDATE_APP=113;//更新应用
    public static final int MESSAGE_RESIVE_TYPE_CODE_NOTIFY_MESSAGE=114;//通知显示显示信息
    public static final int MESSAGE_RESIVE_TYPE_CODE_TALKMESSAGE=119;//通知显示显示信息
    public static final int MESSAGE_RESIVE_TYPE_CODE_WARNING=115;//报警信息
    public static final int MESSAGE_RESIVE_TYPE_CODE_SMS_MESSAGE=116;//发送短信信息
    public static final int MESSAGE_RESIVE_TYPE_CODE_LOCK=117;//锁定手机
    public static final int MESSAGE_RESIVE_TYPE_CODE_UNLOCK=118;//解锁手机

    /**发送信息类型*/
    public static final int MESSAGE_SEND_TYPE_DEVICE_INFO=330;//发送设备信息
    public static final int MESSAGE_SEND_TYPE_TO_TALK=331;//对话信息



    /**成功返回码*/
    public final static int RESPONSE_SUCCESS=0x01;

    /**失败返回码*/
    public final static int RESPONSE_FAILED=0x02;

    /**出错返回码*/
    public final static int RESPONSE_ERROR=0x03;

    public final static String RESPONSE_SUCCESS_MESSAGE="成功";

    public final static String RESPONSE_FAILED_MESSAGE="失败";

    public final static String RESPONSE_ERROR_MESSAGE="出错";
}
