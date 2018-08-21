cmake_minimum_required(VERSION 3.4.1)

set(MB_DIR ${CMAKE_CURRENT_LIST_DIR}/mapbox)
file(GLOB SOURCE_SET ${MB_DIR}/parsedate/*.c ${MB_DIR}/csscolorparser/*.cpp)
set(PLATFORM android)

file(GLOB_RECURSE MBGL_SOURCE_SET
        ${MB_DIR}/mbgl/*.cpp
        ${MB_DIR}/mbgl/*.hpp)

add_library(mbgl STATIC ${SOURCE_SET} ${MBGL_SOURCE_SET})

message("icu include dir" ${ICU_SRC_DIR}/include)
target_include_directories(mbgl PUBLIC ${MB_DIR} ${MB_DIR}/include ${ICU_SRC_DIR}/include)
include_directories(mbgl PUBLIC ${MB_DIR}/platform/${PLATFORM} ${MB_DIR}/platform/default)
target_compile_definitions(mbgl PRIVATE RAPIDJSON_HAS_STDSTRING=1 MBGL_VERSION_REV="r1")
