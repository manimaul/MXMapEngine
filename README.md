# MXMapEngine

Android Only MapBox-GL-Native based map rendering library
(unashamedly ripped off from [mapbox-gl-native](https://github.com/mapbox/mapbox-gl-native) version tag android-v6.3.0)


### What?

Customized Mapbox GL Native library isolated for use on Android. Isolated = This only uses the Android/CMake build system and does *NOT* rely upon [mason](https://github.com/mapbox/mason) for dependencies.


### Why?

Although Mapbox is awesome, I want repeatable builds that does not rely so heavily on the success or existence of third parties.


### Status?

WIP - Todo:

* build ICU with stand-alone android toolchain (rather than using static libs)
* ~~include mapbox-android-gestures sources~~
* include mapbox-sdk-geojson sources
* include mapbox-sdk-services sources
* remove timber
* add mbgl gtest tests
* add mbgl junit tests
* CICD app center
* custom raster/vector tile provider
* custom overlays
* publish to jcenter
