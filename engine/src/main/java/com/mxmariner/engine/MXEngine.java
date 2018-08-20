package com.mxmariner.engine;

public class MXEngine {

    static {
        System.loadLibrary("mapbox-gl");
    }

    public static native String stringFromJni();
}
