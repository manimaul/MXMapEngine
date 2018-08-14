cmake_minimum_required(VERSION 3.4.1)

set(CUR_DIR ${CMAKE_CURRENT_LIST_DIR})

macro(inc_header_lib)
    string(REPLACE ":" " " ARGN ${ARGN})
    separate_arguments(ARGN)
    list(GET ARGN 0 _NAME)
    list(GET ARGN 1 _VERSION)
    message("including header only library name: " ${_NAME} " version: " ${_VERSION})
    set(_SRC_DIR "${CUR_DIR}/headers/${_NAME}/${_VERSION}/include")
    file(GLOB SOURCE_SET "${_SRC_DIR}/*.hpp")
    include_directories(_NAME PUBLIC ${_SRC_DIR})
endmacro()
