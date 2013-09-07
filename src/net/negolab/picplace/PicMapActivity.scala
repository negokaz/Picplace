package net.negolab.picplace

import android.app.Activity
import android.view.Menu
import android.os.Bundle

class PicMapActivity extends Activity {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pic_map)
  }
  
  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater().inflate(R.menu.pic_map, menu)
    true
  }
}