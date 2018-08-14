#pragma once

#include <mbgl/util/noncopyable.hpp>
#include <jni/jni.hpp>
#include "lat_lng.hpp"
#include "lat_lng_bounds.hpp"

namespace mbgl {
namespace android {

class VisibleRegion : private mbgl::util::noncopyable {
public:

    static constexpr auto Name() { return "com/mapbox/mapboxsdk/geometry/VisibleRegion"; };

    static jni::Object<LatLng> getFarLeft(jni::JNIEnv&, jni::Object<VisibleRegion>);

    static jni::Object<LatLng> getFarRight(jni::JNIEnv&, jni::Object<VisibleRegion>);

    static jni::Object<LatLng> getNearLeft(jni::JNIEnv&, jni::Object<VisibleRegion>);

    static jni::Object<LatLng> getNearRight(jni::JNIEnv&, jni::Object<VisibleRegion>);

    static jni::Object<LatLngBounds> getBounds(jni::JNIEnv&, jni::Object<VisibleRegion>);

    static jni::Class<VisibleRegion> javaClass;

    static void registerNative(jni::JNIEnv&);

};


} // namespace android
} // namespace mbgl