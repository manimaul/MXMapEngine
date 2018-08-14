#include "lat_lng.hpp"

namespace mbgl {
namespace android {

jni::Object<LatLng> LatLng::New(jni::JNIEnv& env, double latitude, double longitude) {
    static auto constructor = LatLng::javaClass.GetConstructor<double, double>(env);
    return LatLng::javaClass.New(env, constructor, latitude, longitude);
}

mbgl::Point<double> LatLng::getGeometry(jni::JNIEnv& env, jni::Object<LatLng> latLng) {
    static auto latitudeField = LatLng::javaClass.GetField<jni::jdouble>(env, "latitude");
    static auto longitudeField = LatLng::javaClass.GetField<jni::jdouble>(env, "longitude");
    return mbgl::Point<double>(latLng.Get(env, longitudeField), latLng.Get(env, latitudeField));
}

mbgl::LatLng LatLng::getLatLng(jni::JNIEnv& env, jni::Object<LatLng> latLng) {
    auto point = LatLng::getGeometry(env, latLng);
    return mbgl::LatLng(point.y, point.x);
}

void LatLng::setLatitude(jni::JNIEnv& env, jni::Object<LatLng> latLng, double latitude) {
    static auto field = LatLng::javaClass.GetField<double>(env, "latitude");
    latLng.Set(env, field, latitude);
}

void LatLng::setLongitude(jni::JNIEnv& env, jni::Object<LatLng> latLng, double longitude) {
    static auto field = LatLng::javaClass.GetField<double>(env, "longitude");
    latLng.Set(env, field, longitude);
}

void LatLng::registerNative(jni::JNIEnv& env) {
    // Lookup the class
    LatLng::javaClass = *jni::Class<LatLng>::Find(env).NewGlobalRef(env).release();
}

jni::Class<LatLng> LatLng::javaClass;


} // namespace android
} // namespace mbgl