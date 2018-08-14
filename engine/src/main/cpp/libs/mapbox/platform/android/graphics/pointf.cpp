#include "pointf.hpp"

namespace mbgl {
namespace android {

jni::Object<PointF> PointF::New(jni::JNIEnv& env, float x, float y) {
    static auto constructor = PointF::javaClass.GetConstructor<float, float>(env);
    return PointF::javaClass.New(env, constructor, x, y);
}

void PointF::setX(jni::JNIEnv& env, jni::Object<PointF> jPointF, float value) {
    static auto field = PointF::javaClass.GetField<float>(env, "x");
    jPointF.Set(env, field, value);
}

void PointF::setY(jni::JNIEnv& env, jni::Object<PointF> jPointF, float value) {
    static auto field = PointF::javaClass.GetField<float>(env, "y");
    jPointF.Set(env, field, value);
}

void PointF::registerNative(jni::JNIEnv& env) {
    // Lookup the class
    PointF::javaClass = *jni::Class<PointF>::Find(env).NewGlobalRef(env).release();
}

jni::Class<PointF> PointF::javaClass;


} // namespace android
} // namespace mbgl