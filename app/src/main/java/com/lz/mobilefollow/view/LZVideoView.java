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

import java.io.IOException;

/**
 * Created by LZ on 2016/12/27.
 * 自定义播放器
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

    private void createView(){
        LayoutInflater mInflater = LayoutInflater.from(context);
        View myView = mInflater.inflate(R.layout.custome_z_video_view, null);
        addView(myView);
    }

    private void initView(){
        initListeners();
        surfacecontainer=(FrameLayout)findViewById(R.id.surfacecontainer);
        pb_prapare_progress= (ProgressBar) findViewById(R.id.pb_prapare_progress);
        pb_prapare_progress.setVisibility(View.VISIBLE);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(mCallback);
        if(!isInEditMode()){
            //造成错误的代码段
            mPlayer = new MediaPlayer();
            mController = new Controller(context);
            mController.setControlStyle(Controller.CONTROL_FLAG_HORIZONTAL);
            try {

                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                //http://source.wenxiaoyou.com/app/answer/video/truncate/uid//aid-635-2892795c-e8e7-4095-a96a-cb65d6492bda.mp4
                //http://source.wenxiaoyou.com/app/answer/video/truncate/uid//aid-631-9ccff71c-ab6c-42db-b9f9-71ce12ac7543.mp4
                mPlayer.setDataSource(context, Uri.parse("http://source.wenxiaoyou.com/app/answer/video/truncate/uid//aid-635-2892795c-e8e7-4095-a96a-cb65d6492bda.mp4"));
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
                mController.setMediaTitle("测试视频");
                mPlayer.start();
                setSurfaceParam();
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
                    hideBar();
                    ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
                    setSurfaceParam();
                }else{
                    showBar();
                    ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //竖屏
                    setSurfaceParam();
                }
            }

            @Override
            public void onClickBack() {
                boolean flag=context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                if(!flag){
                    showBar();
                    ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //竖屏
                    setSurfaceParam();
                }
            }
        };
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
