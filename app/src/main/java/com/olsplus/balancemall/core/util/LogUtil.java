package com.olsplus.balancemall.core.util;

import android.util.Log;

import com.olsplus.balancemall.BuildConfig;

/**
 * Log统一管理类
 */
public final class LogUtil {

    private LogUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = "Log";

    // 下面四个是默认tag的函数 
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数 
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
}