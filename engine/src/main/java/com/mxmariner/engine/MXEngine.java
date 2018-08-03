package com.mxmariner.engine;

public class MXEngine {

    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJni();
}
