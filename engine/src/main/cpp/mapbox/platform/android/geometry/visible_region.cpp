#include "visible_region.hpp"

namespace mbgl {
namespace android {

jni::Object<LatLng> VisibleRegion::getFarLeft(jni::JNIEnv& env, jni::Object<VisibleRegion> visibleRegion) {
    static auto field = VisibleRegion::javaClass.GetField<jni::Object<LatLng>>(env, "farLeft");
    return visibleRegion.Get(env, field);
}

jni::Object<LatLng> VisibleRegion::getFarRight(jni::JNIEnv& env, jni::Object<VisibleRegion> visibleRegion) {
    static auto field = VisibleRegion::javaClass.GetField<jni::Object<LatLng>>(env, "farRight");
    return visibleRegion.Get(env, field);
}

jni::Object<LatLng> VisibleRegion::getNearLeft(jni::JNIEnv& env, jni::Object<VisibleRegion> visibleRegion) {
    static auto field = VisibleRegion::javaClass.GetField<jni::Object<LatLng>>(env, "nearLeft");
    return visibleRegion.Get(env, field);
}

jni::Object<LatLng> VisibleRegion::getNearRight(jni::JNIEnv& env, jni::Object<VisibleRegion> visibleRegion) {
    static auto field = VisibleRegion::javaClass.GetField<jni::Object<LatLng>>(env, "nearRight");
    return visibleRegion.Get(env, field);
}

jni::Object<LatLngBounds> VisibleRegion::getBounds(jni::JNIEnv& env, jni::Object<VisibleRegion> visibleRegion) {
    static auto field = VisibleRegion::javaClass.GetField<jni::Object<LatLngBounds>>(env, "latLngBounds");
    return visibleRegion.Get(env, field);
}

void VisibleRegion::registerNative(jni::JNIEnv& env) {
    // Lookup the class
    VisibleRegion::javaClass = *jni::Class<VisibleRegion>::Find(env).NewGlobalRef(env).release();
}

jni::Class<VisibleRegion> VisibleRegion::javaClass;


} // namespace android
} // namespace mbgl
