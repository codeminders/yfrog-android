/**
 * 
 */
package com.codeminders.yfrog.android.util.image.cache;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import android.graphics.*;
import android.graphics.Bitmap.CompressFormat;

import com.codeminders.yfrog.android.util.MD5;

/**
 * @author idemydenko
 *
 */
public final class FileCache {
	private static final String ROOT = "/data/data/com.codeminders.yfrog.android/cache/";
	
	private static final int MAX_CACHED_COUNT = 500;
	private static final int TO_DELETE_COUNT = 100;
	private static final float LOAD_FACTOR = 0.75f;
	private static final long EXPIRED_TIME = 1000 * 60 * 60 * 24 * 7;
	
	private File root;
	private MD5 md5;
	
	private static final FileCache instance = new FileCache();
	
	public static FileCache getInstance() {
		return instance;
	}
	
	private FileCache() {
		root = new File(ROOT);
		
		if (!root.exists()) {
			root.mkdir();
		}
		
		try {
			md5 = MD5.getInstance();
		} catch (NoSuchAlgorithmException e) {
			// TODO: handle exception
		}
	}
	
	public void put(String key, Bitmap bitmap) {
		String filename = md5.hashData(key.getBytes());
		FileOutputStream fos = null;
		File file = new File(ROOT + filename);
		
		if (!file.exists()) {
			if (!pull()) {
				forcePull();
			}

			try {
				file.createNewFile();
				fos = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 75, fos);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if ( fos != null) { 
						fos.close();
					}
				} catch (IOException e) {
					
				}
			}
		}
	}
	
	public Bitmap get(String key) {
		Bitmap bitmap = null;
		FileInputStream fis = null;
		String filename = md5.hashData(key.getBytes());
		
		File file = new File(ROOT + filename);
		if (file.exists()) {
			try {
				fis = new FileInputStream(file);
				file.setLastModified(System.currentTimeMillis());
				bitmap = BitmapFactory.decodeStream(fis);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					
				}
			}
		}
		
		return bitmap;
	}
	
	private boolean pull() {
		boolean processed = false;
		File[] files = root.listFiles(); 
		int cached = files.length;
		
		if (cached >= MAX_CACHED_COUNT * LOAD_FACTOR) {
			long current = System.currentTimeMillis();
			for (int i = 0; i < cached; i++) {
				long modifiedTime = files[i].lastModified();
				if ((current - modifiedTime) > EXPIRED_TIME) {
					files[i].delete();
					processed = true;
				}
			}
		} else {
			processed = true;
		}
		
		return processed;
	}

	private void forcePull() {
		File[] files = root.listFiles(); 
		int cached = files.length;
		
		if (cached >= MAX_CACHED_COUNT) {
			List<File> list = Arrays.asList(files);
			
			Collections.sort(list, new Comparator<File>() {
				public int compare(File object1, File object2) {
					long time1 = object1.lastModified();
					long time2 = object2.lastModified();
					if (time1 > time2) {
						return 1;
					}
					if (time1 < time2) {
						return -1;
					}
					return 0;
				}
			});
			
			List<File> toDelete = list.subList(0, TO_DELETE_COUNT);
			
			for (int i = 0; i < TO_DELETE_COUNT; i++) {
				toDelete.get(i).delete();
			}			
		}
	}

}
