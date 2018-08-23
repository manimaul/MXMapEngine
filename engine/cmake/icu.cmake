cmake_minimum_required(VERSION 3.4.1)

message("android abi ${ANDROID_ABI}")
# armeabi armeabi-v7a arm64-v8a x86 x86-64

add_library(icu STATIC IMPORTED)
set_property(TARGET icu PROPERTY IMPORTED_LOCATION ${SRC_ROOT}/libs/icu/${ANDROID_ABI}/58.1-min-size/libicuuc.a)

include_directories(${SRC_ROOT}/libs/icu/${ANDROID_ABI}/58.1-min-size/include)
