cmake_minimum_required(VERSION 3.4.1)

macro(target_inc_sqlite _TARGET _VISIBILITY)
    set(SQLITE_SRC_DIR ${SRC_ROOT}/libs/sqlite-amalgamation/)
    add_library(sqlite STATIC ${SQLITE_SRC_DIR}/sqlite3.c)

    #target_include_directories(sqlite PUBLIC ${SQLITE_SRC_DIR})
    target_include_directories(${_TARGET} ${_VISIBILITY} ${SQLITE_SRC_DIR})
endmacro()
