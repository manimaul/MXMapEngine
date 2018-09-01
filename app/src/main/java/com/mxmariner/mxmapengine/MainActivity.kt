package com.mxmariner.mxmapengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.plugins.locationlayer.CameraMode.TRACKING_GPS_NORTH
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.willkamp.rxar.RxPermission
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Mapbox.getInstance(this, getString(R.string.mapbox_token))
    setContentView(R.layout.activity_main)
    mapView.onCreate(savedInstanceState)

    RxPermission.checkLocationPermissionLazyRequest(this, supportFragmentManager).filter {
      it
    }.flatMap {
      mapView.mapAsync.toMaybe()
    }.observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
              val lp = LocationLayerPlugin(mapView, it)
              lp.cameraMode = TRACKING_GPS_NORTH
            }
        )

    mapView.mapAsync.observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
              it.setStyle(Style.OUTDOORS)
            }
        )
  }

  public override fun onStart() {
    super.onStart()
    mapView.onStart()
  }

  public override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  public override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  public override fun onStop() {
    super.onStop()
    mapView.onStop()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}
