package com.mapbox.mapboxsdk.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.Arrays;

/**
 * Defines configuration MapboxMapMapOptions for a MapboxMap. These options can be used when adding a
 * map to your application programmatically (as opposed to via XML). If you are using a MapFragment,
 * you can pass these options in using the static factory method newInstance(MapboxMapOptions).
 * If you are using a MapView, you can pass these options in using the constructor
 * MapView(Context, MapboxMapOptions). If you add a map using XML, then you can apply these options
 * using custom XML tags.
 */
public class MapboxMapOptions implements Parcelable {

  private static final float FOUR_DP = 4f;

  private CameraPosition cameraPosition;

  private boolean debugActive;

  private boolean compassEnabled = true;
  private boolean fadeCompassFacingNorth = true;
  private int compassGravity = Gravity.TOP | Gravity.END;
  private int[] compassMargins;
  private Drawable compassImage;

  private double minZoom = MapboxConstants.MINIMUM_ZOOM;
  private double maxZoom = MapboxConstants.MAXIMUM_ZOOM;

  private boolean rotateGesturesEnabled = true;
  private boolean scrollGesturesEnabled = true;
  private boolean tiltGesturesEnabled = true;
  private boolean zoomGesturesEnabled = true;
  private boolean zoomControlsEnabled = false;
  private boolean doubleTapGesturesEnabled = true;

  private boolean prefetchesTiles = true;
  private boolean zMediaOverlay = false;
  private String localIdeographFontFamily;

  private String apiBaseUrl;

  private boolean textureMode;
  private boolean translucentTextureSurface;

  private String style;

  private float pixelRatio;

  /**
   * Creates a new MapboxMapOptions object.
   */
  public MapboxMapOptions() {
  }

  private MapboxMapOptions(Parcel in) {
    cameraPosition = in.readParcelable(CameraPosition.class.getClassLoader());
    debugActive = in.readByte() != 0;

    compassEnabled = in.readByte() != 0;
    compassGravity = in.readInt();
    compassMargins = in.createIntArray();
    fadeCompassFacingNorth = in.readByte() != 0;

    Bitmap compassBitmap = in.readParcelable(getClass().getClassLoader());
    if (compassBitmap != null) {
      compassImage = new BitmapDrawable(compassBitmap);
    }

    minZoom = in.readDouble();
    maxZoom = in.readDouble();

    rotateGesturesEnabled = in.readByte() != 0;
    scrollGesturesEnabled = in.readByte() != 0;
    tiltGesturesEnabled = in.readByte() != 0;
    zoomControlsEnabled = in.readByte() != 0;
    zoomGesturesEnabled = in.readByte() != 0;
    doubleTapGesturesEnabled = in.readByte() != 0;

    style = in.readString();
    apiBaseUrl = in.readString();
    textureMode = in.readByte() != 0;
    translucentTextureSurface = in.readByte() != 0;
    prefetchesTiles = in.readByte() != 0;
    zMediaOverlay = in.readByte() != 0;
    localIdeographFontFamily = in.readString();
    pixelRatio = in.readFloat();
  }

  /**
   * Creates a MapboxMapsOptions from the attribute set.s
   *
   * @param context Context related to a map view.
   * @param attrs   Attributeset containing configuration
   * @return the MapboxMapOptions created from attributes
   */
  public static MapboxMapOptions createFromAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
    MapboxMapOptions mapboxMapOptions = new MapboxMapOptions();
    float pxlRatio = context.getResources().getDisplayMetrics().density;
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.mapbox_MapView, 0, 0);
    try {
      mapboxMapOptions.camera(new CameraPosition.Builder(typedArray).build());
      mapboxMapOptions.styleUrl(typedArray.getString(R.styleable.mapbox_MapView_mapbox_styleUrl));
      mapboxMapOptions.apiBaseUrl(typedArray.getString(R.styleable.mapbox_MapView_mapbox_apiBaseUrl));

      mapboxMapOptions.zoomGesturesEnabled(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiZoomGestures, true));
      mapboxMapOptions.scrollGesturesEnabled(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiScrollGestures, true));
      mapboxMapOptions.rotateGesturesEnabled(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiRotateGestures, true));
      mapboxMapOptions.tiltGesturesEnabled(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiTiltGestures, true));
      mapboxMapOptions.zoomControlsEnabled(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiZoomControls, false));
      mapboxMapOptions.doubleTapGesturesEnabled(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiDoubleTapGestures, true));

      mapboxMapOptions.maxZoomPreference(typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraZoomMax,
        MapboxConstants.MAXIMUM_ZOOM));
      mapboxMapOptions.minZoomPreference(typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraZoomMin,
        MapboxConstants.MINIMUM_ZOOM));

      mapboxMapOptions.compassEnabled(typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_uiCompass, true));
      mapboxMapOptions.compassGravity(typedArray.getInt(R.styleable.mapbox_MapView_mapbox_uiCompassGravity,
        Gravity.TOP | Gravity.END));
      mapboxMapOptions.compassMargins(new int[] {
        (int) (typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiCompassMarginLeft,
          FOUR_DP * pxlRatio)),
        ((int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiCompassMarginTop,
          FOUR_DP * pxlRatio)),
        ((int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiCompassMarginRight,
          FOUR_DP * pxlRatio)),
        ((int) typedArray.getDimension(R.styleable.mapbox_MapView_mapbox_uiCompassMarginBottom,
          FOUR_DP * pxlRatio))});
      mapboxMapOptions.compassFadesWhenFacingNorth(typedArray.getBoolean(
        R.styleable.mapbox_MapView_mapbox_uiCompassFadeFacingNorth, true));
      Drawable compassDrawable = typedArray.getDrawable(
        R.styleable.mapbox_MapView_mapbox_uiCompassDrawable);
      if (compassDrawable == null) {
        compassDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.mapbox_compass_icon, null);
      }
      mapboxMapOptions.compassImage(compassDrawable);

      mapboxMapOptions.textureMode(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_renderTextureMode, false));
      mapboxMapOptions.translucentTextureSurface(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_renderTextureTranslucentSurface, false));
      mapboxMapOptions.setPrefetchesTiles(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_enableTilePrefetch, true));
      mapboxMapOptions.renderSurfaceOnTop(
        typedArray.getBoolean(R.styleable.mapbox_MapView_mapbox_enableZMediaOverlay, false));
      mapboxMapOptions.localIdeographFontFamily(
        typedArray.getString(R.styleable.mapbox_MapView_mapbox_localIdeographFontFamily));
      mapboxMapOptions.pixelRatio(
        typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_pixelRatio, 0));
    } finally {
      typedArray.recycle();
    }
    return mapboxMapOptions;
  }

  /**
   * Specifies the URL used for API endpoint.
   *
   * @param apiBaseUrl The base of our API endpoint
   * @return This
   */
  public MapboxMapOptions apiBaseUrl(String apiBaseUrl) {
    this.apiBaseUrl = apiBaseUrl;
    return this;
  }

  /**
   * Specifies a the initial camera position for the map view.
   *
   * @param cameraPosition Inital camera position
   * @return This
   */
  public MapboxMapOptions camera(CameraPosition cameraPosition) {
    this.cameraPosition = cameraPosition;
    return this;
  }

  /**
   * Specifies the style url associated with a map view.
   *
   * @param styleUrl Url to be used to load a style
   * @return This
   */
  public MapboxMapOptions styleUrl(String styleUrl) {
    style = styleUrl;
    return this;
  }

  /**
   * Specifies the used debug type for a map view.
   *
   * @param enabled True is debug is enabled
   * @return This
   */
  public MapboxMapOptions debugActive(boolean enabled) {
    debugActive = enabled;
    return this;
  }

  /**
   * Specifies the used minimum zoom level for a map view.
   *
   * @param minZoom Zoom level to be used
   * @return This
   */
  public MapboxMapOptions minZoomPreference(double minZoom) {
    this.minZoom = minZoom;
    return this;
  }

  /**
   * Specifies the used maximum zoom level for a map view.
   *
   * @param maxZoom Zoom level to be used
   * @return This
   */
  public MapboxMapOptions maxZoomPreference(double maxZoom) {
    this.maxZoom = maxZoom;
    return this;
  }

  /**
   * Specifies the visibility state of a mapbox_compass_icon for a map view.
   *
   * @param enabled True and mapbox_compass_icon is shown
   * @return This
   */
  public MapboxMapOptions compassEnabled(boolean enabled) {
    compassEnabled = enabled;
    return this;
  }

  /**
   * Specifies the gravity state of mapbox_compass_icon for a map view.
   *
   * @param gravity Android SDK Gravity.
   * @return This
   */
  public MapboxMapOptions compassGravity(int gravity) {
    compassGravity = gravity;
    return this;
  }

  /**
   * Specifies the margin state of mapbox_compass_icon for a map view
   *
   * @param margins 4 long array for LTRB margins
   * @return This
   */
  public MapboxMapOptions compassMargins(int[] margins) {
    compassMargins = margins;
    return this;
  }

  /**
   * Specifies if the mapbox_compass_icon fades to invisible when facing north.
   * <p>
   * By default this value is true.
   * </p>
   *
   * @param compassFadeWhenFacingNorth true is mapbox_compass_icon fades to invisble
   * @return This
   */
  public MapboxMapOptions compassFadesWhenFacingNorth(boolean compassFadeWhenFacingNorth) {
    this.fadeCompassFacingNorth = compassFadeWhenFacingNorth;
    return this;
  }

  /**
   * Specifies the image of the CompassView.
   * <p>
   * By default this value is R.drawable.mapbox_compass_icon.
   * </p>
   *
   * @param compass the drawable to show as image compass
   * @return This
   */
  public MapboxMapOptions compassImage(Drawable compass) {
    this.compassImage = compass;
    return this;
  }

  /**
   * Specifies if the rotate gesture is enabled for a map view.
   *
   * @param enabled True and gesture will be enabled
   * @return This
   */
  public MapboxMapOptions rotateGesturesEnabled(boolean enabled) {
    rotateGesturesEnabled = enabled;
    return this;
  }

  /**
   * Specifies if the scroll gesture is enabled for a map view.
   *
   * @param enabled True and gesture will be enabled
   * @return This
   */
  public MapboxMapOptions scrollGesturesEnabled(boolean enabled) {
    scrollGesturesEnabled = enabled;
    return this;
  }

  /**
   * Specifies if the tilt gesture is enabled for a map view.
   *
   * @param enabled True and gesture will be enabled
   * @return This
   */
  public MapboxMapOptions tiltGesturesEnabled(boolean enabled) {
    tiltGesturesEnabled = enabled;
    return this;
  }

  /**
   * Specifies if the zoom controls are enabled for a map view.
   *
   * @param enabled True and gesture will be enabled
   * @return This
   */
  public MapboxMapOptions zoomControlsEnabled(boolean enabled) {
    zoomControlsEnabled = enabled;
    return this;
  }

  /**
   * Specifies if the zoom gesture is enabled for a map view.
   *
   * @param enabled True and gesture will be enabled
   * @return This
   */
  public MapboxMapOptions zoomGesturesEnabled(boolean enabled) {
    zoomGesturesEnabled = enabled;
    return this;
  }

  /**
   * Specifies if the double tap gesture is enabled for a map view.
   *
   * @param enabled True and gesture will be enabled
   * @return This
   */
  public MapboxMapOptions doubleTapGesturesEnabled(boolean enabled) {
    doubleTapGesturesEnabled = enabled;
    return this;
  }

  /**
   * Enable {@link android.view.TextureView} as rendered surface.
   * <p>
   * Since the 5.2.0 release we replaced our TextureView with an {@link android.opengl.GLSurfaceView}
   * implementation. Enabling this option will use the {@link android.view.TextureView} instead.
   * {@link android.view.TextureView} can be useful in situations where you need to animate, scale
   * or transform the view. This comes at a siginficant performance penalty and should not be considered
   * unless absolutely needed.
   * </p>
   *
   * @param textureMode True to enable texture mode
   * @return This
   */
  public MapboxMapOptions textureMode(boolean textureMode) {
    this.textureMode = textureMode;
    return this;
  }

  public MapboxMapOptions translucentTextureSurface(boolean translucentTextureSurface) {
    this.translucentTextureSurface = translucentTextureSurface;
    return this;
  }

  /**
   * Enable tile pre-fetching. Loads tiles at a lower zoom-level to pre-render
   * a low resolution preview while more detailed tiles are loaded.
   * Enabled by default
   *
   * @param enable true to enable
   * @return This
   */
  public MapboxMapOptions setPrefetchesTiles(boolean enable) {
    this.prefetchesTiles = enable;
    return this;
  }

  /**
   * Set the font family for generating glyphs locally for ideographs in the &#x27;CJK Unified Ideographs&#x27;
   * and &#x27;Hangul Syllables&#x27; ranges.
   * <p>
   * The font family argument is passed to {@link android.graphics.Typeface#create(String, int)}.
   * Default system fonts are defined in &#x27;/system/etc/fonts.xml&#x27;
   *
   * @param fontFamily font family for local ideograph generation.
   * @return This
   */
  public MapboxMapOptions localIdeographFontFamily(String fontFamily) {
    this.localIdeographFontFamily = fontFamily;
    return this;
  }

  /**
   * Set the custom pixel ratio configuration to override the default value from resources.
   * This ratio will be used to initialise the map with.
   *
   * @param pixelRatio the custom pixel ratio of the map under construction
   * @return This
   */
  public MapboxMapOptions pixelRatio(float pixelRatio) {
    this.pixelRatio = pixelRatio;
    return this;
  }

  /**
   * Check whether tile pre-fetching is enabled.
   *
   * @return true if enabled
   */
  public boolean getPrefetchesTiles() {
    return prefetchesTiles;
  }


  /**
   * Set the flag to render the map surface on top of another surface.
   *
   * @param renderOnTop true if this map is shown on top of another one, false if bottom.
   */
  public void renderSurfaceOnTop(boolean renderOnTop) {
    this.zMediaOverlay = renderOnTop;
  }

  /**
   * Get the flag to render the map surface on top of another surface.
   *
   * @return true if this map is
   */
  public boolean getRenderSurfaceOnTop() {
    return zMediaOverlay;
  }

  /**
   * Get the current configured API endpoint base URL.
   *
   * @return Base URL to be used API endpoint.
   */
  public String getApiBaseUrl() {
    return apiBaseUrl;
  }

  /**
   * Get the current configured initial camera position for a map view.
   *
   * @return CameraPosition to be initially used.
   */
  public CameraPosition getCamera() {
    return cameraPosition;
  }

  /**
   * Get the current configured min zoom for a map view.
   *
   * @return Mininum zoom level to be used.
   */
  public double getMinZoomPreference() {
    return minZoom;
  }

  /**
   * Get the current configured maximum zoom for a map view.
   *
   * @return Maximum zoom to be used.
   */
  public double getMaxZoomPreference() {
    return maxZoom;
  }

  /**
   * Get the current configured visibility state for mapbox_compass_icon for a map view.
   *
   * @return Visibility state of the mapbox_compass_icon
   */
  public boolean getCompassEnabled() {
    return compassEnabled;
  }

  /**
   * Get the current configured gravity state for mapbox_compass_icon for a map view.
   *
   * @return Gravity state of the mapbox_compass_icon
   */
  public int getCompassGravity() {
    return compassGravity;
  }

  /**
   * Get the current configured margins for mapbox_compass_icon for a map view.
   *
   * @return Margins state of the mapbox_compass_icon
   */
  public int[] getCompassMargins() {
    return compassMargins;
  }

  /**
   * Get the current configured state for fading the mapbox_compass_icon when facing north.
   *
   * @return True if mapbox_compass_icon fades to invisible when facing north
   */
  public boolean getCompassFadeFacingNorth() {
    return fadeCompassFacingNorth;
  }

  /**
   * Get the current configured CompassView image.
   *
   * @return the drawable used as compass image
   */
  public Drawable getCompassImage() {
    return compassImage;
  }

  /**
   * Get the current configured style url for a map view.
   *
   * @return Style url to be used.
   */
  public String getStyle() {
    return style;
  }

  /**
   * Get the current configured rotate gesture state for a map view.
   *
   * @return True indicates gesture is enabled
   */
  public boolean getRotateGesturesEnabled() {
    return rotateGesturesEnabled;
  }

  /**
   * Get the current configured scroll gesture state for a map view.
   *
   * @return True indicates gesture is enabled
   */
  public boolean getScrollGesturesEnabled() {
    return scrollGesturesEnabled;
  }

  /**
   * Get the current configured tilt gesture state for a map view.
   *
   * @return True indicates gesture is enabled
   */
  public boolean getTiltGesturesEnabled() {
    return tiltGesturesEnabled;
  }

  /**
   * Get the current configured zoom controls state for a map view.
   *
   * @return True indicates gesture is enabled
   */
  public boolean getZoomControlsEnabled() {
    return zoomControlsEnabled;
  }

  /**
   * Get the current configured zoom gesture state for a map view.
   *
   * @return True indicates gesture is enabled
   */
  public boolean getZoomGesturesEnabled() {
    return zoomGesturesEnabled;
  }

  /**
   * Get the current configured double tap gesture state for a map view.
   *
   * @return True indicates gesture is enabled
   */
  public boolean getDoubleTapGesturesEnabled() {
    return doubleTapGesturesEnabled;
  }

  /**
   * Get the current configured debug state for a map view.
   *
   * @return True indicates debug is enabled.
   */
  public boolean getDebugActive() {
    return debugActive;
  }

  /**
   * Returns true if TextureView is being used the render view.
   *
   * @return True if TextureView is used.
   */
  public boolean getTextureMode() {
    return textureMode;
  }

  public boolean getTranslucentTextureSurface() {
    return translucentTextureSurface;
  }

  /**
   * Returns the font-family for locally overriding generation of glyphs in the
   * &#x27;CJK Unified Ideographs&#x27; and &#x27;Hangul Syllables&#x27; ranges.
   *
   * @return Local ideograph font family name.
   */
  public String getLocalIdeographFontFamily() {
    return localIdeographFontFamily;
  }

  /**
   * Return the custom configured pixel ratio, returns 0 if not configured.
   *
   * @return the pixel ratio used by the map under construction
   */
  public float getPixelRatio() {
    return pixelRatio;
  }

  public static final Parcelable.Creator<MapboxMapOptions> CREATOR = new Parcelable.Creator<MapboxMapOptions>() {
    public MapboxMapOptions createFromParcel(Parcel in) {
      return new MapboxMapOptions(in);
    }

    public MapboxMapOptions[] newArray(int size) {
      return new MapboxMapOptions[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(cameraPosition, flags);
    dest.writeByte((byte) (debugActive ? 1 : 0));

    dest.writeByte((byte) (compassEnabled ? 1 : 0));
    dest.writeInt(compassGravity);
    dest.writeIntArray(compassMargins);
    dest.writeByte((byte) (fadeCompassFacingNorth ? 1 : 0));
    dest.writeParcelable(compassImage != null
      ? BitmapUtils.getBitmapFromDrawable(compassImage) : null, flags);

    dest.writeDouble(minZoom);
    dest.writeDouble(maxZoom);

    dest.writeByte((byte) (rotateGesturesEnabled ? 1 : 0));
    dest.writeByte((byte) (scrollGesturesEnabled ? 1 : 0));
    dest.writeByte((byte) (tiltGesturesEnabled ? 1 : 0));
    dest.writeByte((byte) (zoomControlsEnabled ? 1 : 0));
    dest.writeByte((byte) (zoomGesturesEnabled ? 1 : 0));
    dest.writeByte((byte) (doubleTapGesturesEnabled ? 1 : 0));

    dest.writeString(style);
    dest.writeString(apiBaseUrl);
    dest.writeByte((byte) (textureMode ? 1 : 0));
    dest.writeByte((byte) (translucentTextureSurface ? 1 : 0));
    dest.writeByte((byte) (prefetchesTiles ? 1 : 0));
    dest.writeByte((byte) (zMediaOverlay ? 1 : 0));
    dest.writeString(localIdeographFontFamily);
    dest.writeFloat(pixelRatio);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MapboxMapOptions options = (MapboxMapOptions) o;

    if (debugActive != options.debugActive) {
      return false;
    }
    if (compassEnabled != options.compassEnabled) {
      return false;
    }
    if (fadeCompassFacingNorth != options.fadeCompassFacingNorth) {
      return false;
    }
    if (compassImage != null
      ? !compassImage.equals(options.compassImage)
      : options.compassImage != null) {
      return false;
    }
    if (compassGravity != options.compassGravity) {
      return false;
    }
    if (Double.compare(options.minZoom, minZoom) != 0) {
      return false;
    }
    if (Double.compare(options.maxZoom, maxZoom) != 0) {
      return false;
    }
    if (rotateGesturesEnabled != options.rotateGesturesEnabled) {
      return false;
    }
    if (scrollGesturesEnabled != options.scrollGesturesEnabled) {
      return false;
    }
    if (tiltGesturesEnabled != options.tiltGesturesEnabled) {
      return false;
    }
    if (zoomGesturesEnabled != options.zoomGesturesEnabled) {
      return false;
    }
    if (zoomControlsEnabled != options.zoomControlsEnabled) {
      return false;
    }
    if (doubleTapGesturesEnabled != options.doubleTapGesturesEnabled) {
      return false;
    }
    if (cameraPosition != null ? !cameraPosition.equals(options.cameraPosition) : options.cameraPosition != null) {
      return false;
    }
    if (!Arrays.equals(compassMargins, options.compassMargins)) {
      return false;
    }
    if (style != null ? !style.equals(options.style) : options.style != null) {
      return false;
    }
    if (apiBaseUrl != null ? !apiBaseUrl.equals(options.apiBaseUrl) : options.apiBaseUrl != null) {
      return false;
    }
    if (prefetchesTiles != options.prefetchesTiles) {
      return false;
    }
    if (zMediaOverlay != options.zMediaOverlay) {
      return false;
    }
    if (!localIdeographFontFamily.equals(options.localIdeographFontFamily)) {
      return false;
    }
    if (pixelRatio != options.pixelRatio) {
      return false;
    }

    return false;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = cameraPosition != null ? cameraPosition.hashCode() : 0;
    result = 31 * result + (debugActive ? 1 : 0);
    result = 31 * result + (compassEnabled ? 1 : 0);
    result = 31 * result + (fadeCompassFacingNorth ? 1 : 0);
    result = 31 * result + compassGravity;
    result = 31 * result + (compassImage != null ? compassImage.hashCode() : 0);
    result = 31 * result + Arrays.hashCode(compassMargins);
    temp = Double.doubleToLongBits(minZoom);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(maxZoom);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (rotateGesturesEnabled ? 1 : 0);
    result = 31 * result + (scrollGesturesEnabled ? 1 : 0);
    result = 31 * result + (tiltGesturesEnabled ? 1 : 0);
    result = 31 * result + (zoomGesturesEnabled ? 1 : 0);
    result = 31 * result + (zoomControlsEnabled ? 1 : 0);
    result = 31 * result + (doubleTapGesturesEnabled ? 1 : 0);
    result = 31 * result + (apiBaseUrl != null ? apiBaseUrl.hashCode() : 0);
    result = 31 * result + (textureMode ? 1 : 0);
    result = 31 * result + (translucentTextureSurface ? 1 : 0);
    result = 31 * result + (style != null ? style.hashCode() : 0);
    result = 31 * result + (prefetchesTiles ? 1 : 0);
    result = 31 * result + (zMediaOverlay ? 1 : 0);
    result = 31 * result + (localIdeographFontFamily != null ? localIdeographFontFamily.hashCode() : 0);
    result = 31 * result + (int) pixelRatio;
    return result;
  }
}
