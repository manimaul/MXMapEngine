#pragma once

#include <mbgl/util/noncopyable.hpp>
#include <mbgl/util/geo.hpp>
#include <mbgl/util/geometry.hpp>

#include <jni/jni.hpp>

namespace mbgl {
namespace android {

class LatLng : private mbgl::util::noncopyable {
public:

    static constexpr auto Name() { return "com/mapbox/mapboxsdk/geometry/LatLng"; };

    static jni::Object<LatLng> New(jni::JNIEnv&, const mbgl::LatLng&);

    static mbgl::Point<double> getGeometry(jni::JNIEnv&, jni::Object<LatLng>);

    static mbgl::LatLng getLatLng(jni::JNIEnv&, jni::Object<LatLng>);

    static void setLatitude(jni::JNIEnv& env, jni::Object<LatLng> latLng, double const latitude);

    static void setLongitude(jni::JNIEnv& env, jni::Object<LatLng> latLng, double const longitude);

    static void setAltitude(jni::JNIEnv& env, jni::Object<LatLng> latLng, double const altitude);

    static jni::Class<LatLng> javaClass;

    static void registerNative(jni::JNIEnv&);

};


} // namespace android
} // namespace mbgl