/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.ViewSwitcher.ViewFactory;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.model.MessageAttachment;
import com.codeminders.yfrog.android.model.UnsentMessage;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.util.FileUtils;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog.android.view.media.pick.VideoPickActivity;

/**
 * @author idemydenko
 *
 */
public abstract class WritableActivity extends Activity implements OnClickListener, TextWatcher, ViewFactory {
	private static final int REQUEST_PHOTO = 2;
	private static final int REQUEST_VIDEO = 3;
	private static final int REQUEST_STORED_PHOTO = 4;
	private static final int REQUEST_STORED_VIDEO = 5;

	
	public static final int RESULT_SEND = 100;
	public static final int RESULT_QUEUE = 101;
	
	public static final String KET_SENT = "sent";
	public static final String KEY_WRITER_USERNAME = "username";
	
	private static final int MAX_COUNT = 140;
	private static final int OVERRIDED_TEXT_COLOR = 0x77FF0000;
	
	protected TwitterService twitterService;
	protected AccountService accountService;
	protected UnsentMessageService unsentMessageService;
	protected YFrogService yfrogService;
	protected GeoLocationService geoLocationService;
	
	private TextSwitcher switcher;
	private int count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitterService = ServiceFactory.getTwitterService();
		accountService = ServiceFactory.getAccountService();
		unsentMessageService = ServiceFactory.getUnsentMessageService();
		yfrogService = ServiceFactory.getYFrogService();
		geoLocationService = ServiceFactory.getGeoLocationService();
		
		setContentView(R.layout.twitter_writable);

		setTitle(createTitle());
		
		EditText editText = (EditText) findViewById(R.id.wr_text);
		editText.addTextChangedListener(this);

		switcher = (TextSwitcher) findViewById(R.id.wr_counter);
		switcher.setFactory(this);

		count = editText.getText().length();
		
		Button button = (Button) findViewById(R.id.wr_send);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.wr_queue);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.wr_add_location);
		button.setOnClickListener(this);
		
		updateCounter();
	}

	@Override
	public void onClick(View v) {
		final String text = getText();
		
		switch (v.getId()) {
		case R.id.wr_send:
			if (isOverrideMaxCount()) {
				return;
			}
			
			if (!StringUtils.isEmpty(text)) {
				
				new AsyncYFrogUpdater(this) {
					protected void doUpdate() throws YFrogTwitterException {
						send(text);						
					}
					
					protected void doAfterUpdate() {
						callback();
						finish();
						Toast.makeText(getApplicationContext(), R.string.msg_sent, Toast.LENGTH_SHORT).show();
					}
				}.update();
			}
			break;
		case R.id.wr_queue:
			if (isOverrideMaxCount()) {
				return;
			}

			if (!StringUtils.isEmpty(text)) {
				saveToQueue(text);
				callback();
				finish();
			}
			
			break;
		case R.id.wr_add_location:
			
			if (geoLocationService.isAvailable()) {
				Location location = geoLocationService.getLocation();
				String mapUrl = StringUtils.creatMapUrl(location.getLatitude(), location.getLongitude());
				addToText(mapUrl);
			} else {
				showDialog(AlertUtils.GPS_RETRIEVE_LOCATION_ERROR);
			}
			break;
		}
	}
	
	private void startAttachment(int request) {
		Intent intent = new Intent();
		
		switch(request) {
			case REQUEST_PHOTO:
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				break;
			case REQUEST_VIDEO:
				intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
				break;
			case REQUEST_STORED_PHOTO:
				intent.setAction(Intent.ACTION_PICK);
				intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
				intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				break;
			case REQUEST_STORED_VIDEO:
				intent.setClass(this, VideoPickActivity.class);
				break;
		}
		startActivityForResult(intent, request);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			attach(requestCode, data);
		}
	}
	
	private void attach(int request, Intent data) {
		final MessageAttachment attachment = createAttachment(request, data);
		
		if (attachment != null) {
			new AsyncYFrogUpdater(this) {
				String mediaUrl = null;

				protected void doUpdate() throws YFrogTwitterException {
					mediaUrl = yfrogService.upload(attachment);					
				}
				
				protected void doAfterUpdate() {
					if (mediaUrl != null) {
						addToText(mediaUrl);
					}
				}
			}.update();
		}
	}

	private MessageAttachment createAttachment(int request, Intent data) {
		MessageAttachment attachment = null;
		if (request == REQUEST_PHOTO) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap bitmap = (Bitmap) extras.getParcelable("data");
				if (bitmap != null) {
					attachment = new MessageAttachment(this, bitmap);
				}
			}
		} else {
			Uri uri = data.getData();
			if (uri != null) {
				attachment = new MessageAttachment(this, FileUtils.getMediaFileUri(this, uri));
			}
		}
		return attachment;
	}
	
	private void addToText(String text) {
		EditText textInput = (EditText) findViewById(R.id.wr_text);
		String currentText = textInput.getText().toString();
		
		String newText = currentText == null ? text : currentText + " " + text;
		textInput.setText(newText);
		textInput.setSelection(newText.length());
	}
	
	protected void saveToQueue(String text) {
		UnsentMessage toSave = createUnsentMessage();
		toSave.setText(text);
		toSave.setAccountId(accountService.getLogged().getId());
		
		unsentMessageService.addUnsentMessage(toSave);
	}
		
	protected abstract void send(String text) throws YFrogTwitterException;
	
	protected abstract UnsentMessage createUnsentMessage();

	protected void callback() {
		
	}
	
	private String getText() {
		EditText textInput = (EditText) findViewById(R.id.wr_text);
		return textInput.getText().toString();
	}

	@Override
	public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        
        if (isOverrideMaxCount()) {
        	t.setTextColor(OVERRIDED_TEXT_COLOR);
        }
        return t;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		count = s.length();
		updateCounter();
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	private void updateCounter() {
		switcher.setText(String.valueOf(MAX_COUNT - count));
	}
	
	private boolean isOverrideMaxCount() {
		return count > MAX_COUNT;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return AlertUtils.createErrorAlert(this, id);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.writable, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int requestCode = -1;
		switch (item.getItemId()) {
			case R.id.camera_image:
				requestCode = REQUEST_PHOTO;
				break;
			case R.id.camera_video:
				requestCode = REQUEST_VIDEO;
				break;
			case R.id.storage_image:
				requestCode = REQUEST_STORED_PHOTO;
				break;
			case R.id.storage_video:
				requestCode = REQUEST_STORED_VIDEO;
				break;
		}

		if (requestCode == -1) {
			return false;
		}
		
		startAttachment(requestCode);
		return true;
	}
	
	protected String createTitle() {
		return StringUtils.formatTitle(twitterService.getLoggedUser().getUsername());
	}
}