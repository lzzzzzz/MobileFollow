package com.lz.mobilefollow.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lz.mobilefollow.R;
import com.lz.mobilefollow.lzcore.framework.log.LogUtils;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.Locale;

public class Controller extends FrameLayout {
    /**竖向布局*/
    public static final int CONTROL_FLAG_VERTICAL=0X01;
    /**横向布局*/
    public static final int CONTROL_FLAG_HORIZONTAL=0X02;

    private int control_flag=CONTROL_FLAG_HORIZONTAL;

    private static final String LOG_TAG = Controller.class.getName();
      
    private static final int    FADE_OUT = 1;  
    private static final int    DEFTIMEOUT = 3000;  
    private static final int    SHOW_PROGRESS = 2;  
      
    private ImageButton         mBtnPause;  
    private ImageButton         mBtnFfwd;  
    private ImageButton         mBtnRew;  
    private ImageButton mBtnNext;
    private ImageButton         mBtnPrev;  
    private ImageButton         mBtnFullscreen;  
    private Handler mHandler = new MsgHandler(this);
      
    private ControlOper             mPlayerCtrl;  
    private Context mContext;
    private ViewGroup mAnchorVGroup;
    private View mRootView;
    private ProgressBar mProgress;
    private TextView mEndTime, mCurTime;
    private boolean             mIsShowing;  
    private boolean             mIsDragging;      
    private boolean             mUseFastForward;  
    private boolean             mFromXml;  
    private boolean             mIsListenersSet;  
    private View.OnClickListener mNextListener, mPrevListener;  
    StringBuilder               mStrBuilder;  
    Formatter mFormatter;

    private LinearLayout ll_ver_top_content;//竖向布局顶部
    private LinearLayout ll_ver_content;//竖向布局下部
    private LinearLayout ll_hor_content;//横向布局
    private ImageButton pause2;//暂停
    private TextView time_current2;//播放进度
    private AppCompatSeekBar mediacontroller_progress2;//进度条
    private TextView time2;//资源长度
    private ImageButton fullscreen2;//全屏

    private TextView tv_show_power;//电量显示
    private TextView tv_show_time;//时间显示

    private BatteryReceiver receiver=null;//电量广播接收
    private TimeReceiver mTimeReceiver = null;//时间广播接收
    private ImageView iv_back_arrow;// 返回箭头
    private TextView tv_media_title;//视频标题

    private ImageView iv_changge_media_img;//光照和声音图标显示
    private TextView tv_show_change_info;//显示变化


    private String titleStr;//标题

    /** 最大声音 */
    private int mMaxVolume;
    /** 当前声音 */
    private int mVolume = -1;
    /**当前亮度*/
    private float mBrightness = -1f;

    private GestureDetector mGestureDetector;//手势监听类

    private  AudioManager mAudioManager;

    public Controller(Context context) {  
        this(context, true);  
    }  
  
    public Controller(Context context, AttributeSet attrs) {
        super(context, attrs);  
          
        mRootView = null;  
        mContext = context;  
        mUseFastForward = true;  
        mFromXml = true;  
    }  
  
    public Controller(Context context, boolean useFastForward) {  
        super(context);  
        mContext = context;  
        mUseFastForward = useFastForward;  
    }  

    /**设置显示风格*/
    public void setControlStyle(int style){
        if(style==CONTROL_FLAG_VERTICAL){
            control_flag=CONTROL_FLAG_VERTICAL;
        }else{
            control_flag=CONTROL_FLAG_HORIZONTAL;
        }
    }
    public void removeHandlerCallback() {  
        if(mHandler != null) {  
            mHandler.removeCallbacksAndMessages(null);  
            mHandler = null;  
        }  
    }  
  
    @Override  
    public void onFinishInflate() {  
        if (mRootView != null) {  
            initCtrlView(mRootView);  
        }  
    }  
      
    public void setMediaPlayer(ControlOper player) {  
        mPlayerCtrl = player;  
        updatePausePlay();  
        updateFullScreen();  
    }  

    /**设置显示标题*/
    public void setMediaTitle(String title){
        this.titleStr=title;
        if(null!=tv_media_title&& !TextUtils.isEmpty(titleStr)){
            tv_media_title.setText(titleStr);
        }
    }
    /**绑定布局*/
    public void setAnchorView(ViewGroup view) {  
          
        mAnchorVGroup = view;  
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(  
                ViewGroup.LayoutParams.MATCH_PARENT,  
                ViewGroup.LayoutParams.MATCH_PARENT  
        );  
        removeAllViews();  
          
        View v = createCtrlView();  
        addView(v, frameParams);  
    }  
  
    protected View createCtrlView() {  
     
        LayoutInflater inflate = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
          
        mRootView = inflate.inflate(R.layout.custome_media_controller, null);
        initCtrlView(mRootView);  
  
        return mRootView;  
    }  
  
    private void initCtrlView(View v) {
        ll_ver_top_content=(LinearLayout)v.findViewById(R.id.ll_ver_top_content);
        ll_ver_content=(LinearLayout)v.findViewById(R.id.ll_ver_content);
        ll_hor_content=(LinearLayout)v.findViewById(R.id.ll_hor_content);

        iv_back_arrow= (ImageView) v.findViewById(R.id.iv_back_arrow);
        tv_media_title= (TextView) v.findViewById(R.id.tv_media_title);
        tv_show_power= (TextView) v.findViewById(R.id.tv_show_power);
        tv_show_time= (TextView) v.findViewById(R.id.tv_show_time);

        iv_changge_media_img= (ImageView) v.findViewById(R.id.iv_changge_media_img);
        tv_show_change_info= (TextView) v.findViewById(R.id.tv_show_change_info);

        if(null!=tv_media_title&& !TextUtils.isEmpty(titleStr)){
            tv_media_title.setText(titleStr);
        }

        if(control_flag==CONTROL_FLAG_VERTICAL){//竖向风格
            ll_ver_top_content.setVisibility(VISIBLE);
            ll_ver_content.setVisibility(VISIBLE);
            ll_hor_content.setVisibility(GONE);
        }else{//横向风格
            ll_ver_top_content.setVisibility(GONE);
            ll_ver_content.setVisibility(GONE);
            ll_hor_content.setVisibility(VISIBLE);
        }
        pause2 = (ImageButton) v.findViewById(R.id.pause2);
        mBtnPause = (ImageButton) v.findViewById(R.id.pause);
        if (mBtnPause != null) {
            mBtnPause.requestFocus();  
            mBtnPause.setOnClickListener(mPauseListener);
            pause2.requestFocus();
            pause2.setOnClickListener(mPauseListener);
        }

        fullscreen2 = (ImageButton) v.findViewById(R.id.fullscreen2);
        mBtnFullscreen = (ImageButton) v.findViewById(R.id.fullscreen);
        if (mBtnFullscreen != null) {
            mBtnFullscreen.requestFocus();  
            mBtnFullscreen.setOnClickListener(mFullscreenListener);
            fullscreen2.requestFocus();
            fullscreen2.setOnClickListener(mFullscreenListener);
        }  
  
        mBtnFfwd = (ImageButton) v.findViewById(R.id.ffwd);  
        if (mBtnFfwd != null) {  
            mBtnFfwd.setOnClickListener(mFfwdListener);  
            if (!mFromXml) {  
                mBtnFfwd.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);  
            }  
        }  
  
        mBtnRew = (ImageButton) v.findViewById(R.id.rew);  
        if (mBtnRew != null) {  
            mBtnRew.setOnClickListener(mRewListener);  
            if (!mFromXml) {  
                mBtnRew.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);  
            }  
        }  
  
        // By default these are hidden. They will be enabled when setPrevNextListeners() is called   
        mBtnNext = (ImageButton) v.findViewById(R.id.next);  
        if (mBtnNext != null && !mFromXml && !mIsListenersSet) {  
            mBtnNext.setVisibility(View.GONE);  
        }  
          
        mBtnPrev = (ImageButton) v.findViewById(R.id.prev);  
        if (mBtnPrev != null && !mFromXml && !mIsListenersSet) {  
            mBtnPrev.setVisibility(View.GONE);  
        }

        mediacontroller_progress2 = (AppCompatSeekBar) v.findViewById(R.id.mediacontroller_progress2);
        mProgress = (AppCompatSeekBar) v.findViewById(R.id.mediacontroller_progress);
        if (mProgress != null) {
            if (mProgress instanceof AppCompatSeekBar) {
                AppCompatSeekBar seeker = (AppCompatSeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
                mediacontroller_progress2.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
            mediacontroller_progress2.setMax(1000);
        }

        time2 = (TextView) v.findViewById(R.id.time2);
        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurTime = (TextView) v.findViewById(R.id.time_current);
        time_current2 = (TextView) v.findViewById(R.id.time_current2);
        mStrBuilder = new StringBuilder();
        mFormatter = new Formatter(mStrBuilder, Locale.getDefault());
        installPrevNextListeners();

        IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        if(null==receiver){
            receiver=new BatteryReceiver();
        }else{
            getContext().unregisterReceiver(receiver);
            receiver=new BatteryReceiver();
        }
        getContext().registerReceiver(receiver, filter);//注册BroadcastReceiver

        if(null==mTimeReceiver){
            mTimeReceiver=new TimeReceiver();
        }else{
            getContext().unregisterReceiver(mTimeReceiver);
            mTimeReceiver=new TimeReceiver();
        }
        IntentFilter mTimeFilter=new IntentFilter(Intent.ACTION_TIME_TICK);
        getContext().registerReceiver(mTimeReceiver, mTimeFilter);//注册BroadcastReceiver
    }

    /**销毁电量接收广播*/
    public void onDestroy(){
        try{
            if(null!=receiver){
                getContext().unregisterReceiver(receiver);
            }else{
                getContext().unregisterReceiver(receiver);
                receiver=null;
            }
        }catch (Exception e){
        }
        try{
            if(null!=mTimeReceiver){
                getContext().unregisterReceiver(mTimeReceiver);
            }else{
                getContext().unregisterReceiver(mTimeReceiver);
                mTimeReceiver=null;
            }
        }catch (Exception e){
        }
    }

    /**显示亮度变化*/
    public void showLigntChange(int persent){
        if(null==iv_changge_media_img||null==iv_changge_media_img){
            return;
        }
        iv_changge_media_img.setVisibility(VISIBLE);
        tv_show_change_info.setVisibility(VISIBLE);
        iv_changge_media_img.setImageResource(R.drawable.ic_wb_sunny_grey600_36dp);
        tv_show_change_info.setText(persent+"%");
    }
    /**显示声音变化*/
    public void showVoiceChange(int persent){
        if(null==iv_changge_media_img||null==iv_changge_media_img){
            return;
        }
        iv_changge_media_img.setVisibility(VISIBLE);
        tv_show_change_info.setVisibility(VISIBLE);
        iv_changge_media_img.setImageResource(R.drawable.ic_volume_up_grey600_36dp);
        tv_show_change_info.setText(persent+"%");
    }
    /** 
     * Show the controller on screen. It will go away automatically after 
     * 3 seconds of inactivity. 
     */  
    public void show() {  
        show(DEFTIMEOUT);  
    }  
  
    /** 
     * Disable pause or seek buttons if the stream cannot be paused or seeked. 
     * This requires the control interface to be a MediaPlayerControlExt 
     */  
    private void disableUnsupportedButtons() {  
        if (mPlayerCtrl == null) {  
            return;  
        }  
          
        try {  
            if (mBtnPause != null && !mPlayerCtrl.canPause()) {  
                mBtnPause.setEnabled(false);
                pause2.setEnabled(false);
            }
            if (mBtnRew != null && !mPlayerCtrl.canSeekBackward()) {  
                mBtnRew.setEnabled(false);
            }
            if (mBtnFfwd != null && !mPlayerCtrl.canSeekForward()) {  
                mBtnFfwd.setEnabled(false);  
            }
        } catch (IncompatibleClassChangeError ex) {  
              
        }  
    }  
      
    public void show(int timeout) {  
        if (!mIsShowing && mAnchorVGroup != null) {  
            setProgress();  
            if (mBtnPause != null) {  
                mBtnPause.requestFocus();
                pause2.requestFocus();
            }
            disableUnsupportedButtons();  
  
            FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(  
                ViewGroup.LayoutParams.MATCH_PARENT,  
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            );  
              
            mAnchorVGroup.addView(this, tlp);  
            mIsShowing = true;  
        }  
        updatePausePlay();  
        updateFullScreen();          
          
        mHandler.sendEmptyMessage(SHOW_PROGRESS);  
  
        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {  
            mHandler.removeMessages(FADE_OUT);  
            mHandler.sendMessageDelayed(msg, timeout);  
        }  
    }  
      
    public boolean isShowing() {  
        return mIsShowing;  
    }  
  
    /** 
     * Remove the controller from the screen. 
     */  
    public void hide() {
        if(null!=iv_changge_media_img){
            iv_changge_media_img.setVisibility(GONE);
        }
        if(null!=tv_show_change_info){
            tv_show_change_info.setVisibility(GONE);
        }

        if (mAnchorVGroup == null) {  
            return;  
        }  
  
        try {  
            mAnchorVGroup.removeView(this);  
            if(mHandler != null) {  
                mHandler.removeMessages(SHOW_PROGRESS);  
            }  
        } catch (IllegalArgumentException ex) {  
            Log.w("MediaController", "already removed");  
        }  
        mIsShowing = false;  
    }  
  
    private String stringForTime(int timeMs) {  
        int totalSeconds = timeMs / 1000;  
  
        int seconds = totalSeconds % 60;  
        int minutes = (totalSeconds / 60) % 60;  
        int hours   = totalSeconds / 3600;  
  
        mStrBuilder.setLength(0);  
        if (hours > 0) {  
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();  
        } else {  
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();  
        }  
    }  
  
    private int setProgress() {  
        if (mPlayerCtrl == null || mIsDragging) {  
            return 0;  
        }  
          
        int position = mPlayerCtrl.getCurPosition();  
        int duration = mPlayerCtrl.getDuration();  
        if (mProgress != null) {  
            if (duration > 0) {  
                // use long to avoid overflow  
                long pos = 1000L * position / duration;  
                mProgress.setProgress( (int) pos);
                mediacontroller_progress2.setProgress( (int) pos);
            }
            int percent = mPlayerCtrl.getBufPercent();  
            mProgress.setSecondaryProgress(percent * 10);
            mediacontroller_progress2.setSecondaryProgress(percent * 10);
        }
  
        if (mEndTime != null)  {
            mEndTime.setText(stringForTime(duration));
            time2.setText(stringForTime(duration));
        }
        if (mCurTime != null) {
            mCurTime.setText(stringForTime(position));
            time_current2.setText(stringForTime(position));
        }


        return position;  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {
        show(DEFTIMEOUT);
        if (null!=mGestureDetector&&mGestureDetector.onTouchEvent(event)){
            return true;
        }
        return true;  
    }  
  
    @Override  
    public boolean onTrackballEvent(MotionEvent ev) {  
        show(DEFTIMEOUT);  
        return false;  
    }  
  
    @Override  
    public boolean dispatchKeyEvent(KeyEvent event) {  
        if (mPlayerCtrl == null) {  
            return true;  
        }  
          
        int keyCode = event.getKeyCode();  
        final boolean uniqueDown = event.getRepeatCount() == 0  
                && event.getAction() == KeyEvent.ACTION_DOWN;  
        if (keyCode ==  KeyEvent.KEYCODE_HEADSETHOOK  
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE  
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {  
                doPauseResume();  
                show(DEFTIMEOUT);  
                if (mBtnPause != null) {  
                    mBtnPause.requestFocus();
                    pause2.requestFocus();
                }
            }  
            return true;  
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {  
            if (uniqueDown && !mPlayerCtrl.isPlaying()) {  
                mPlayerCtrl.start();  
                updatePausePlay();  
                show(DEFTIMEOUT);  
            }  
            return true;  
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP  
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {  
            if (uniqueDown && mPlayerCtrl.isPlaying()) {  
                mPlayerCtrl.pause();  
                updatePausePlay();  
                show(DEFTIMEOUT);  
            }  
            return true;  
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN  
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP  
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {  
            // don't show the controls for volume adjustment  
            return super.dispatchKeyEvent(event);  
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {  
            if (uniqueDown) {  
                hide();  
            }  
            return true;  
        }  
  
        show(DEFTIMEOUT);  
        return super.dispatchKeyEvent(event);  
    }  
  
    private View.OnClickListener mPauseListener = new View.OnClickListener() {  
        public void onClick(View v) {  
            doPauseResume();  
            show(DEFTIMEOUT);  
        }  
    };  
  
    private View.OnClickListener mFullscreenListener = new View.OnClickListener() {  
        public void onClick(View v) {  
            doToggleFullscreen();  
            show(DEFTIMEOUT);  
        }  
    };  
  
    public void updatePausePlay() {  
        if (mRootView == null || mBtnPause == null || mPlayerCtrl == null||pause2==null) {
            return;  
        }  
  
        if (mPlayerCtrl.isPlaying()) {
            mBtnPause.setImageResource(android.R.drawable.ic_media_play);
            pause2.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mBtnPause.setImageResource(android.R.drawable.ic_media_pause);
            pause2.setImageResource(android.R.drawable.ic_media_pause);
        }
    }  
  
    public void updateFullScreen() {  
        if (mRootView == null || mBtnFullscreen == null || mPlayerCtrl == null||fullscreen2==null) {
            return;  
        }  
          
        if (mPlayerCtrl.isFullScreen()) {  
            mBtnFullscreen.setImageResource(R.drawable.ic_screen_rotation_grey);
            fullscreen2.setImageResource(R.drawable.ic_screen_rotation_grey);
        }
        else {  
            mBtnFullscreen.setImageResource(R.drawable.ic_screen_rotation_grey);
            fullscreen2.setImageResource(R.drawable.ic_screen_rotation_grey);
        }
    }  
  
    private void doPauseResume() {  
        if (mPlayerCtrl == null) {  
            return;  
        }  
          
        if (mPlayerCtrl.isPlaying()) {  
            mPlayerCtrl.pause();  
        } else {  
            mPlayerCtrl.start();  
        }  
        updatePausePlay();  
    }  
  
    private void doToggleFullscreen() {  
        if (mPlayerCtrl == null) {  
            return;  
        }  
        Log.i(LOG_TAG, "doToggleFullscreen");
        mPlayerCtrl.fullScreen();  
    }  
  
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {  
            show(3600000);  
  
            mIsDragging = true;  
            mHandler.removeMessages(SHOW_PROGRESS);  
        }  
  
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {  
            if (mPlayerCtrl == null) {  
                return;  
            }  
              
            if (!fromuser) {  
                // We're not interested in programmatically generated changes to  
                // the progress bar's position.  
                return;  
            }  
  
            long duration = mPlayerCtrl.getDuration();  
            long newposition = (duration * progress) / 1000L;  
            mPlayerCtrl.seekTo( (int) newposition);  
            if (mCurTime != null)  {
                mCurTime.setText(stringForTime( (int) newposition));
                time_current2.setText(stringForTime( (int) newposition));
            }

        }  
  
        public void onStopTrackingTouch(SeekBar bar) {  
            mIsDragging = false;  
            setProgress();  
            updatePausePlay();  
            show(DEFTIMEOUT);  
              
            mHandler.sendEmptyMessage(SHOW_PROGRESS);  
        }  
    };  
  
    @Override  
    public void setEnabled(boolean enabled) {  
        if (mBtnPause != null) {  
            mBtnPause.setEnabled(enabled);
            pause2.setEnabled(enabled);
        }
        if (mBtnFfwd != null) {  
            mBtnFfwd.setEnabled(enabled);  
        }  
        if (mBtnRew != null) {  
            mBtnRew.setEnabled(enabled);  
        }  
        if (mBtnNext != null) {  
            mBtnNext.setEnabled(enabled && mNextListener != null);  
        }  
        if (mBtnPrev != null) {  
            mBtnPrev.setEnabled(enabled && mPrevListener != null);  
        }  
        if (mProgress != null) {  
            mProgress.setEnabled(enabled);
            mediacontroller_progress2.setEnabled(enabled);
        }
        disableUnsupportedButtons();  
        super.setEnabled(enabled);  
    }  
  
    @SuppressLint("NewApi")  
    @Override  
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);  
        event.setClassName(Controller.class.getName());  
    }  
  
    @SuppressLint("NewApi")
    @Override  
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);  
        info.setClassName(Controller.class.getName());  
    }  
  
    private View.OnClickListener mRewListener = new View.OnClickListener() {  
        public void onClick(View v) {  
            if (mPlayerCtrl == null) {  
                return;  
            }  
              
            int pos = mPlayerCtrl.getCurPosition();  
            pos -= 5000; // milliseconds  
            mPlayerCtrl.seekTo(pos);  
            setProgress();  
  
            show(DEFTIMEOUT);  
        }  
    };  
  
    private View.OnClickListener mFfwdListener = new View.OnClickListener() {  
        public void onClick(View v) {  
            if (mPlayerCtrl == null) {  
                return;  
            }  
              
            int pos = mPlayerCtrl.getCurPosition();  
            pos += 15000; // milliseconds  
            mPlayerCtrl.seekTo(pos);  
            setProgress();  
  
            show(DEFTIMEOUT);  
        }  
    };  
  
    private void installPrevNextListeners() {
        if(null!=iv_back_arrow){
            iv_back_arrow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null!=mPlayerCtrl){
                        mPlayerCtrl.onClickBack();
                    }
                }
            });
        }
        if (mBtnNext != null) {  
            mBtnNext.setOnClickListener(mNextListener);  
            mBtnNext.setEnabled(mNextListener != null);  
        }  
  
        if (mBtnPrev != null) {  
            mBtnPrev.setOnClickListener(mPrevListener);  
            mBtnPrev.setEnabled(mPrevListener != null);  
        }
        mGestureDetector=new GestureDetector(getContext(),new MyGestureListener());
       mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }  
  
    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {  
        mNextListener = next;  
        mPrevListener = prev;  
        mIsListenersSet = true;  
  
        if (mRootView != null) {  
            installPrevNextListeners();  
              
            if (mBtnNext != null && !mFromXml) {  
                mBtnNext.setVisibility(View.VISIBLE);  
            }  
            if (mBtnPrev != null && !mFromXml) {  
                mBtnPrev.setVisibility(View.VISIBLE);  
            }  
        }  
    }  
      
    public interface ControlOper {
        void    onClickBack();
        void    start();  
        void    pause();  
        int     getDuration();  
        int     getCurPosition();  
        void    seekTo(int pos);  
        boolean isPlaying();  
        int     getBufPercent();  
        boolean canPause();  
        boolean canSeekBackward();  
        boolean canSeekForward();  
        boolean isFullScreen();  
        void    fullScreen();  
    }  
      
    private static class MsgHandler extends Handler {  
          
        private final WeakReference<Controller> mView;
  
        MsgHandler(Controller view) {  
            mView = new WeakReference<Controller>(view);  
        }  
          
        @Override  
        public void handleMessage(Message msg) {  
              
            Controller view = mView.get();  
            if (view == null || view.mPlayerCtrl == null) {  
                return;  
            }  
              
            int pos;  
            switch (msg.what) {  
              
                case FADE_OUT: {  
                    view.hide();  
                    break;  
                }  
                  
                case SHOW_PROGRESS: {  
                      
                    if(view.mPlayerCtrl.isPlaying()) {  
                        pos = view.setProgress();  
                    } else {  
                        return;  
                    }  
                      
                    if (!view.mIsDragging && view.mIsShowing  
                            && view.mPlayerCtrl.isPlaying()) {  
                          
                        msg = obtainMessage(SHOW_PROGRESS);  
                        sendMessageDelayed(msg, 1000 - (pos % 1000));  
                    }  
                    break;  
                }  
            }  
        }  
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        /** 双击 */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        /** 滑动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            float beginX = e1.getX();
            float endX = e2.getX();
            float beginY = e1.getY();
            float endY = e2.getY();

            Display disp = ((Activity)getContext()).getWindow().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            disp.getSize(size);
            int windowWidth = size.x;
            int windowHeight = size.y;
            if (Math.abs(endX - beginX) < Math.abs(beginY - endY)) {//上下滑动
                if (beginX > windowWidth * 3.0 / 4.0) {// 右边滑动 屏幕3/5
                    onVolumeSlide((beginY - endY) / windowHeight);
                } else if (beginX < windowWidth * 1.0 / 4.0) {// 左边滑动 屏幕2/5
                    onBrightnessSlide((beginY - endY) / windowHeight);
                }
            }else {//左右滑动
                onSeekTo((endX - beginX) / 20);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if(null==mAudioManager){
            return;
        }
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0){
                mVolume = 0;
            }
           // showVoiceChange(mVolume);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        double nu=((float)index/(float)mMaxVolume)*100;
        LogUtils.d("---------voice nu-index-mMaxVolume-mVolume:"+nu+"-"+index+"-"+mMaxVolume+"-"+mVolume);
        //显示声音大小
        showVoiceChange((int)nu);
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = ((Activity)getContext()).getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
            showLigntChange((int)mBrightness);
        }
        WindowManager.LayoutParams lpa = ((Activity)getContext()).getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f){
            lpa.screenBrightness = 1.0f;
        }else if (lpa.screenBrightness < 0.01f){
            lpa.screenBrightness = 0.01f;
        }
        ((Activity)getContext()).getWindow().setAttributes(lpa);
        showLigntChange((int)(lpa.screenBrightness * 100));
    }
    /**电量广播接收器*/
    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int current=intent.getExtras().getInt("level");//获得当前电量
            int total=intent.getExtras().getInt("scale");//获得总电量
            int percent=current*100/total;
            if(null!=tv_show_power){
                tv_show_power.setVisibility(VISIBLE);
                tv_show_power.setText(percent+"%");
            }

        }
    }
    /**时钟接收器*/
    class TimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                if(null!=tv_show_time){
                    long time=System.currentTimeMillis();
                    DateFormat formatter = new SimpleDateFormat("hh:mm");
                    tv_show_time.setText(formatter.format(time));
                }
            }
        }
    }

    /**
     * 滑动改变播放进度
     *
     * @param percent
     */
    private void onSeekTo(float percent) {
        //计算并显示 前进后退
        if(null==mPlayerCtrl){
            return;
        }
        int change = (int) (percent);
        int perdr=5*1000;
        int nowDra=mPlayerCtrl.getDuration()*(mProgress.getProgress()/1000);
        if(change>0){
            iv_changge_media_img.setVisibility(GONE);
            tv_show_change_info.setVisibility(VISIBLE);
            tv_show_change_info.setText("快进");
            if(nowDra+perdr<=mPlayerCtrl.getDuration()){
                mPlayerCtrl.seekTo(nowDra+perdr);
                setProgress();
            }else{
                mPlayerCtrl.seekTo(mPlayerCtrl.getDuration());
                setProgress();
            }
        }else if(change<0){
            iv_changge_media_img.setVisibility(GONE);
            tv_show_change_info.setVisibility(VISIBLE);
            tv_show_change_info.setText("快退");
            if(nowDra-perdr>=0){
                mPlayerCtrl.seekTo(nowDra-perdr);
                setProgress();
            }else{
                mPlayerCtrl.seekTo(0);
                setProgress();
            }
        }
    }
}