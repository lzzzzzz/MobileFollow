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
        lz_video_view.setVideoPath("http://source.wenxiaoyou.com/app/answer/video/truncate/uid//aid-635-2892795c-e8e7-4095-a96a-cb65d6492bda.mp4");
        lz_video_view.setVideoTitle("杨澜的视频");
        lz_video_view.playVideo();
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

    public void changeVideo(View view) {
        //切换视频
        lz_video_view.setVideoPath("http://mpv.videocc.net/86852d458b/6/86852d458b3fe18ba9752a63136d1986_1.mp4?pid=1493793188124X1187567");
        lz_video_view.setVideoTitle("何江的视频");
        lz_video_view.playVideo();
    }

    @Override
    public void onBackPressed() {
        boolean flag=getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if(!flag) {//全屏
            lz_video_view.showOrientation(LinearLayout.VERTICAL);
        }else{
            super.onBackPressed();
        }
    }
}
