add_library(mbgl-core STATIC
    ${MBGL_CORE_FILES}
)

target_include_directories(mbgl-core
    PUBLIC ${SRC_ROOT}/mapbox/include
    PRIVATE ${SRC_ROOT}/mapbox/src
)

target_inc_header_lib(mbgl-core PUBLIC geometry 0.9.2)
target_inc_header_lib(mbgl-core PUBLIC variant 1.1.4)
target_inc_header_lib(mbgl-core PRIVATE unique_resource cba309e)
target_inc_header_lib(mbgl-core PRIVATE rapidjson 1.1.0)
target_inc_header_lib(mbgl-core PRIVATE boost 1.65.1)
target_inc_header_lib(mbgl-core PRIVATE geojson 0.4.2)
target_inc_header_lib(mbgl-core PRIVATE geojsonvt 6.3.0)
target_inc_header_lib(mbgl-core PRIVATE supercluster 0.2.2)
target_inc_header_lib(mbgl-core PRIVATE kdbush 0.1.1-1)
target_inc_header_lib(mbgl-core PRIVATE earcut 0.12.4)
target_inc_header_lib(mbgl-core PRIVATE protozero 1.5.2)
target_inc_header_lib(mbgl-core PRIVATE polylabel 1.0.3)
target_inc_header_lib(mbgl-core PRIVATE wagyu 0.4.3)
target_inc_header_lib(mbgl-core PRIVATE shelf-pack 2.1.1)
target_inc_header_lib(mbgl-core PRIVATE vector-tile 1.0.1)

mbgl_platform_core()

create_source_groups(mbgl-core)
