package com.example.test.test;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by xinheng on 2017/11/24.
 * describe：
 */

public class MyMediaPlayer {

    private final MediaPlayer mMediaPlayer;
    private final String TAG=MyMediaPlayer.class.getSimpleName();
    public MyMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
        init();
        /*try {
            mMediaPlayer.setDataSource("http://www.xiwang.com/task/resourceview?id=462&token=BSZHgiovoprjhuz7ujjwo89ek5y3zy2stjg7");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //mMediaPlayer.prepareAsync();
    }
    private void init(){
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.e(TAG, "onBufferingUpdate: "+percent );
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e(TAG, "onCompletion: " );
            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                mp.reset();
            }
        });
    }
    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }
    public void statr(){
        mMediaPlayer.prepareAsync();
    }
}
