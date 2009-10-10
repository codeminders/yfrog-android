/**
 * 
 */
package com.codeminders.yfrog.android.util.image.cache;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.codeminders.yfrog.android.util.async.ViewAsyncUpdatable;
import com.codeminders.yfrog.android.util.async.ViewAsyncUpdateTask;
import com.codeminders.yfrog.android.util.async.ViewAsyncUpdateTask.UpdateListener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

/**
 * @author idemydenko
 * 
 */
public final class ImageCache implements UpdateListener<UserImageViewable>{
	private static ImageCache instance = new ImageCache();

	private final HashMap<String, SoftReference<Bitmap>> memcache = new HashMap<String, SoftReference<Bitmap>>();
	private final ViewAsyncUpdateTask<UserImageViewable> loader;
	private final FileCache fileCache = FileCache.getInstance();
	

	private ImageCache() {
		loader = new ViewAsyncUpdateTask<UserImageViewable>(this);
		loader.execute();
	}

	public static ImageCache getInstance() {
		return instance;
	}

	public void putImage(String url, ImageView imageView) {
		try {
			putImage(new URL(url), imageView);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void putImage(URL url, ImageView imageView) {

		String key = url.toString();

		Bitmap bitmap = get(key);

		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			UserImageViewable viewable = new UserImageViewable();
			viewable.setUrl(url);
			viewable.setView(imageView);

			loader.addToLoad(viewable);
		}
	}
	
	private Bitmap get(String key) {
		Bitmap bitmap = getMemcache(key);
		
		if (bitmap == null) {
			bitmap = getFileCache(key);
			
			if (bitmap != null) {
				putMemcache(key, bitmap);
			}
		}
		return bitmap;
	}

	@Override
	public void onUpdate(UserImageViewable updatable, Object received) {
		if (received instanceof Bitmap) {
			String key = updatable.getUrl().toString(); 
			
			putMemcache(key, (Bitmap) received);
			putFileCache(key, (Bitmap) received);
		}
		
	}
	
	private void putMemcache(String key, Bitmap bitmap) {
		synchronized (memcache) {
			memcache.put(key, new SoftReference<Bitmap>(bitmap));
//			System.out.println("put to memcache " + key + ": " + bitmap);			
		}
	}
	
	private Bitmap getMemcache(String key) {
		Bitmap result = null;
		if (memcache.containsKey(key)) {
			result = memcache.get(key).get();
//			System.out.println("get from memcache " + key + ": " + result);
		}			
		return result;
	}
	
	private void putFileCache(String key, Bitmap bitmap) {
		synchronized (fileCache) {
			fileCache.put(key, bitmap);
//			System.out.println("put to filecache " + key + ": " + bitmap);
		}		
	}
	
	private Bitmap getFileCache(String key) {
		Bitmap result = null;
		result = fileCache.get(key);
//		System.out.println("get from filecache " + key + ": " + result);		
		return result;
	}

}

class UserImageViewable extends ViewAsyncUpdatable<ImageView, Bitmap> {
	private static final int WIDTH = 48;
	private static final int HEIGHT = 48;

	@Override
	public void putData(final Bitmap bitmap) {
		getView().setImageBitmap(bitmap);
	}
	
	@Override
	public Bitmap receiveData() {
		Bitmap bitmap = null;

		try {
			bitmap = BitmapFactory.decodeStream(getUrl().openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resize(bitmap);
	}
	
	private Bitmap resize(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (width <= WIDTH && height <= HEIGHT) {
			return bitmap;
		}

		float scaleWidth = ((float) WIDTH) / width;
		float scaleHeight = ((float) HEIGHT) / height;
		
		Matrix matrix = new Matrix();
		matrix.setScale(scaleWidth, scaleHeight);
		
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}
}