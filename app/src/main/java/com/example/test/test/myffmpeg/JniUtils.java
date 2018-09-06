package com.example.test.test.myffmpeg;

/**
 * Created by xinheng on 2017/12/1.
 * describe：
 */

public class JniUtils {
    static {
        System.loadLibrary("native-lib");
    }
    public static native String getStringFormC();
    public static native String getAndroidCpuType();

    public static native String jiaMi(String s);

}
