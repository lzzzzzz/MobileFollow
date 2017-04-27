package com.lz.mobilefollow.activity.testdemo;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.lz.mobilefollow.R;
import com.lz.mobilefollow.lzcore.base.BaseActivity;
import com.lz.mobilefollow.view.LZVideoView;

public class MediaControlerActivity extends BaseActivity {

    private LZVideoView lz_video_view;
    private LinearLayout ll_others;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_controler;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        super.initView();
        lz_video_view= (LZVideoView) this.findViewById(R.id.lz_video_view);
        ll_others= (LinearLayout) this.findViewById(R.id.ll_others);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        lz_video_view.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lz_video_view.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        boolean flag=getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if(!flag) {//全屏
            ll_others.setVisibility(View.GONE);
        }else{
            ll_others.setVisibility(View.VISIBLE);
        }
    }
}
