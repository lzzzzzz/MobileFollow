package com.lz.mobilefollow.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.TextUtils;

import com.lz.mobilefollow.lzcore.framework.log.LogUtils;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by LZ on 2016/10/31.
 */

public class CalendarUtils {

    /**
     * 添加日历事件
     *
     * @param startMillis 事件开始的时间
     * @param endMillis   事件结束的时间
     * @param mimus       提前通知的分钟数
     * @param evTitle     事件的标题
     * @param evContent   事件的具体内容
     * @param evLocation  事件的地点
     * @return 添加到日程是否成功
     */
    public static String addEventToSysterms(Context context,long startMillis, long endMillis, int mimus, String evTitle, String evContent, String evLocation) {
        String myEventsId;
        Context ctx = context;
        evTitle = TextUtils.isEmpty(evTitle) ? "" : evTitle;
        evContent = TextUtils.isEmpty(evContent) ? "" : evContent;
        evLocation = TextUtils.isEmpty(evLocation) ? "" : evLocation;
        long nowTime = new Date().getTime();
        if (startMillis < nowTime || endMillis < nowTime || (nowTime + mimus * 60 * 1000) > startMillis) {
            return null;
        }

        String calanderURL = "";
        String calanderEventURL = "";
        String calanderRemiderURL = "";
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            calanderURL = "content://com.android.calendar/calendars";
            calanderEventURL = "content://com.android.calendar/events";
            calanderRemiderURL = "content://com.android.calendar/reminders";

        } else {
            calanderURL = "content://calendar/calendars";
            calanderEventURL = "content://calendar/events";
            calanderRemiderURL = "content://calendar/reminders";
        }
        try {

            // 获取要出入的gmail账户的id
            String calID = "";
            Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null,
                    null, null, null);
            if (userCursor.getCount() > 0) {
                userCursor.moveToFirst();
                calID = userCursor.getString(userCursor.getColumnIndex("_id"));
            }

            ContentValues eValues = new ContentValues();  //插入事件
            TimeZone tz = TimeZone.getDefault();//获取默认时区

            //插入日程
            eValues.put(CalendarContract.Events.DTSTART, startMillis);
            eValues.put(CalendarContract.Events.DTEND, endMillis);
            eValues.put(CalendarContract.Events.TITLE, evTitle);
            eValues.put(CalendarContract.Events.HAS_ALARM, 1);
            eValues.put(CalendarContract.Events.DESCRIPTION, evContent);
            eValues.put(CalendarContract.Events.CALENDAR_ID, calID);
            LogUtils.d("----------------calID:" + calID);
            eValues.put(CalendarContract.Events.EVENT_LOCATION, evLocation);
            eValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID().toString());
            Uri uri = ctx.getContentResolver().insert(Uri.parse(calanderEventURL), eValues);

            ContentValues rValues = new ContentValues();  //插入提醒，与事件配合起来才有效
            //插完日程之后必须再插入以下代码段才能实现提醒功能
            myEventsId = uri.getLastPathSegment(); // 得到当前表的_id
            rValues.put("event_id", myEventsId);
            rValues.put(CalendarContract.Reminders.EVENT_ID, myEventsId);
            rValues.put(CalendarContract.Reminders.MINUTES, mimus);    //提前10分钟提醒
            rValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);    //如果需要有提醒,必须要有这一行
            ctx.getContentResolver().insert(Uri.parse(calanderRemiderURL), rValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return myEventsId;
    }

    /**
     * 删除日程
     */
    public static int deleteEventFromSystem(Context context,String id) {
        LogUtils.d("------------------remove eventId:" + id);
        if (TextUtils.isEmpty(id)) {
            return 0;
        }
        long eventid = 0;
        try {
            eventid = Long.valueOf(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventid);
        int rows = context.getContentResolver().delete(deleteUri, null, null);
        return rows;
    }
}
