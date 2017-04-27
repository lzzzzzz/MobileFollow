package com.lz.mobilefollow.lzcore.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/1/22 0022.
 */
public class TranslateAnimUtils {

    /**
     * 每次移动的距离
     */
    private int bmpW = 0;
    private int currentTabIndex = 0;

    /**
     *
     * @param cxt
     * @param position 分几段移动
     */
    public TranslateAnimUtils(Context cxt, int position) {
        DisplayMetrics mDisplayMetrics = SystemInfo.getDeviceDisplayMetrics(cxt);
        bmpW = mDisplayMetrics.widthPixels / position;
    }

    /**
     * 执行 移动动画
     * @param tvCursor
     * @param arg0
     */
    public void onSelectedListener(TextView tvCursor, int arg0) {
        int one = 0;
        int two = bmpW;
        int three = bmpW * 2;
        int four = bmpW * 3;
        int to = 0;
        int form = 0;
        switch (arg0) {
            case 0:
                if (currentTabIndex == 1) {
                    form = two;
                    to = one;
                } else if (currentTabIndex == 2) {
                    form = three;
                    to = one;
                }else if(currentTabIndex == 3){
                    form = four;
                    to = one;
                }
                break;
            case 1:
                if (currentTabIndex == 0) {
                    form = 0;
                    to = two;
                } else if (currentTabIndex == 2) {
                    form = three;
                    to = two;
                }else if(currentTabIndex == 3){
                    form = four;
                    to = two;
                }
                break;
            case 2:
                if (currentTabIndex == 0) {
                    form = four;
                    to = three;
                } else if (currentTabIndex == 1) {
                    form = two;
                    to = three;
                }else if(currentTabIndex == 3){
                    form = four;
                    to = three;
                }
                break;
            case 3:
                if (currentTabIndex == 0) {
                    form = one;
                    to = four;
                } else if (currentTabIndex == 1) {
                    form = two;
                    to = four;
                }else if(currentTabIndex == 2){
                    form = three;
                    to = four;
                }
                break;
        }
        TranslateAnimation animation = new TranslateAnimation (form, to, 0, 0);
        currentTabIndex = arg0;
        animation.setFillAfter(true);
        animation.setInterpolator(new OvershootInterpolator());//加速器
        animation.setDuration(200);
        tvCursor.startAnimation(animation);

    }

}
