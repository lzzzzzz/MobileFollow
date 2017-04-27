package com.lz.mobilefollow.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.lz.mobilefollow.dao.DraftInfo;
import com.lz.mobilefollow.dao.PushMessage;
import com.lz.mobilefollow.lzcore.constant.Constant;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;
import com.lz.mobilefollow.lzcore.util.SharedPreferenceUtils;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by LZ on 2016/10/13.
 */

public class WebSocketFactory {
    private static boolean ifConnect=false;
    private static WebSocketFactory instance;
    private WebSocketClient socketClient;
    private DraftInfo selectDraft=new DraftInfo("WebSocket协议Draft_17", new Draft_17());// 连接协议
    /*DraftInfo[] draftInfos = {, new DraftInfo
      ("WebSocket协议Draft_10", new Draft_10()), new DraftInfo("WebSocket协议Draft_76", new Draft_76()), new
      DraftInfo("WebSocket协议Draft_75", new Draft_75())};// 所有连接协议*/

    public static WebSocketFactory getInstance(){
        if(null==instance){
            instance=new WebSocketFactory();
        }
        return instance;
    }

    /**设置是否允许连接*/
    public void setIfConnect(boolean ifConnect){
        this.ifConnect=ifConnect;
    }
    /**开始连接*/
    public boolean beginConnect(final Context context){
        if(!ifConnect&&Constant.AUTHEN_SECRET_KEY<=0){
            return false;
        }
        boolean isConnect= SharedPreferenceUtils.getSPBoolean(context,Constant.SHARED_KEY_IS_CONNECT);
        if(null!=socketClient&& isConnect){
            return true;
        }
        try {
            socketClient = new WebSocketClient(new URI(Constant.WEB_SOCKET_CONNECT_URL), selectDraft.getDraft()) {
                @Override
                public void onOpen(final ServerHandshake serverHandshakeData) {
                    SharedPreferenceUtils.setSPBoolean(context, Constant.SHARED_KEY_IS_CONNECT,true);
//                    BaseApplication.isConnect=true;
                     LogUtils.d("已经连接到服务器【" + getURI() + "】\n");
                }

                @Override
                public void onMessage(final String message) {
                    LogUtils.d("获取到服务器信息【" + message + "】");
                    MessageFactory.analyticalMess(context,message);
                }

                @Override
                public void onClose(final int code, final String reason, final boolean remote) {
//                    BaseApplication.isConnect=false;
                    SharedPreferenceUtils.setSPBoolean(context,Constant.SHARED_KEY_IS_CONNECT,false);
                   LogUtils.e("断开服务器连接【" + getURI() + "，状态码： " + code + "，断开原因：" + reason + "】");
                }

                @Override
                public void onError(final Exception e) {
//                    BaseApplication.isConnect=false;
                    SharedPreferenceUtils.setSPBoolean(context,Constant.SHARED_KEY_IS_CONNECT,false);
                    LogUtils.e("连接发生了异常【异常原因：" + e + "】");
                 }
            };
            socketClient.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**关闭连接*/
    public void closeClient(Context context){
        ifConnect=false;
        boolean isConnect= (boolean) SharedPreferenceUtils.getSPBoolean(context,Constant.SHARED_KEY_IS_CONNECT);
        if(null!=socketClient&&isConnect){
            socketClient.close();
        }
    }

    /**发送消息*/
    public void sendMessage(PushMessage message,Context context){
        if(!ifConnect){
            return;
        }
        boolean isConnect= (boolean) SharedPreferenceUtils.getSPBoolean(context,Constant.SHARED_KEY_IS_CONNECT);
        if(null==socketClient||!isConnect){
            if(beginConnect(context)){
                sendPushMessage(message,context);
            }
        }else{
            sendPushMessage(message,context);
        }
    }

    /**发送消息*/
    private void sendPushMessage(PushMessage pushMessage,Context context){
        boolean isConnect= (boolean) SharedPreferenceUtils.getSPBoolean(context,Constant.SHARED_KEY_IS_CONNECT);
        if(null!=socketClient&&isConnect){
            Gson gson=new Gson();
            String message= gson.toJson(pushMessage,PushMessage.class);
            this.socketClient.send(message);
        }
    }
}
