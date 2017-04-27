package com.lz.mobilefollow.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by LZ on 2016/10/31.
 */

public class MediaPlayUtils {
    private static MediaPlayer mpMediaPlayer;
    private static MediaPlayUtils instance;
    public static MediaPlayUtils getInstance(){
        if(instance==null){
            instance=new MediaPlayUtils();
        }
        if(null==mpMediaPlayer){
            mpMediaPlayer=new MediaPlayer();
        }
        return instance;
    }
    public void play(Context context,String assetName) {
        AssetManager am = context.getAssets();
        final MediaPlayer mpMediaPlayer = new MediaPlayer();
        try {
            mpMediaPlayer.setDataSource(am.openFd(assetName).getFileDescriptor());
            mpMediaPlayer.prepare();
            mpMediaPlayer.start();
            mpMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer arg0) {
                    mpMediaPlayer.start();
                    mpMediaPlayer.setLooping(true);
                }
            });
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void destroy(){
        if(mpMediaPlayer!=null||mpMediaPlayer.isPlaying()){
            mpMediaPlayer=null;
        }
    }
}
