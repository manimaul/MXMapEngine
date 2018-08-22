package com.mxmariner.mxmapengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Mapbox.getInstance(this, "abc123")
    setContentView(R.layout.activity_main)
    mapView.onCreate(savedInstanceState)
//    mapView.getMapAsync {
//      it.api
//    }
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
