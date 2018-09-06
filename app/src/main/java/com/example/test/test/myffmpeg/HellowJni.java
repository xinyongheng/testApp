package com.example.test.test.myffmpeg;

/**
 * Created by xinheng on 2017/12/1.
 * describe：
 */

public class HellowJni {
    static {
        /*System.loadLibrary("avfilter-6");
        System.loadLibrary("avformat-57");
        System.loadLibrary("avcodec-57");
        System.loadLibrary("swresample-2");
        System.loadLibrary("swscale-4");
        System.loadLibrary("avutil-55");
        System.loadLibrary("avdevice-57");
        System.loadLibrary("postproc-54");*/
        System.loadLibrary("ffmpeg-inf");
    }
    //声明函数
    public native String configurationinfo();
    public native String getAndroidCpuType();
}
