package com.lz.mobilefollow.lzcore.constant;

/**
 * Created by LZ on 2016/10/13.
 */

public class Constant {

    public final static String PLATFORM="ANDROID";
    public final static boolean DEBUG=true;//调试模式，上线关闭

    public static int AUTHEN_SECRET_KEY=0;

    private final static String WEB_SERVER_HOST="HTTP://192.168.31.15:16080";
    public final static String WEB_SOCKET_CONNECT_URL=WEB_SERVER_HOST+"/sams/app/talkjs/echo";

    public final static int COMMIT_TIME_MILLIS=60*1000;//消息提交时间为1分钟/次

    public final static String TAG_NAME="MOBILE_FOLLOWER";

    public static String TEST_EXTRA_MESSAGE="MOBILE_FOLLOW";


    public static String SHARED_KEY_IS_CONNECT="isConnect";//是否已经连线
    public static String SHARED_KEY_IF_CONNECT="ifConnect";//是否允许连接

    public static String SHARED_KEY_IF_LOGIN="ifLogin";//是否允许连接


}
