cmake_minimum_required(VERSION 3.4.1)

set(MB_DIR ${CMAKE_CURRENT_LIST_DIR}/mapbox)
file(GLOB SOURCE_SET ${MB_DIR}/parsedate/*.c ${MB_DIR}/csscolorparser/*.cpp)
set(PLATFORM android)

file(GLOB_RECURSE MBGL_SOURCE_SET
        ${MB_DIR}/mbgl/*.cpp
        ${MB_DIR}/mbgl/*.hpp)

file(GLOB_RECURSE PLATFORM_SET
        ${MB_DIR}/platform/default/*.cpp
        ${MB_DIR}/platform/${PLATFORM}/*.cpp)

message(${PLATFORM_SET})

add_library(mbgl STATIC ${SOURCE_SET} ${MBGL_SOURCE_SET} ${PLATFORM_SET})

target_include_directories(mbgl PUBLIC ${MB_DIR} ${MB_DIR}/include)
include_directories(mbgl_platform PUBLIC ${MB_DIR}/platform/${PLATFORM} ${MB_DIR}/platform/default)
