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

include(cmake/headerlib.cmake)
include(cmake/config.cmake)

if (COMMAND mbgl_filesource)
    include(cmake/filesource.cmake)
endif()

include(cmake/core-files.cmake)
include(cmake/core.cmake)
