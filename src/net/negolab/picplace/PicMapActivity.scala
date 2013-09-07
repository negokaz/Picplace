package net.negolab.picplace

import android.app.Activity
import android.view.Menu
import android.os.Bundle
import android.widget.Gallery
import android.content.Context
import android.widget.BaseAdapter
import android.content.ContentResolver
import android.widget.ImageView
import android.view.ViewGroup
import android.view.View
import android.graphics.BitmapFactory
import android.net.Uri
import android.graphics.Bitmap
import android.provider.MediaStore
import android.provider.BaseColumns

class PicMapActivity extends Activity {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pic_map)
    
    val picGallery = findViewById(R.id.picGallery).asInstanceOf[Gallery]
    picGallery.setAdapter(new ImageAdapter(this))
  }
  
  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater().inflate(R.menu.pic_map, menu)
    true
  }
  
  class ImageAdapter(context: Context) extends BaseAdapter {

    val resolver = context.getContentResolver()
    
    var imageIds = List[Long]()
    var imageNames = List[String]()
    
    val cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
    
    while (cursor.moveToNext()) {
      val id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
	  val name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE))
      
      imageIds = id :: imageIds
      imageNames= name :: imageNames
    }
    cursor.close()
    
    override def getCount() = imageIds.size
    
    override def getItemId(position: Int) = imageIds(position)
    
    override def getItem(position: Int) = imageNames(position)

    override def getView(position: Int, convertView: View, viewGroup: ViewGroup) = {
	    if (convertView == null) {
	      val view = new ImageView(PicMapActivity.this)
	      view.asInstanceOf[ImageView].setImageBitmap(getImageBitmap(position))
	      view.setLayoutParams(new Gallery.LayoutParams(150, 100))
	      view.asInstanceOf[ImageView].setScaleType(ImageView.ScaleType.FIT_XY)
	      view
	    } else convertView
    }
    
	def getImageBitmap(position: Int) = {
    	val imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getItemId(position).toString)
    	val options = new BitmapFactory.Options()
    	options.inSampleSize = 4
    	
    	val imputStream = resolver.openInputStream(imageUri)
    	
    	BitmapFactory.decodeStream(imputStream, null, options)
    }
    
  }
}