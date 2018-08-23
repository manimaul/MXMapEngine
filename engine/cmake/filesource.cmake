add_library(mbgl-filesource STATIC
    # File source
    ${SRC_ROOT}/mapbox/include/mbgl/storage/default_file_source.hpp
    ${SRC_ROOT}/mapbox/platform/default/default_file_source.cpp
    ${SRC_ROOT}/mapbox/platform/default/mbgl/storage/file_source_request.hpp
    ${SRC_ROOT}/mapbox/platform/default/file_source_request.cpp
    ${SRC_ROOT}/mapbox/include/mbgl/storage/online_file_source.hpp
    ${SRC_ROOT}/mapbox/platform/default/online_file_source.cpp
    ${SRC_ROOT}/mapbox/src/mbgl/storage/http_file_source.hpp
    ${SRC_ROOT}/mapbox/src/mbgl/storage/asset_file_source.hpp
    ${SRC_ROOT}/mapbox/platform/default/asset_file_source.cpp
    ${SRC_ROOT}/mapbox/src/mbgl/storage/local_file_source.hpp
    ${SRC_ROOT}/mapbox/platform/default/local_file_source.cpp

    # Offline
    ${SRC_ROOT}/mapbox/include/mbgl/storage/offline.hpp
    ${SRC_ROOT}/mapbox/platform/default/mbgl/storage/offline.cpp
    ${SRC_ROOT}/mapbox/platform/default/mbgl/storage/offline_database.hpp
    ${SRC_ROOT}/mapbox/platform/default/mbgl/storage/offline_database.cpp
    ${SRC_ROOT}/mapbox/platform/default/mbgl/storage/offline_download.hpp
    ${SRC_ROOT}/mapbox/platform/default/mbgl/storage/offline_download.cpp

    # Database
    ${SRC_ROOT}/mapbox/platform/default/sqlite3.hpp
)

target_inc_header_lib(mbgl-filesource PUBLIC geometry 0.9.2)
target_inc_header_lib(mbgl-filesource PUBLIC variant 1.1.4)
target_inc_header_lib(mbgl-filesource PRIVATE rapidjson 1.1.0)
target_inc_header_lib(mbgl-filesource PRIVATE boost 1.65.1)
target_inc_header_lib(mbgl-filesource PRIVATE geojson 0.4.2)

target_include_directories(mbgl-filesource
    PRIVATE ${SRC_ROOT}/mapbox/include
    PRIVATE ${SRC_ROOT}/mapbox/src
    PRIVATE ${SRC_ROOT}/mapbox/platform/default
)

target_link_libraries(mbgl-filesource
    PUBLIC mbgl-core
)

mbgl_filesource()

create_source_groups(mbgl-filesource)
