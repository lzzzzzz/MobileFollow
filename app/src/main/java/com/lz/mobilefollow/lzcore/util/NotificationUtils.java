package com.lz.mobilefollow.lzcore.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.lz.mobilefollow.R;
import com.lz.mobilefollow.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by LZ on 2016/11/1.
 */

public class NotificationUtils {

    public static void showNotification(Context context, String tickerText, String contentTitle,
                                 String contentText, int iconId) {
        NotificationManager  notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // 创建一个Notification
        Notification notification = new Notification();
        // 设置通知 消息 图标
        notification.icon = iconId;
        // 设置发出消息的内容
        notification.tickerText = tickerText;
        // 设置发出通知的时间
        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_AUTO_CANCEL;// 不能够自动清除
        // 设置显示通知时的默认的发声、振动、Light效果
        notification.defaults = Notification.DEFAULT_VIBRATE;// 振动

        // Notification notification = new Notification(R.drawable.ic_launcher,"有新的消息", System.currentTimeMillis());

        // 3步：PendingIntent android系统负责维护
        Intent intent=new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent, 0);
        // 4步：设置更加详细的信息
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.notifycation_view);
        rv.setTextViewText(R.id.tv_show_notify_title, contentTitle);
        rv.setTextViewText(R.id.tv_show_notify_message, contentText);
        notification.contentView = rv;
        notification.contentIntent =pendingIntent;
        // 5步：使用notificationManager对象的notify方法 显示Notification消息 需要制定
        // Notification的标识
        notificationManager.notify(1, notification);

    }
}
