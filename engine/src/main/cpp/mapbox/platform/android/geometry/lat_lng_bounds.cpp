#include "lat_lng_bounds.hpp"

namespace mbgl {
namespace android {

jni::Object<LatLngBounds> LatLngBounds::New(jni::JNIEnv& env, mbgl::LatLngBounds bounds) {
    static auto constructor = LatLngBounds::javaClass.GetConstructor<double, double, double, double>(env);
    return LatLngBounds::javaClass.New(env, constructor, bounds.north(), bounds.east(), bounds.south(), bounds.west());
}

mbgl::LatLngBounds LatLngBounds::getLatLngBounds(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds) {
    static auto swLatField = LatLngBounds::javaClass.GetField<jni::jdouble>(env, "latitudeSouth");
    static auto swLonField = LatLngBounds::javaClass.GetField<jni::jdouble>(env, "longitudeWest");
    static auto neLatField = LatLngBounds::javaClass.GetField<jni::jdouble>(env, "latitudeNorth");
    static auto neLonField = LatLngBounds::javaClass.GetField<jni::jdouble>(env, "longitudeEast");

    mbgl::LatLng sw = { bounds.Get(env, swLatField), bounds.Get(env, swLonField) };
    mbgl::LatLng ne = { bounds.Get(env, neLatField), bounds.Get(env, neLonField) };

    sw.unwrapForShortestPath(ne);

    return mbgl::LatLngBounds::hull(sw, ne);
}

void LatLngBounds::registerNative(jni::JNIEnv& env) {
    // Lookup the class
    LatLngBounds::javaClass = *jni::Class<LatLngBounds>::Find(env).NewGlobalRef(env).release();
}

void LatLngBounds::setLatitudeNorth(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds, double const value) {
    static auto field = LatLngBounds::javaClass.GetField<double>(env, "latitudeNorth");
    bounds.Set(env, field, value);
}

void LatLngBounds::setLatitudeSouth(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds, double const value) {
    static auto field = LatLngBounds::javaClass.GetField<double>(env, "latitudeSouth");
    bounds.Set(env, field, value);
}

void LatLngBounds::setLongitudeEast(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds, double const value) {
    static auto field = LatLngBounds::javaClass.GetField<double>(env, "longitudeEast");
    bounds.Set(env, field, value);
}

void LatLngBounds::setLongitudeWest(jni::JNIEnv& env, jni::Object<LatLngBounds> bounds, double const value) {
    static auto field = LatLngBounds::javaClass.GetField<double>(env, "longitudeWest");
    bounds.Set(env, field, value);
}

jni::Class<LatLngBounds> LatLngBounds::javaClass;


} // namespace android
} // namespace mbgl