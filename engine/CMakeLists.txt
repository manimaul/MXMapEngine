# documentation: https://d.android.com/studio/projects/add-native-code.html
cmake_minimum_required(VERSION 3.4.1)

#set(CMAKE_CXX_STANDARD 14)
add_definitions(-DMBGL_USE_GLES2=1)

set(LIB_DIR ${CMAKE_CURRENT_LIST_DIR}/src/main/cpp/libs)

# header only dependencies
include(${LIB_DIR}/headerlibConfig.cmake)
inc_header_lib(vector-tile:1.0.1)
inc_header_lib(boost:1.65.1)
inc_header_lib(cheap-ruler:2.5.6)
inc_header_lib(earcut:0.12.4)
inc_header_lib(geojson:0.4.2)
inc_header_lib(geojsonvt:6.3.0)
inc_header_lib(geometry:0.9.2)
inc_header_lib(jni.hpp:3.0.0)
inc_header_lib(kdbush:0.1.1-1)
inc_header_lib(pixelmatch:0.10.0)
inc_header_lib(polylabel:1.0.3)
inc_header_lib(protozero:1.5.2)
inc_header_lib(rapidjson:1.1.0)
inc_header_lib(shelf-pack:2.1.1)
inc_header_lib(supercluster:0.2.2)
inc_header_lib(unique_resource:cba309e)
inc_header_lib(variant:1.1.4)
inc_header_lib(vector-tile:1.0.1)
inc_header_lib(wagyu:0.4.3)

# icu
set(icu_DIR ${LIB_DIR})
find_package(icu REQUIRED)

# mbgl
set(mbgl_DIR ${LIB_DIR})
find_package(mbgl REQUIRED)

# sqlite
set(sqlite_DIR ${LIB_DIR})
find_package(sqlite REQUIRED)

add_library( # Sets the name of the library.
             mapbox-gl

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

find_library(log-lib log)

add_dependencies(icu mapbox-gl)

target_link_libraries(mapbox-gl
        #PRIVATE nunicode todo: (WK)
        PRIVATE mbgl
        PUBLIC -llog
        PUBLIC -landroid
        PUBLIC -ljnigraphics
        PUBLIC -lEGL
        PUBLIC -lGLESv2
        PUBLIC -lstdc++
        PUBLIC -latomic
        PUBLIC -lz
    )
