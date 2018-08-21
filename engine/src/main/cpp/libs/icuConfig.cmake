cmake_minimum_required(VERSION 3.4.1)

set(ICU_SRC_DIR ${CMAKE_CURRENT_LIST_DIR}/icu/${ANDROID_ABI}/58.1-min-size)

message("android abi ${ANDROID_ABI}")
# armeabi armeabi-v7a arm64-v8a x86 x86-64

add_library(icu STATIC IMPORTED)
set_property(TARGET icu PROPERTY IMPORTED_LOCATION ${ICU_SRC_DIR}/lib/libicuuc.a)
#set(ICU_LIBRARIES ${ICU_SRC_DIR}/lib/libicuuc.a)

include_directories(${ICU_SRC_DIR}/include)
