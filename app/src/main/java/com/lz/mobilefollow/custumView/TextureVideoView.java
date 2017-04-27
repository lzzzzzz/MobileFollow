package com.lz.mobilefollow.custumView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;

@SuppressLint("NewApi")
public class TextureVideoView extends TextureView implements
		SurfaceTextureListener {

	boolean playFinished=false;//是否播放完毕,在onCompletion中值修改为true，表示播放完毕，不在调用onSeek
	
	private MediaPlayer mediaPlayer;
	MediaState mediaState;

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public interface OnStateChangeListener {
		public void onSurfaceTextureDestroyed(SurfaceTexture surface);

		public void onBuffering();

		public void onPlaying();

		public void onSeek(int max, int progress);

		public void onStop();
		
		public void onPause();

		public void onTextureViewAvaliable();

		public void playFinish();

		public void onPrepare();
	}

	OnStateChangeListener onStateChangeListener;

	public void setOnStateChangeListener(
			OnStateChangeListener onStateChangeListener) {
		this.onStateChangeListener = onStateChangeListener;
	}

	private OnInfoListener onInfoListener = new OnInfoListener() {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			if (onStateChangeListener != null&&mediaState!=MediaState.PAUSE) {
				onStateChangeListener.onPlaying();
				if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
					onStateChangeListener.onBuffering();
				} else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
					onStateChangeListener.onPlaying();
				}
			}
			return false;
		}
	};

	private OnBufferingUpdateListener bufferingUpdateListener = new OnBufferingUpdateListener() {
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			if (onStateChangeListener != null) {
				//在某些情况下视频一次性缓冲100%，有些手机竟然不调用OnInfo回调，比如小米2，导致缓冲指示器一直显示，所以在此处添加此代码
				if(percent==100&&mediaState!=MediaState.PAUSE)
				{
					mediaState = MediaState.PLAYING;
					onStateChangeListener.onPlaying();
				}
				if (mediaState == MediaState.PLAYING) {
					if(playFinished)
						return;
					onStateChangeListener.onSeek(mediaPlayer.getDuration(),
							mediaPlayer.getCurrentPosition());
				}
			}
		}
	};

	public TextureVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		setSurfaceTextureListener(this);
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
			int width, int height) {
		Surface surface = new Surface(surfaceTexture);
		if (mediaPlayer == null) {
			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
			}
			mediaPlayer
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mediaPlayer) {
							playFinished=false;
							mediaPlayer.start();
							mediaState = MediaState.PLAYING;
						}
					});
			mediaPlayer.setOnInfoListener(onInfoListener);
			mediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					if (onStateChangeListener != null) {
						if (mediaState != MediaState.PLAYING)
							return;
						onStateChangeListener.playFinish();
						playFinished=true;
					}
				}
			});
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mediaPlayer.reset();
					mediaState = MediaState.INIT;
					onStateChangeListener.onStop();
					return false;
				}
			});
		}
		mediaPlayer.setSurface(surface);
		mediaState = MediaState.INIT;
		if (onStateChangeListener != null) {
			onStateChangeListener.onTextureViewAvaliable();
		}
	}

	public void stop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (mediaState == MediaState.INIT) {
						return;
					}
					if (mediaState == MediaState.PREPARING) {
						mediaPlayer.reset();
						mediaState = MediaState.INIT;
						return;
					}
					if (mediaState == MediaState.PAUSE) {
						mediaPlayer.stop();
						mediaPlayer.reset();
						mediaState = MediaState.INIT;
						return;
					}
					if (mediaState == MediaState.PLAYING) {
						mediaPlayer.pause();
						mediaPlayer.stop();
						mediaPlayer.reset();
						mediaState = MediaState.INIT;
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (null != mediaPlayer)
						mediaPlayer.reset();
					mediaState = MediaState.INIT;
				} finally {
					Message.obtain(mHandler, STOP).sendToTarget();
				}
			}
		}).start();
	}

	private final int STOP=0;
	Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOP:
				if (onStateChangeListener != null) {
					onStateChangeListener.onStop();
				}
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		if (onStateChangeListener != null) {
			onStateChangeListener.onSurfaceTextureDestroyed(surface);
		}
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
	}

	public void play(String videoUrl) {
		play(videoUrl, true);
	}

	public void play(String videoUrl, boolean looping) {
		if (mediaState == MediaState.PREPARING) {
			stop();
			return;
		}
		mediaPlayer.reset();
		mediaPlayer.setLooping(looping);
		try {
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepareAsync();
			if (onStateChangeListener != null) {
				onStateChangeListener.onPrepare();
			}
			mediaState = MediaState.PREPARING;
		} catch (Exception e) {
			mediaPlayer.reset();
			mediaState = MediaState.INIT;
		}
	}

	public void pause() {
		mediaPlayer.pause();
		mediaState = MediaState.PAUSE;
		if(onStateChangeListener!=null)
		{
			onStateChangeListener.onPause();
		}
	}

	public void start() {
		playFinished=false;
		mediaPlayer.start();
		mediaState = MediaState.PLAYING;
	}

	public enum MediaState {
		INIT, PREPARING, PLAYING, PAUSE, RELEASE;
	}

	public MediaState getState() {
		return mediaState;
	}

}
