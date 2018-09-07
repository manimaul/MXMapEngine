#pragma once

#include <mbgl/util/noncopyable.hpp>
#include <mbgl/util/geo.hpp>
#include <mbgl/util/geometry.hpp>

#include <jni/jni.hpp>

namespace mbgl {
namespace android {

class LatLngBounds : private mbgl::util::noncopyable {
public:

    static constexpr auto Name() { return "com/mapbox/mapboxsdk/geometry/LatLngBounds"; };

    static jni::Object<LatLngBounds> New(jni::JNIEnv&, mbgl::LatLngBounds);

    static mbgl::LatLngBounds getLatLngBounds(jni::JNIEnv&, jni::Object<LatLngBounds>);

    static void setLatitudeNorth(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds, double const value);

    static void setLatitudeSouth(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds, double const value);

    static void setLongitudeEast(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds, double const value);

    static void setLongitudeWest(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds, double const value);

    static jni::Class<LatLngBounds> javaClass;

    static void registerNative(jni::JNIEnv&);

};


} // namespace android
} // namespace mbgl