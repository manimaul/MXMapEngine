package com.mapbox.mapboxsdk.utils;

import android.util.Log;

import com.mapbox.mapboxsdk.BuildConfig;

public class Logger {

    public static void v(String tag, String msg, Object... fmt) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, String.format(msg, fmt));
        }
    }

    public static void i(String tag, String msg, Object... fmt) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, String.format(msg, fmt));
        }
    }

    public static void d(String tag, String msg, Object... fmt) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, String.format(msg, fmt));
        }
    }

    public static void w(String tag, String msg, Object... fmt) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, String.format(msg, fmt));
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg, e);
        }
    }

    public static void w(String tag, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, "", e);
        }
    }

    public static void w(String tag, Throwable e, String msg, Object... fmt) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, String.format(msg, fmt), e);
        }
    }

    public static void e(String tag, String msg, Object... fmt) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, String.format(msg, fmt));
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg, e);
        }
    }

    public static void e(String tag, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "", e);
        }
    }

    public static void e(String tag, Throwable e, String msg, Object... fmt) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, String.format(msg, fmt), e);
        }
    }
}
