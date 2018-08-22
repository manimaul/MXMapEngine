add_definitions(-DMBGL_USE_GLES2=1)
# todo include(cmake/test-files.cmake)
include(cmake/nunicode.cmake)

# Build thin archives.
set(CMAKE_CXX_ARCHIVE_CREATE "<CMAKE_AR> cruT <TARGET> <LINK_FLAGS> <OBJECTS>")
set(CMAKE_C_ARCHIVE_CREATE "<CMAKE_AR> cruT <TARGET> <LINK_FLAGS> <OBJECTS>")
set(CMAKE_CXX_ARCHIVE_APPEND "<CMAKE_AR> ruT <TARGET> <LINK_FLAGS> <OBJECTS>")
set(CMAKE_C_ARCHIVE_APPEND "<CMAKE_AR> ruT <TARGET> <LINK_FLAGS> <OBJECTS>")

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -ffunction-sections -fdata-sections")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -ffunction-sections -fdata-sections")

if ((ANDROID_ABI STREQUAL "armeabi-v7a") OR (ANDROID_ABI STREQUAL "arm64-v8a") OR
    (ANDROID_ABI STREQUAL "x86") OR (ANDROID_ABI STREQUAL "x86_64"))
    # Use Identical Code Folding on platforms that support the gold linker.
    set(CMAKE_EXE_LINKER_FLAGS "-fuse-ld=gold -Wl,--icf=safe ${CMAKE_EXE_LINKER_FLAGS}")
    set(CMAKE_SHARED_LINKER_FLAGS "-fuse-ld=gold -Wl,--icf=safe ${CMAKE_SHARED_LINKER_FLAGS}")
endif()

set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -Wl,--gc-sections -Wl,--version-script=${SRC_ROOT}/mapbox/platform/android/version-script")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--gc-sections -Wl,--version-script=${SRC_ROOT}/mapbox/platform/android/version-script")

# todo() mason_use(jni.hpp VERSION 3.0.0 HEADER_ONLY)
# todo() mason_use(sqlite VERSION 3.14.2)
# todo() mason_use(gtest VERSION 1.8.0)
# todo() mason_use(icu VERSION 58.1-min-size)

## mbgl core ##

macro(mbgl_platform_core)
    target_sources(mbgl-core
        # Loop
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/async_task.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/run_loop.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/run_loop_impl.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/timer.cpp

        # Misc
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/text/collator.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/text/collator_jni.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/text/local_glyph_rasterizer.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/text/local_glyph_rasterizer_jni.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/logging_android.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/thread.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/string_stdlib.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/bidi.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/thread_local.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/unaccent.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/unaccent.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/utf.cpp

        # Image handling
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/png_writer.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/bitmap.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/bitmap.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/bitmap_factory.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/bitmap_factory.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/image.cpp

        # Thread pool
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/util/shared_thread_pool.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/util/shared_thread_pool.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/util/default_thread_pool.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/util/default_thread_pool.hpp

        # Rendering
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/android_renderer_backend.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/android_renderer_backend.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/android_renderer_frontend.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/android_renderer_frontend.hpp

        # Snapshots (core)
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/gl/headless_backend.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/gl/headless_backend.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/gl/headless_frontend.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/gl/headless_frontend.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/map/map_snapshotter.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/map/map_snapshotter.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/linux/src/headless_backend_egl.cpp
    )

    target_include_directories(mbgl-core
        PUBLIC ${SRC_ROOT}/mapbox/platform/default
        PRIVATE ${SRC_ROOT}/mapbox/platform/android
    )

    target_inc_header_lib(mbgl-core PUBLIC geojson 0.4.2)
    target_inc_header_lib(mbgl-core PUBLIC jni.hpp 3.0.0)
    target_inc_header_lib(mbgl-core PUBLIC rapidjson 1.1.0)

    ## target_add_mason_package(mbgl-core PUBLIC geojson)
    ## target_add_mason_package(mbgl-core PUBLIC jni.hpp)
    ## target_add_mason_package(mbgl-core PUBLIC rapidjson)
    ## todo() target_add_mason_package(mbgl-core PRIVATE icu)

    target_link_libraries(mbgl-core
        PRIVATE nunicode
        PUBLIC -llog
        PUBLIC -landroid
        PUBLIC -ljnigraphics
        PUBLIC -lEGL
        PUBLIC -lGLESv2
        PUBLIC -lstdc++
        PUBLIC -latomic
        PUBLIC -lz
    )
endmacro()


macro(mbgl_filesource)
    target_sources(mbgl-filesource
        # File source
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/http_file_source.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/asset_manager.hpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/asset_manager_file_source.cpp
        PRIVATE ${SRC_ROOT}/mapbox/platform/android/asset_manager_file_source.hpp

        # Database
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/sqlite3.cpp
    )

    target_add_mason_package(mbgl-filesource PUBLIC sqlite)
    target_add_mason_package(mbgl-filesource PUBLIC jni.hpp)

    target_link_libraries(mbgl-filesource
        PUBLIC -llog
        PUBLIC -landroid
        PUBLIC -lstdc++
        PUBLIC -latomic
    )
endmacro()


## Main library ##

add_library(mbgl-android STATIC
    # Conversion C++ -> Java
    ${SRC_ROOT}/mapbox/platform/android/conversion/constant.hpp
    ${SRC_ROOT}/mapbox/platform/android/conversion/conversion.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/conversion/function.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/conversion/property_value.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/conversion/types.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/conversion/types_string_values.hpp
    ${SRC_ROOT}/mapbox/platform/android/map/camera_position.cpp
    ${SRC_ROOT}/mapbox/platform/android/map/camera_position.hpp
    ${SRC_ROOT}/mapbox/platform/android/map/image.cpp
    ${SRC_ROOT}/mapbox/platform/android/map/image.hpp

    # Style conversion Java -> C++
    ${SRC_ROOT}/mapbox/platform/android/style/android_conversion.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/value.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/value.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/conversion/url_or_tileset.hpp

    # Style
    ${SRC_ROOT}/mapbox/platform/android/style/transition_options.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/transition_options.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/background_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/background_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/circle_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/circle_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/custom_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/custom_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/fill_extrusion_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/fill_extrusion_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/fill_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/fill_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/heatmap_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/heatmap_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/hillshade_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/hillshade_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/layers.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/layers.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/line_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/line_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/raster_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/raster_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/symbol_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/symbol_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/unknown_layer.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/layers/unknown_layer.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/geojson_source.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/geojson_source.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/custom_geometry_source.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/custom_geometry_source.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/source.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/source.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/raster_source.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/raster_source.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/unknown_source.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/unknown_source.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/vector_source.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/vector_source.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/image_source.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/image_source.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/raster_dem_source.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/sources/raster_dem_source.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/position.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/position.hpp
    ${SRC_ROOT}/mapbox/platform/android/style/light.cpp
    ${SRC_ROOT}/mapbox/platform/android/style/light.hpp

    # FileSource holder
    ${SRC_ROOT}/mapbox/platform/android/file_source.cpp
    ${SRC_ROOT}/mapbox/platform/android/file_source.hpp

    # Connectivity
    ${SRC_ROOT}/mapbox/platform/android/connectivity_listener.cpp
    ${SRC_ROOT}/mapbox/platform/android/connectivity_listener.hpp

    # Native map
    ${SRC_ROOT}/mapbox/platform/android/native_map_view.cpp
    ${SRC_ROOT}/mapbox/platform/android/native_map_view.hpp
    ${SRC_ROOT}/mapbox/platform/android/map_renderer.cpp
    ${SRC_ROOT}/mapbox/platform/android/map_renderer.hpp
    ${SRC_ROOT}/mapbox/platform/android/map_renderer_runnable.cpp
    ${SRC_ROOT}/mapbox/platform/android/map_renderer_runnable.hpp

    # Java core classes
    ${SRC_ROOT}/mapbox/platform/android/java/lang.cpp
    ${SRC_ROOT}/mapbox/platform/android/java/lang.hpp
    ${SRC_ROOT}/mapbox/platform/android/java/util.cpp
    ${SRC_ROOT}/mapbox/platform/android/java/util.hpp

    # Graphics
    ${SRC_ROOT}/mapbox/platform/android/graphics/pointf.cpp
    ${SRC_ROOT}/mapbox/platform/android/graphics/pointf.hpp
    ${SRC_ROOT}/mapbox/platform/android/graphics/rectf.cpp
    ${SRC_ROOT}/mapbox/platform/android/graphics/rectf.hpp

    # GeoJSON
    ${SRC_ROOT}/mapbox/platform/android/geojson/feature.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/feature.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/feature_collection.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/feature_collection.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/geometry.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/geometry.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/geometry_collection.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/geometry_collection.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/line_string.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/line_string.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/multi_line_string.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/multi_line_string.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/multi_point.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/multi_point.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/multi_polygon.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/multi_polygon.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/point.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/point.hpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/polygon.cpp
    ${SRC_ROOT}/mapbox/platform/android/geojson/polygon.hpp

    # Geometry
    ${SRC_ROOT}/mapbox/platform/android/geometry/lat_lng.cpp
    ${SRC_ROOT}/mapbox/platform/android/geometry/lat_lng.hpp
    ${SRC_ROOT}/mapbox/platform/android/geometry/lat_lng_bounds.cpp
    ${SRC_ROOT}/mapbox/platform/android/geometry/lat_lng_bounds.hpp
    ${SRC_ROOT}/mapbox/platform/android/geometry/lat_lng_quad.cpp
    ${SRC_ROOT}/mapbox/platform/android/geometry/lat_lng_quad.hpp
    ${SRC_ROOT}/mapbox/platform/android/geometry/projected_meters.cpp
    ${SRC_ROOT}/mapbox/platform/android/geometry/projected_meters.hpp

    # GSon
    ${SRC_ROOT}/mapbox/platform/android/gson/json_array.cpp
    ${SRC_ROOT}/mapbox/platform/android/gson/json_array.hpp
    ${SRC_ROOT}/mapbox/platform/android/gson/json_element.cpp
    ${SRC_ROOT}/mapbox/platform/android/gson/json_element.hpp
    ${SRC_ROOT}/mapbox/platform/android/gson/json_object.cpp
    ${SRC_ROOT}/mapbox/platform/android/gson/json_object.hpp
    ${SRC_ROOT}/mapbox/platform/android/gson/json_primitive.cpp
    ${SRC_ROOT}/mapbox/platform/android/gson/json_primitive.hpp

    # Annotation
    ${SRC_ROOT}/mapbox/platform/android/annotation/marker.cpp
    ${SRC_ROOT}/mapbox/platform/android/annotation/marker.hpp
    ${SRC_ROOT}/mapbox/platform/android/annotation/polygon.cpp
    ${SRC_ROOT}/mapbox/platform/android/annotation/polygon.hpp
    ${SRC_ROOT}/mapbox/platform/android/annotation/polyline.cpp
    ${SRC_ROOT}/mapbox/platform/android/annotation/polyline.hpp

    # Offline
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_manager.cpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_manager.hpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_region.cpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_region.hpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_region_definition.cpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_region_definition.hpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_region_error.cpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_region_error.hpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_region_status.cpp
    ${SRC_ROOT}/mapbox/platform/android/offline/offline_region_status.hpp

    # Snapshots (SDK)
    ${SRC_ROOT}/mapbox/platform/android/snapshotter/map_snapshotter.cpp
    ${SRC_ROOT}/mapbox/platform/android/snapshotter/map_snapshotter.hpp
    ${SRC_ROOT}/mapbox/platform/android/snapshotter/map_snapshot.cpp
    ${SRC_ROOT}/mapbox/platform/android/snapshotter/map_snapshot.hpp

    # Main jni bindings
    ${SRC_ROOT}/mapbox/platform/android/attach_env.cpp
    ${SRC_ROOT}/mapbox/platform/android/attach_env.hpp
    ${SRC_ROOT}/mapbox/platform/android/java_types.cpp
    ${SRC_ROOT}/mapbox/platform/android/java_types.hpp

    # Main entry point
    ${SRC_ROOT}/mapbox/platform/android/jni.hpp
    ${SRC_ROOT}/mapbox/platform/android/jni.cpp
)

target_link_libraries(mbgl-android
    PUBLIC mbgl-filesource
    PUBLIC mbgl-core
)

## Shared library

add_library(mapbox-gl SHARED
    ${SRC_ROOT}/mapbox/platform/android/main.cpp
)

target_link_libraries(mapbox-gl
    PRIVATE mbgl-android
)

## Test library ##

set(MBGL_TEST_TARGET_TYPE "library")
macro(mbgl_platform_test)
    target_sources(mbgl-test
        PRIVATE ${SRC_ROOT}/mapbox/platform/default/mbgl/test/main.cpp

        # Main test entry point
        ${SRC_ROOT}/mapbox/platform/android/test/main.jni.cpp
    )

    target_include_directories(mbgl-test
        PRIVATE ${SRC_ROOT}/mapbox/platform/android
    )

    target_link_libraries(mbgl-test
        PRIVATE mbgl-android
    )
endmacro()

## Custom layer example ##

add_library(example-custom-layer SHARED
    ${SRC_ROOT}/mapbox/platform/android/example_custom_layer.cpp
)

target_link_libraries(example-custom-layer
    PRIVATE mbgl-core
)
