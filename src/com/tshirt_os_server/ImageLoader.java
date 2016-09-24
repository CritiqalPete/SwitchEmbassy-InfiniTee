package com.tshirt_os_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.tshirt_os_server.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	final int stub_id = R.drawable.default_img;

	public void displayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
		}
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		try{
			PhotoToLoad picToLoad = new PhotoToLoad(url, imageView);
			executorService.submit(new PhotosLoader(picToLoad));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Bitmap getBitmap(String url) {
		File file = fileCache.getFile(url);

		// from SD cache
		Bitmap bitmap1 = decodeFile(file);
		if (bitmap1 != null){
			return bitmap1;
		}

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream inputStrm = conn.getInputStream();
			OutputStream outputStrm = new FileOutputStream(file);
			Utils.copyStream(inputStrm, outputStrm);
			outputStrm.close();
			bitmap = decodeFile(file);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File file) {
		try {
			// decode image size
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, option);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 250;
			int widthTmp = option.outWidth, heightTmp = option.outHeight;
			int scale = 1;
			while (true) {
				if (widthTmp / 2 < REQUIRED_SIZE
						|| heightTmp / 2 < REQUIRED_SIZE){
					break;
				}
				widthTmp /= 2;
				heightTmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options option2 = new BitmapFactory.Options();
			option2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(file), null, option2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String tempStr, ImageView imgView) {
			url = tempStr;
			imageView = imgView;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad)){
				return;
			}
			Bitmap bmp = getBitmap(photoToLoad.url);
//			bmp = getRoundedCornerBitmap(bmp);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad)){
				return;
			}
			BitmapDisplayer bitDisply = new BitmapDisplayer(bmp, photoToLoad);
			Activity activity = (Activity) photoToLoad.imageView.getContext();
			activity.runOnUiThread(bitDisply);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url)){
			return true;
		}
		return false;
	}

	// for making image round corner
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		
		Bitmap bitmap2= Bitmap.createScaledBitmap(bitmap, 200, 200, false);
		
		Bitmap output = Bitmap.createBitmap(bitmap2.getWidth(),
				bitmap2.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 100; 

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap2, rect, rect, paint);
		bitmap2.recycle();
		return output;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap bitmap3, PhotoToLoad picToLoad) {
			bitmap = bitmap3;
			photoToLoad = picToLoad;
		}

		public void run() {
			if (imageViewReused(photoToLoad)){
				return;
			}
			if (bitmap != null){
				photoToLoad.imageView.setImageBitmap(bitmap);
			}
			else{
				photoToLoad.imageView.setImageResource(stub_id);
			}
			}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}


}
