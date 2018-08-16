package com.mapbox.mapboxsdk.maps;


import com.mapbox.mapboxsdk.BuildConfig;
import com.mapbox.mapboxsdk.Mapbox;

public class Telemetry {
  static final String TWO_FINGER_TAP = "TwoFingerTap";
  static final String DOUBLE_TAP = "DoubleTap";
  static final String SINGLE_TAP = "SingleTap";
  static final String PAN = "Pan";
  static final String PINCH = "Pinch";
  static final String ROTATION = "Rotation";
  static final String PITCH = "Pitch";

  private Telemetry() {
  }

  public static void initialize() {
  }

  /**
   * Set the debug logging state of telemetry.
   *
   * @param debugLoggingEnabled true to enable logging
   */
  public static void updateDebugLoggingEnabled(boolean debugLoggingEnabled) {
  }

  /**
   * Method to be called when an end-user has selected to participate in telemetry collection.
   */
  public static void enableOnUserRequest() {
  }

  /**
   * Method to be called when an end-user has selected to opt-out of telemetry collection.
   */
  public static void disableOnUserRequest() {
  }

  private static class TelemetryHolder {
    private static final Telemetry INSTANCE = new Telemetry();
  }
}
