package com.mapbox.mapboxsdk.http;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.utils.Logger;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.SSLException;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;
import static android.util.Log.WARN;

public class HTTPRequest implements Callback {
  private static final String TAG = HTTPRequest.class.getSimpleName();
  private static final int CONNECTION_ERROR = 0;
  private static final int TEMPORARY_ERROR = 1;
  private static final int PERMANENT_ERROR = 2;
  public static final int OFFLINE_RESPONSE_CODE = 200;
  public static final int OFFLINE_FAILURE_CODE = 404;
  public static final String UNKNOWN_ERROR = "unknown error";
  public static final String OFFLINE_INTERCEPTOR_NULL_OBSERVABLE = "offline interceptor produced a null observable";

  /**
   * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cache-Control
   * The Cache-Control general-header field is used to specify directives for caching mechanisms in both,
   * requests and responses. Caching directives are unidirectional, meaning that a given directive
   * in a request is not implying that the same directive is to be given in the response.
   *
   * Cache-Control: max-age=<seconds>
   *     Specifies the maximum amount of time a resource will be considered fresh. Contrary to Expires,
   *     this directive is relative to the time of the request.
   * Cache-Control: s-maxage=<seconds>
   *     Overrides max-age or the Expires header, but it only applies to shared caches (e.g., proxies)
   *     and is ignored by a private cache.
   */
  public static final String OFFLINE_RESPONSE_CACHE_CONTROL = String.format(Locale.US, "max-age=%d", 30);

  /**
   * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Expires
   * The Expires header contains the date/time after which the response is considered stale.
   *
   * Invalid dates, like the value 0, represent a date in the past and mean that the resource is already expired.
   *
   * If there is a Cache-Control header with the "max-age" or "s-max-age" directive in the response,
   * the Expires header is ignored.
   */
  public static final String OFFLINE_RESPONSE_CACHE_EXPIRES = null;

  private static MessageDigest sMessageDigest;
  static {
    try {
      sMessageDigest = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
      Logger.e(TAG, e);
    }
  }

  private static final Handler sHandler = new Handler();
  private static final Scheduler sScheduler = AndroidSchedulers.from(sHandler.getLooper());


  private static OkHttpClient client = new OkHttpClient.Builder().dispatcher(getDispatcher()).build();
  private static boolean logEnabled = true;
  private static boolean logRequestUrl = false;

  // Reentrancy is not needed, but "Lock" is an abstract class.
  private ReentrantLock lock = new ReentrantLock();
  private String userAgentString;
  private long nativePtr = 0;
  private Call call;
  private static OfflineInterceptor sOfflineInterceptor;
  private Uri mResourceUrl;

  private HTTPRequest(long nativePtr, String resourceUrl, String etag, String modified) {
    this.nativePtr = nativePtr;

    mResourceUrl = Uri.parse(resourceUrl);
    if (sOfflineInterceptor != null && mResourceUrl.getHost().equals(sOfflineInterceptor.host())) {
      initializeOfflineRequest();
    } else if (resourceUrl.startsWith("local://")) {
      // used by render test to serve files from assets
      executeLocalRequest(resourceUrl);
    } else {
      executeRequest(resourceUrl, etag, modified);
    }
  }

  public void cancel() {
    // call can be null if the constructor gets aborted (e.g, under a NoRouteToHostException).
    if (call == null) {
      if (sOfflineInterceptor != null) {
        sOfflineInterceptor.cancel(mResourceUrl);
      }
    } else {
      call.cancel();
    }

    // TODO: We need a lock here because we can try
    // to cancel at the same time the request is getting
    // answered on the OkHTTP thread. We could get rid of
    // this lock by using Runnable when we move Android
    // implementation of mbgl::RunLoop to Looper.
    lock.lock();
    nativePtr = 0;
    lock.unlock();
  }

  @Override
  public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
    if (response.isSuccessful()) {
      log(VERBOSE, String.format("[HTTP] Request was successful (code = %s).", response.code()));
    } else {
      // We don't want to call this unsuccessful because a 304 isn't really an error
      String message = !TextUtils.isEmpty(response.message()) ? response.message() : "No additional information";
      log(DEBUG, String.format("[HTTP] Request with response code = %s: %s", response.code(), message));
    }

    ResponseBody responseBody = response.body();
    if (responseBody == null) {
      log(ERROR, "[HTTP] Received empty response body");
      return;
    }

    byte[] body;
    try {
      body = responseBody.bytes();
    } catch (IOException ioException) {
      onFailure(call, ioException);
      // throw ioException;
      return;
    } finally {
      response.close();
    }

    lock.lock();
    if (nativePtr != 0) {
      nativeOnResponse(response.code(),
        response.header("ETag"),
        response.header("Last-Modified"),
        response.header("Cache-Control"),
        response.header("Expires"),
        response.header("Retry-After"),
        response.header("x-rate-limit-reset"),
        body);
    }
    lock.unlock();
  }

  @Override
  public void onFailure(@NonNull Call call, @NonNull IOException e) {
    handleFailure(call, e);
  }

  static void enableLog(boolean enabled) {
    logEnabled = enabled;
  }

  static void enablePrintRequestUrlOnFailure(boolean enabled) {
    logRequestUrl = enabled;
  }

  static void setOKHttpClient(OkHttpClient client) {
    HTTPRequest.client = client;
  }

  public static void setOfflineInterceptor(@Nullable OfflineInterceptor offlineInterceptor) {
    sOfflineInterceptor = offlineInterceptor;
  }

  private static Dispatcher getDispatcher() {
    Dispatcher dispatcher = new Dispatcher();
    // Matches core limit set on
    // https://github.com/mapbox/mapbox-gl-native/blob/master/platform/android/src/http_file_source.cpp#L192
    dispatcher.setMaxRequestsPerHost(20);
    return dispatcher;
  }

  private void executeRequest(String resourceUrl, String etag, String modified) {
    try {
      HttpUrl httpUrl = HttpUrl.parse(resourceUrl);
      if (httpUrl == null) {
        log(Log.ERROR, String.format("[HTTP] Unable to parse resourceUrl %s", resourceUrl));
      }

      final String host = httpUrl.host().toLowerCase(MapboxConstants.MAPBOX_LOCALE);
      // Don't try a request to remote server if we aren't connected
      if (!Mapbox.isConnected() && !host.equals("127.0.0.1") && !host.equals("localhost")) {
        throw new NoRouteToHostException("No Internet connection available.");
      }

      if (host.equals("mapbox.com") || host.endsWith(".mapbox.com") || host.equals("mapbox.cn")
        || host.endsWith(".mapbox.cn")) {
        if (httpUrl.querySize() == 0) {
          resourceUrl = resourceUrl + "?";
        } else {
          resourceUrl = resourceUrl + "&";
        }
        resourceUrl = resourceUrl + "events=true";
      }

      Request.Builder builder = new Request.Builder()
        .url(resourceUrl)
        .tag(resourceUrl.toLowerCase(MapboxConstants.MAPBOX_LOCALE))
        .addHeader("User-Agent", getUserAgent());
      if (etag.length() > 0) {
        builder = builder.addHeader("If-None-Match", etag);
      } else if (modified.length() > 0) {
        builder = builder.addHeader("If-Modified-Since", modified);
      }
      Request request = builder.build();
      call = client.newCall(request);
      call.enqueue(this);
    } catch (Exception exception) {
      handleFailure(call, exception);
    }
  }

  private void executeLocalRequest(String resourceUrl) {
    new LocalRequestTask(new LocalRequestTask.OnLocalRequestResponse() {
      @Override
      public void onResponse(byte[] bytes) {
        if (bytes != null) {
          lock.lock();
          if (nativePtr != 0) {
            nativeOnResponse(200, null, null, null, null, null, null, bytes);
          }
          lock.unlock();
        }
      }
    }).execute(resourceUrl);
  }

  private void handleFailure(Call call, Exception e) {
    String errorMessage = e.getMessage() != null ? e.getMessage() : "Error processing the request";
    int type = getFailureType(e);

    if (logEnabled && call != null && call.request() != null) {
      String requestUrl = call.request().url().toString();
      logFailure(type, errorMessage, requestUrl);
    }

    lock.lock();
    if (nativePtr != 0) {
      nativeOnFailure(type, errorMessage);
    }
    lock.unlock();
  }

  public String makeModified() {
    return DateFormat.format("EEE, dd MMM yyyy HH:mm:ss zzz", System.currentTimeMillis()).toString();
  }

  public synchronized String makeEtag(byte[] body) {
    assert sMessageDigest != null;
    sMessageDigest.reset();
    return Base64.encodeToString(sMessageDigest.digest(body), Base64.DEFAULT);
  }

  public void onOfflineResponse(byte[] body) {
    lock.lock();
    if (nativePtr != 0) {
      String etag = makeEtag(body);
      String mod = makeModified();
      nativeOnResponse(OFFLINE_RESPONSE_CODE,
              etag,
              mod,
              OFFLINE_RESPONSE_CACHE_CONTROL,
              OFFLINE_RESPONSE_CACHE_EXPIRES,
              null,
              null,
              body);
    }
    lock.unlock();
  }

  public void onOfflineFailure(final String message) {
    lock.lock();
    if (nativePtr != 0) {
      nativeOnFailure(OFFLINE_FAILURE_CODE, message);
    }
    lock.unlock();
  }

  private int getFailureType(Exception e) {
    if ((e instanceof NoRouteToHostException) || (e instanceof UnknownHostException) || (e instanceof SocketException)
      || (e instanceof ProtocolException) || (e instanceof SSLException)) {
      return CONNECTION_ERROR;
    } else if ((e instanceof InterruptedIOException)) {
      return TEMPORARY_ERROR;
    }
    return PERMANENT_ERROR;
  }

  private void log(int type, String errorMessage) {
    if (logEnabled) {
      switch (type) {
        case Log.VERBOSE:
          Log.v(TAG, errorMessage);
          break;
        case Log.DEBUG:
          Logger.d(TAG, errorMessage);
          break;
        case Log.INFO:
          Log.i(TAG, errorMessage);
          break;
        case Log.WARN:
          Log.w(TAG, errorMessage);
          break;
        case Log.ERROR:
          Log.e(TAG, errorMessage);
          break;
      }
    }
  }

  private void logFailure(int type, String errorMessage, String requestUrl) {
    log(type == TEMPORARY_ERROR ? DEBUG : type == CONNECTION_ERROR ? INFO : WARN,
      String.format(
        "Request failed due to a %s error: %s %s",
        type == TEMPORARY_ERROR ? "temporary" : type == CONNECTION_ERROR ? "connection" : "permanent",
        errorMessage,
        logRequestUrl ? requestUrl : ""
      )
    );
  }

  private String getUserAgent() {
    if (userAgentString == null) {
      userAgentString = getApplicationIdentifier();
//      userAgentString = TelemetryUtils.toHumanReadableAscii(
//        String.format("%s %s (%s) Android/%s (%s)",
//          getApplicationIdentifier(),
//          BuildConfig.MAPBOX_VERSION_STRING,
//          BuildConfig.GIT_REVISION_SHORT,
//          Build.VERSION.SDK_INT,
//          Build.CPU_ABI)
//      );
    }
    return userAgentString;
  }

  private String getApplicationIdentifier() {
    try {
      Context context = Mapbox.getApplicationContext();
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return String.format("%s/%s (%s)", context.getPackageName(), packageInfo.versionName, packageInfo.versionCode);
    } catch (Exception exception) {
      return "";
    }
  }

  private void initializeOfflineRequest() {
    Single<byte[]> responseObservable = sOfflineInterceptor.handleRequest(mResourceUrl);
    if (responseObservable != null) {
      responseObservable
              .observeOn(sScheduler)
              .subscribe(new SingleObserver<byte[]>() {

                @Override
                public void onError(Throwable e) {
                  final String message = e.getMessage();
                  onOfflineFailure(message == null ? UNKNOWN_ERROR : message);
                }

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(byte[] bytes) {
                  onOfflineResponse(bytes);
                }
              });
    } else {
      onOfflineFailure(OFFLINE_INTERCEPTOR_NULL_OBSERVABLE);
    }
  }

  private native void nativeOnFailure(int type, String message);

  private native void nativeOnResponse(int code, String etag, String modified, String cacheControl, String expires,
                                       String retryAfter, String xRateLimitReset, byte[] body);
}
