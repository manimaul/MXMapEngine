package com.mxmariner.mxmapengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mxmariner.engine.MXEngine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Example of a call to a native method
    sample_text.text = MXEngine.stringFromJni()
  }
}
