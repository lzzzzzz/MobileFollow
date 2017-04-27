package com.lz.mobilefollow.utils;

import android.content.Context;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lz.mobilefollow.R;
import com.lz.mobilefollow.dao.DeviceEntity;
import com.lz.mobilefollow.dao.DeviceMessage;
import com.lz.mobilefollow.dao.PushMessage;
import com.lz.mobilefollow.dao.ResiveMessage;
import com.lz.mobilefollow.dao.SMSMessage;
import com.lz.mobilefollow.dao.TalkMessage;
import com.lz.mobilefollow.lzcore.constant.Constant;
import com.lz.mobilefollow.lzcore.constant.MessageConfig;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;
import com.lz.mobilefollow.lzcore.framework.manager.ToolKit;
import com.lz.mobilefollow.lzcore.util.DialogUtil;
import com.lz.mobilefollow.lzcore.util.NotificationUtils;
import com.lz.mobilefollow.lzcore.util.SystemInfo;
import com.lz.mobilefollow.lzcore.util.ToastUtil;

import java.util.List;

/**
 * Created by LZ on 2016/10/14.
 */

public class MessageFactory {
    /**接受信息处理类*/
    public static void analyticalMess(Context context,String message){
        LogUtils.d("----------------get message:"+message);
        Gson gson=new Gson();
        ResiveMessage rm=gson.fromJson(message,ResiveMessage.class);
        if(null!=rm){
            switch (rm.getMessageType()){//消息类型
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_ALARM://响铃
                    doWithAlarm(context,rm);
                    break;
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_CLOSE_APP://关闭应用
                    break;
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_UPDATE_APP://更新应用
                    break;
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_NOTIFY_MESSAGE://通知信息
                    doWithNotifyMessage(context,rm);
                    break;
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_TALKMESSAGE://对话信息
                    doWithTalk(context,rm);
                    break;
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_WARNING://报警
                    break;
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_SMS_MESSAGE://发送短信
                    doWithSMSMessage(rm);
                    break;
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_LOCK://锁定手机
                    break;
                case MessageConfig.MESSAGE_RESIVE_TYPE_CODE_UNLOCK://解锁手机
                    break;
                default:
                    break;
            }
        }
    }

    /**提醒消息*/
    private static void doWithAlarm(final Context context, ResiveMessage rm){
        int commendType=rm.getCommandType();//指令类型
        if(commendType==2){//立即响铃
            final String message=rm.getExtraMessage();
            ToolKit.runOnMainThreadAsync(new Runnable(){
                @Override
                public void run() {
                    if(!SystemInfo.isBackground(context)){//前台运行显示dialog
                        DialogUtil.showAlarmDialog(context,message);
                    }
                }
            });
        }
    }
    /**对话消息*/
    private static void doWithTalk(final Context context, ResiveMessage rm){
        String message=rm.getExtraMessage();
        TalkMessage tm=new Gson().fromJson(message,TalkMessage.class);
        if(null==tm){
            return;
        }
        final String messageContent=tm.getMessage();
        final String fromUser=tm.getFromUser();
        if(!TextUtils.isEmpty(messageContent)&&!TextUtils.isEmpty(fromUser)){
            ToolKit.runOnMainThreadAsync(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,fromUser+"给你发消息："+messageContent,Toast.LENGTH_SHORT).show();
                }
            });
//            ToastUtil.makeText(context,fromUser+"给你发消息："+messageContent);
        }
    }

    /**显示通知消息*/
    private static void doWithNotifyMessage( Context context, ResiveMessage rm){
        String message=rm.getExtraMessage();
        NotificationUtils.showNotification(context,"有新消息","通知标题",message,R.mipmap.ic_launcher);
        /*NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.mipmap.ic_launcher, "状态栏通知测试", System.currentTimeMillis());
        manager.notify(11, notification);*/
    }
    /**发送短信消息*/
    private static void doWithSMSMessage(ResiveMessage rm){
        String messageContent=rm.getExtraMessage();
        SMSMessage smsMessage=new Gson().fromJson(messageContent,SMSMessage.class);
        if(null==smsMessage){
            return;
        }
        String smsContent=smsMessage.getMessage();
        String toMobile=smsMessage.getTomobile();
        if(TextUtils.isEmpty(smsContent)||TextUtils.isEmpty(toMobile)){
            return;
        }
        SmsManager smsManager = SmsManager.getDefault();
        //如果汉字大于70个
        if(smsContent.length() > 70){
        //返回多条短信
         List<String> contents = smsManager.divideMessage(smsContent);
         for(String sms:contents){
        //1.目标地址：电话号码 2.原地址：短信中心服号码3.短信内容4.意图
                smsManager.sendTextMessage(toMobile, null, sms, null, null);
            }
        }else{
            smsManager.sendTextMessage(toMobile, null, smsContent, null, null);
        }
    }
    /**发送设备信息消息*/
    public static void sendDeviceMesssage(Context context, String extraMessage){
        PushMessage pushMessage=new PushMessage();
        DeviceMessage dm=new DeviceMessage();
        DeviceEntity device= DeviceUtils.getDeviceInfo(context);
        dm.setDeviceInfo(device);
        dm.setTimeStamp(System.currentTimeMillis());
        pushMessage.setExtraMessage(new Gson().toJson(dm));
        pushMessage.setMessageType(MessageConfig.MESSAGE_SEND_TYPE_DEVICE_INFO);
        WebSocketFactory.getInstance().sendMessage(pushMessage,context);
    }
    /**发送对话消息*/
    public static void sendTalkMesssage(Context context, String toUser,String message){
        if(TextUtils.isEmpty(toUser)||TextUtils.isEmpty(message)){//发送失败
            ToastUtil.makeText(context,context.getResources().getString(R.string.text_toast_str_send_talk_mesage));
            return;
        }
        PushMessage pushMessage=new PushMessage();
        TalkMessage tm=new TalkMessage();
        tm.setFromUser(String.valueOf(Constant.AUTHEN_SECRET_KEY));
        tm.setToUser(toUser);
        tm.setMessage(message);
        pushMessage.setExtraMessage(new Gson().toJson(tm));
        pushMessage.setMessageType(MessageConfig.MESSAGE_SEND_TYPE_TO_TALK);
        WebSocketFactory.getInstance().sendMessage(pushMessage,context);
    }
}
