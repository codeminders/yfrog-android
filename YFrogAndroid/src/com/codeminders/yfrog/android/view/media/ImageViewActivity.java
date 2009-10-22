/**
 * 
 */
package com.codeminders.yfrog.android.view.media;

import java.io.*;
import java.net.URL;
import java.util.*;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.util.DialogUtils;
import com.codeminders.yfrog.android.util.async.*;

import android.app.*;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;

/**
 * @author idemydenko
 *
 */
public class ImageViewActivity extends Activity {
	private static final String FILENAME_PREFIX = "yfrog";
	
	public static final String KEY_IMAGE_URL = "imageUrl"; 
	
	private Zoom imageView;
	private String bitmapUrl;
	private Bitmap bitmap;
	private boolean saved = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			bitmapUrl = extras.getString(KEY_IMAGE_URL);
		} else {
			finish();
			return;
		}

		new AsyncUpdater(this) {
			protected void doUpdate() throws YFrogTwitterException {
				try {
					bitmap = BitmapFactory.decodeStream(new URL(bitmapUrl).openStream());
				} catch (Exception e) {
					throw new YFrogTwitterException(e);
				}
			}
			
			protected void doAfterUpdate() {
//				setContentView(R.layout.image_view);
//				LinearLayout layout = (LinearLayout) findViewById(R.id.iv_scroll);
//				LayoutParams params = new ViewGroup.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
				imageView = new Zoom(ImageViewActivity.this, bitmap);
//				layout.addView(imageView, params);
				setContentView(imageView);
			}
		}.update();
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.image_view, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.iv_zoom_in:
			imageView.zoomIn();
			return true;

		case R.id.iv_zoom_out:
			imageView.zoomOut();
			return true;

		case R.id.iv_save:
			save();
			return true;

		}
		return false;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (saved) {
			menu.findItem(R.id.iv_save).setEnabled(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			imageView.zoomIn();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			imageView.zoomOut();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			imageView.panBy(-20f, 0);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			imageView.panBy(20f, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			imageView.startMove(event.getX(), event.getY());
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			imageView.stopMove(event.getX(), event.getY());
		}

		return super.onTouchEvent(event);
	}
	
	private void save() {
		if (saved) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, createName());
		values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
		Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


		OutputStream out = null;
		try{
			out = getContentResolver().openOutputStream(uri);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			saved = true;
		} catch(IOException e){
			
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					
				}
			}
		}

	}
	
	private String createName() {
		Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				new String[] { MediaStore.Images.ImageColumns.DISPLAY_NAME }, 
				null, 
				null, 
				MediaStore.Images.ImageColumns.DISPLAY_NAME + " ASC");
		
		if (cursor == null || cursor.getCount() == 0) {
			return FILENAME_PREFIX + 0;
		}
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		try {
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
				
			while(cursor.moveToNext()) {
				indexes.add(extractFileindex(cursor.getString(idx)));
			}
		} finally {
			cursor.close();
		}
		
		
		return FILENAME_PREFIX + (Collections.max(indexes) + 1);
	}
	
	private Integer extractFileindex(String filename) {
		if (filename == null || !filename.startsWith(FILENAME_PREFIX)) {
			return 0;
		}
		
		Integer result = 0;
		
		try {
			result = Integer.parseInt(filename.substring(FILENAME_PREFIX.length() - 1));
		} catch (NumberFormatException e) {
		}
		
		return result;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialiog = null;
		switch (id) {
		case DialogUtils.ALERT_IO_ERROR:
			dialiog = DialogUtils.createIOErrorAlert(this);
			break;
		}
		return dialiog;
	}

}
