package com.mapbox.core.constants;


/**
 * Includes common variables used throughout the Mapbox Service modules.
 *
 * @since 3.0.0
 */
public final class Constants {

  /**
   * Use a precision of 6 decimal places when encoding or decoding a polyline.
   *
   * @since 2.1.0
   */
  public static final int PRECISION_6 = 6;

  /**
   * Use a precision of 5 decimal places when encoding or decoding a polyline.
   *
   * @since 1.0.0
   */
  public static final int PRECISION_5 = 5;

  private Constants() {
    // Empty constructor prevents users from initializing this class
  }
}
