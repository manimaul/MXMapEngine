cmake_minimum_required(VERSION 3.4.1)

set(MB_DIR ${CMAKE_CURRENT_LIST_DIR}/mapbox)
file(GLOB SOURCE_SET ${MB_DIR}/parsedate/*.c ${MB_DIR}/csscolorparser/*.cpp)
set(PLATFORM android)

# sqlite
set(sqlite_DIR ${CMAKE_CURRENT_LIST_DIR})
find_package(sqlite REQUIRED)

file(GLOB_RECURSE MBGL_SOURCE_SET
        ${MB_DIR}/platform/default/*.cpp
        ${MB_DIR}/platform/${PLATFORM}/*.cpp
        ${MB_DIR}/mbgl/*.cpp
        ${MB_DIR}/mbgl/*.hpp)

list(REMOVE_ITEM MBGL_SOURCE_SET ${MB_DIR}/platform/default/collator.cpp
    ${MB_DIR}/platform/default/http_file_source.cpp
    ${MB_DIR}/platform/default/jpeg_reader.cpp
    ${MB_DIR}/platform/default/headless_backend_osmesa.cpp
    ${MB_DIR}/platform/default/async_task.cpp
    ${MB_DIR}/platform/default/png_reader.cpp
    ${MB_DIR}/platform/default/run_loop.cpp
    ${MB_DIR}/platform/default/timer.cpp
    ${MB_DIR}/platform/default/string_stdlib.cpp
    ${MB_DIR}/platform/default/thread.cpp
    ${MB_DIR}/platform/default/unaccent.cpp
    ${MB_DIR}/platform/default/webp_reader.cpp
)

add_library(mbgl STATIC ${SOURCE_SET} ${MBGL_SOURCE_SET})

target_link_libraries(mbgl sqlite)
target_include_directories(mbgl PUBLIC ${MB_DIR} ${MB_DIR}/include ${ICU_SRC_DIR}/include)
include_directories(mbgl PUBLIC ${MB_DIR}/platform/${PLATFORM} ${MB_DIR}/platform/default)
target_compile_definitions(mbgl PRIVATE RAPIDJSON_HAS_STDSTRING=1 MBGL_VERSION_REV="r1")
