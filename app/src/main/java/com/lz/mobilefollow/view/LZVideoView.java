package com.lz.mobilefollow.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.lz.mobilefollow.R;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;
import com.lz.mobilefollow.view.Controller;

import java.io.IOException;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Created by LZ on 2016/12/27.
 * 自定义播放器
 * 注意：全屏状态下不能直接销毁activity,需要先变为竖屏然后销毁
 * 使用时需要对应activity生命周期。
 */

public class LZVideoView extends RelativeLayout {

    private FrameLayout surfacecontainer;
    MediaPlayer mPlayer;
    SurfaceView mSurfaceView;
    private ProgressBar pb_prapare_progress;//加载中
    Controller mController;
    SurfaceHolder.Callback mCallback;
    MediaPlayer.OnPreparedListener mPreparedListener;
    Controller.ControlOper mPlayerControl;
    private long mExitTime = 0;

    private Context context;

    /**初始宽*/
    private int normal_width=0;
    /**初始高*/
    private int normal_height=0;

    private boolean isFirst=true;

    private int marginTop=0;
    private int marginBottom=0;
    private int marginLeft=0;
    private int marginRight=0;

    /**视频地址*/
    private String video_path="";
    /**视频标题*/
    private String video_title="";

    private MediaPlayer.OnCompletionListener onCompletionListener;

    public LZVideoView(Context context) {
        super(context);
        this.context=context;
    }

    public LZVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        createView();
        initView();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(isFirst) {//全屏
            normal_width = this.getWidth();
            normal_height = this.getHeight();
            MarginLayoutParams pm= (MarginLayoutParams) this.getLayoutParams();
            marginTop=pm.topMargin;
            marginBottom=pm.bottomMargin;
            marginLeft=pm.leftMargin;
            marginRight=pm.rightMargin;
            LogUtils.d("---------onLayout()");
            isFirst=false;
        }
    }

    /**创建视图*/
    private void createView(){
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.custome_z_video_view, null);
        addView(myView);
    }
    /**初始化布局*/
    private void initView(){
        initListeners();
        surfacecontainer=(FrameLayout)findViewById(R.id.surfacecontainer);
        pb_prapare_progress= (ProgressBar) findViewById(R.id.pb_prapare_progress);
        pb_prapare_progress.setVisibility(View.VISIBLE);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(mCallback);
        initMediaplayer();
    }
    /**初始化播放器*/
    private void initMediaplayer(){
        if(!isInEditMode()){
            //造成错误的代码段
            try {
                if(null==mPlayer){
                    mPlayer = new MediaPlayer();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                if(null==mController){
                    mController = new Controller(context);
                    mController.setControlStyle(Controller.CONTROL_FLAG_HORIZONTAL);
                }
                mPlayer.stop();
                mPlayer.reset();
                mPlayer.setDataSource(context, Uri.parse(null==video_path?"":video_path));
                mPlayer.setOnPreparedListener(mPreparedListener);
                mPlayer.prepareAsync();

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**释放播放器资源*/
    private void releaseMediaPlayer() {
        mController.onDestroy();
        mController.removeHandlerCallback();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    /**设置播放完毕监听*/
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener){
        this.onCompletionListener=onCompletionListener;
    }
    /**设置视频播放地址*/
    public void setVideoPath(String path){
        this.video_path=path;
    }
    /**设置视频播放标题*/
    public void setVideoTitle(String title){
        this.video_title=title;
    }

    /**开始准备播放视频*/
    public void playVideo(){
        initMediaplayer();
    }
    /**开始准备播放视频*/
    public void playVideo(String path){
        this.video_path=path;
        initMediaplayer();
    }
    /**开始准备播放视频*/
    public void playVideo(String path,String title){
        this.video_path=path;
        this.video_title=title;
        initMediaplayer();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mController.show();
        mController.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**控件销毁方法*/
    public void onDestroy() {
        releaseMediaPlayer();
    }


    /**控件pause状态方法*/
    public void onPause() {
        mPlayer.pause();
    }

    /**初始化监听*/
    private void initListeners() {
        mCallback = new SurfaceHolder.Callback() {

            public void surfaceCreated(SurfaceHolder holder) {
                mPlayer.setDisplay(holder);
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {            }

            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        };

        mPreparedListener = new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                pb_prapare_progress.setVisibility(View.GONE);
                mController.setMediaPlayer(mPlayerControl);
                mController.setAnchorView(surfacecontainer);
                mController.setMediaTitle(null==video_title?"":video_title);
                mPlayer.start();
                setSurfaceParam();
                if(null!=onCompletionListener){
                    mPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        };

        mPlayerControl = new Controller.ControlOper() {

            public void start() {
                mPlayer.start();
            }

            public void pause() {
                mPlayer.pause();
            }

            public int getDuration() {
                return mPlayer.getDuration();
            }

            public int getCurPosition() {
                return mPlayer.getCurrentPosition();
            }

            public void seekTo(int pos) {
                mPlayer.seekTo(pos);
            }

            public boolean isPlaying() {
                return mPlayer.isPlaying();
            }

            public int getBufPercent() {
                return 0;
            }

            public boolean canPause() {
                return true;
            }

            public boolean canSeekBackward() {
                return true;
            }

            public boolean canSeekForward() {
                return true;
            }

            public boolean isFullScreen() {
                return true;
            }

            public void fullScreen() {
                boolean flag=context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                if(flag){
                    showOrientation(HORIZONTAL);
                }else{//竖屏
                    showOrientation(VERTICAL);
                }
            }

            @Override
            public void onClickBack() {
                boolean flag=context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                if(!flag){
                    showOrientation(VERTICAL);
                }
            }
        };
    }

    /**设置横屏或竖屏
     * @param flag：VERTICAL（1）竖屏 HORIZONTAL（0）横屏
     * */
    public void showOrientation(int flag){
        if(flag==VERTICAL){//竖屏
            showBar();
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //竖屏
            setSurfaceParam();
        }else if(flag==HORIZONTAL){
            hideBar();
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
            setSurfaceParam();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSurfaceParam();
            }
        }, 500);
    }
    /**重新计算surfaceview大小*/
    private void setSurfaceParam() {
        if(null!=mSurfaceView){
            int n_height=0;
            int n_width=0;
            boolean flag=context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
            if(!flag){//全屏
                Point size = new Point();
                ((Activity)context).getWindowManager().getDefaultDisplay().getSize(size);
                n_width=size.x;
                n_height=size.y;
                MarginLayoutParams lap= (MarginLayoutParams) this.getLayoutParams();
                lap.setMargins(0,0,0,0);
                this.setLayoutParams(lap);
            }else{
                n_width=normal_width;
                n_height=normal_height;
                MarginLayoutParams lap= (MarginLayoutParams) this.getLayoutParams();
                lap.setMargins(marginLeft,marginTop,marginRight,marginBottom);
                this.setLayoutParams(lap);
            }
            ViewGroup.LayoutParams param=surfacecontainer.getLayoutParams();
            param.width=n_width;
            param.height=n_height;
            surfacecontainer.setLayoutParams(param);
            ViewGroup.LayoutParams f_pm= this.getLayoutParams();
            f_pm.width=n_width;
            f_pm.height=n_height;
            this.setLayoutParams(f_pm);

            //视频缩放
            int v_width=mPlayer.getVideoWidth();
            int v_height=mPlayer.getVideoHeight();
            float wRatio = (float) v_width / (float) n_width;
            float hRatio = (float) v_height / (float) n_height;
            // 选择大的一个进行缩放
            float ratio = Math.max(wRatio, hRatio);
            int new_width = (int) Math.ceil((float) v_width / ratio);
            int new_height = (int) Math.ceil((float) v_height / ratio);
            LogUtils.d("---------new_width-new_height:"+new_width+"-"+new_height);
            ViewGroup.LayoutParams pm=mSurfaceView.getLayoutParams();
            pm.width=new_width;
            pm.height=new_height;
            mSurfaceView.setLayoutParams(pm);

            printWidthH();
        }
    }
    private void hideBar(){
        ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    private void showBar(){
        WindowManager.LayoutParams attrs = ((Activity)context).getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity)context).getWindow().setAttributes(attrs);
    }

    private void printWidthH(){
        ViewGroup.LayoutParams param=surfacecontainer.getLayoutParams();
        LogUtils.d("comtanier width-heightz;"+param.width+"-"+param.height);
        ViewGroup.LayoutParams param2=mSurfaceView.getLayoutParams();
        LogUtils.d("mSurfaceView width-heightz;"+param2.width+"-"+param2.height);
    }

}
