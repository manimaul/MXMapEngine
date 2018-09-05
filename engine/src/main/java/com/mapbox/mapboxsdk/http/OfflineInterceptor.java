package com.mapbox.mapboxsdk.http;

import android.net.Uri;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface OfflineInterceptor {

    void cancel(Uri uri);

    /**
     * An observable that emits a single response for a given {@link Uri}.
     *
     * @param uri the uri to handle the request.
     * @return an observable of a request response.
     */
    Single<byte[]> handleRequest(Uri uri);

    /**
     * The name of the host to delegate request handling to. E.g. "localhost" as in http://localhost
     *
     * @return the name of the host.
     */
    String host();
}