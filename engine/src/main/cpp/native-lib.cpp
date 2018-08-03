#include <jni.h>
#include <string>


extern "C"
JNIEXPORT jstring JNICALL
Java_com_mxmariner_engine_MXEngine_stringFromJni(JNIEnv *env, jclass type) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}