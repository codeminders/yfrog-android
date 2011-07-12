/**
 * 
 */
package com.codeminders.yfrog2.android.view.media;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.controller.service.AccountService;
import com.codeminders.yfrog2.android.controller.service.ServiceFactory;
import com.codeminders.yfrog2.android.util.AlertUtils;
import com.codeminders.yfrog2.android.util.StringUtils;
import com.codeminders.yfrog2.android.util.async.AsyncIOUpdater;

/**
 * @author idemydenko
 *
 */
public class ImageViewActivity extends Activity {
	public static final String KEY_IMAGE_URL = "imageUrl"; 
	
	private Zoom imageView;
	private String bitmapUrl;
	private Bitmap bitmap;
	private boolean saved = false;
	private AccountService accountService;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		accountService = ServiceFactory.getAccountService();
		
		setTitle(createTitle());
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			bitmapUrl = extras.getString(KEY_IMAGE_URL);
		} else {
			finish();
			return;
		}

		new AsyncIOUpdater(this) {
			protected void doUpdate() throws Exception {
				bitmap = BitmapFactory.decodeStream(new URL(bitmapUrl).openStream());
			}
			
			protected void doAfterUpdate() {
				imageView = new Zoom(ImageViewActivity.this, bitmap, accountService.getLogged().isScaleImage());
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
		values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, StringUtils.createFilename());
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
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return AlertUtils.createErrorAlert(this, id);
	}
	
	private String createTitle() {
		return accountService.getLogged().getUsername() + "> " + getResources().getString(R.string.iv_title);
	}
}
