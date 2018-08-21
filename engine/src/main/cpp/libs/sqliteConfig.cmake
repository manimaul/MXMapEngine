cmake_minimum_required(VERSION 3.4.1)

set(SQLITE_SRC_DIR ${CMAKE_CURRENT_LIST_DIR}/sqlite-amalgamation/)

add_library(sqlite STATIC
        ${SQLITE_SRC_DIR}/sqlite3.c)
target_include_directories(sqlite PUBLIC ${SQLITE_SRC_DIR})
