# documentation: https://d.android.com/studio/projects/add-native-code.html
cmake_minimum_required(VERSION 3.4.1)
project(mbgl LANGUAGES CXX C)
set(CMAKE_CXX_STANDARD 14)

option(WITH_ERROR    "Add -Werror flag to build (turns warnings into errors)" ON)

set(MBGL_PLATFORM android)
message("CMAKE_SOURCE_DIR=" ${CMAKE_SOURCE_DIR})
set(SRC_ROOT ${CMAKE_SOURCE_DIR}/src/main/cpp)
message("SRC_ROOT=" ${SRC_ROOT})

add_definitions(-DMBGL_USE_GLES2=1)

if(EXISTS ${CMAKE_SOURCE_DIR}/../.git/HEAD)
    exec_program(
        "git"
        ${CMAKE_SOURCE_DIR}
        ARGS "rev-parse --short=8 HEAD"
        OUTPUT_VARIABLE MBGL_VERSION_REV )
else()
    set(MBGL_VERSION_REV 00000000)
endif()
message("MBGL_VERSION_REV=" ${MBGL_VERSION_REV})

set_source_files_properties(${SRC_ROOT}/mapbox/src/mbgl/util/version.cpp PROPERTIES COMPILE_DEFINITIONS MBGL_VERSION_REV="${MBGL_VERSION_REV}")
add_definitions(-DRAPIDJSON_HAS_STDSTRING=1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14 -ftemplate-depth=1024 -fPIC -fvisibility=hidden -Wall -Wextra -Wshadow -Wnon-virtual-dtor -Wno-variadic-macros -Wno-unknown-pragmas")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -fPIC -fvisibility=hidden -Wall -Wextra -Wshadow -Wno-variadic-macros -Wno-unknown-pragmas")
if (WITH_ERROR)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Werror")
    set(CMAKE_C_FLAGS   "${CMAKE_C_FLAGS} -Werror")
endif()
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -Os -DNDEBUG")
set(CMAKE_CXX_FLAGS_RELWITHDEBINFO "${CMAKE_CXX_FLAGS_RELWITHDEBINFO} -O3 -DNDEBUG")
set(CMAKE_CXX_FLAGS_SANITIZE "${CMAKE_CXX_FLAGS_SANITIZE} -O1 -g -fno-omit-frame-pointer -fno-optimize-sibling-calls")

if(CMAKE_CXX_COMPILER_ID MATCHES ".*Clang")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wno-error=unknown-warning-option")
elseif(CMAKE_COMPILER_IS_GNUCXX)
    # https://svn.boost.org/trac/boost/ticket/9240
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fext-numeric-literals")
endif()

## todo(remove) ## if(NOT EXISTS ${CMAKE_SOURCE_DIR}/src/main/libs/mapbox/platform/${MBGL_PLATFORM}/config.cmake)
## todo(remove) ##     message(ERROR " Can't find config.cmake file for platform ${MBGL_PLATFORM}")
## todo(remove) ## endif()

include(cmake/headerlib.cmake)
include(cmake/config.cmake)

if (COMMAND mbgl_filesource)
    include(cmake/filesource.cmake)
endif()

include(cmake/core-files.cmake)
include(cmake/core.cmake)

## set(LIB_DIR ${CMAKE_CURRENT_LIST_DIR}/src/main/cpp/libs)
##
## # header only dependencies
## include(${LIB_DIR}/headerlibConfig.cmake)
## inc_header_lib(vector-tile:1.0.1)
## inc_header_lib(boost:1.65.1)
## inc_header_lib(cheap-ruler:2.5.6)
## inc_header_lib(earcut:0.12.4)
## inc_header_lib(geojson:0.4.2)
## inc_header_lib(geojsonvt:6.3.0)
## inc_header_lib(geometry:0.9.2)
## inc_header_lib(jni.hpp:3.0.0)
## inc_header_lib(kdbush:0.1.1-1)
## inc_header_lib(pixelmatch:0.10.0)
## inc_header_lib(polylabel:1.0.3)
## inc_header_lib(protozero:1.5.2)
## inc_header_lib(rapidjson:1.1.0)
## inc_header_lib(shelf-pack:2.1.1)
## inc_header_lib(supercluster:0.2.2)
## inc_header_lib(unique_resource:cba309e)
## inc_header_lib(variant:1.1.4)
## inc_header_lib(vector-tile:1.0.1)
## inc_header_lib(wagyu:0.4.3)
##
## # icu
## set(icu_DIR ${LIB_DIR})
## find_package(icu REQUIRED)
##
## # mbgl
## set(mbgl_DIR ${LIB_DIR})
## find_package(mbgl REQUIRED)
##
## # nunicode
## set(nunicode_DIR ${LIB_DIR})
## find_package(nunicode REQUIRED)
##
## add_library(mapbox-gl SHARED src/main/cpp/native-lib.cpp )
##
## find_library(log-lib log)
##
## add_dependencies(icu mapbox-gl)
##
## target_link_libraries(mapbox-gl
##         PRIVATE nunicode
##         PRIVATE mbgl
##         PUBLIC -llog
##         PUBLIC -landroid
##         PUBLIC -ljnigraphics
##         PUBLIC -lEGL
##         PUBLIC -lGLESv2
##         PUBLIC -lstdc++
##         PUBLIC -latomic
##         PUBLIC -lz
##     )
