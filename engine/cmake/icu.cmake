cmake_minimum_required(VERSION 3.4.1)

message("android abi ${ANDROID_ABI}")
# armeabi armeabi-v7a arm64-v8a x86 x86-64

set(ICU_SRC_DIR ${SRC_ROOT}/libs/icu/${ANDROID_ABI}/58.1-min-size)
add_library(icu STATIC IMPORTED)
set_target_properties(icu PROPERTIES IMPORTED_LOCATION ${ICU_SRC_DIR}/lib/libicuuc.a)
include_directories(${ICU_SRC_DIR}/include)
