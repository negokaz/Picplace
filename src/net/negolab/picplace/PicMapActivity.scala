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
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager

class PicMapActivity extends Activity {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_pic_map)
    
    val picPager = findViewById(R.id.picPager).asInstanceOf[ViewPager]
    picPager.setAdapter(new ImagePageAdapter(this))
  }
  
  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater().inflate(R.menu.pic_map, menu)
    true
  }
  
  class ImagePageAdapter(context: Context) extends PagerAdapter {

    val resolver = context.getContentResolver()
    
    var imageIds = List[Long]()
    
    val cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
    
    while (cursor.moveToNext()) {
      val id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
      imageIds = id :: imageIds
    }
    cursor.close()
    
    override def isViewFromObject(view: View, obj: Any) = view == obj
    
    override def getCount() = imageIds.size
    
    override def destroyItem(viewGroup: ViewGroup, position: Int, obj: Any) =
      viewGroup.removeView(obj.asInstanceOf[View])
    
    override def instantiateItem(viewGroup: ViewGroup, position: Int) = {
      val imageView = new ImageView(context)
      imageView.asInstanceOf[ImageView].setImageBitmap(getImageBitmap(position))
      imageView.setLayoutParams(new Gallery.LayoutParams(150, 100))
      imageView.asInstanceOf[ImageView].setScaleType(ImageView.ScaleType.FIT_XY)
      viewGroup.addView(imageView)
      imageView
    }
    
	def getImageBitmap(position: Int) = {
    	val imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageIds(position).toString)
    	val options = new BitmapFactory.Options()
    	options.inSampleSize = 4
    	
    	val imputStream = resolver.openInputStream(imageUri)
    	
    	BitmapFactory.decodeStream(imputStream, null, options)
    }
  }
}