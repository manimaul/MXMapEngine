# documentation: https://d.android.com/studio/projects/add-native-code.html
cmake_minimum_required(VERSION 3.4.1)

set(LIB_DIR ${CMAKE_CURRENT_LIST_DIR}/src/main/cpp/libs/)
set(kdbush_DIR ${LIB_DIR})
find_package(kdbush REQUIRED)
set_target_properties(kdbush PROPERTIES LINKER_LANGUAGE CXX)

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp )

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       kdbush)