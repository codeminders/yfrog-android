/**
 * 
 */
package com.codeminders.yfrog2.android.view.message;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.ViewSwitcher.ViewFactory;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.controller.service.*;
import com.codeminders.yfrog2.android.model.MessageAttachment;
import com.codeminders.yfrog2.android.model.UnsentMessage;
import com.codeminders.yfrog2.android.util.*;
import com.codeminders.yfrog2.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog2.android.view.media.pick.VideoPickActivity;

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
	private SensorManager sensorManager;
	
	private TextSwitcher switcher;
	private int count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
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
	protected void onResume() {
		super.onResume();
		activateSensor();
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
		
		synchronized (lock) {
			savedSensorValues[0] = azimuth;
			savedSensorValues[1] = pitch;
			savedSensorValues[2] = roll;
		}
		
//		System.out.println("Start Attachment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> A: " + savedSensorValues[0] + " > P: " + savedSensorValues[1] + " > R: " + savedSensorValues[2]);

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
		attachment.setAzimuth(savedSensorValues[0]);
		attachment.setPitch(savedSensorValues[1]);
		attachment.setRoll(savedSensorValues[2]);
		
//		System.out.println(" Attach to server >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> A: " + savedSensorValues[0] + " > P: " + savedSensorValues[1] + " > R: " + savedSensorValues[2]);
		
		if (attachment != null) {
			new AsyncYFrogUpdater(this) {
				private static final String TAG = "upload";
				String mediaUrl = null;
				WakeLock powerWakeLock;

				protected void doUpdate() throws YFrogTwitterException {
					PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
					powerWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
					powerWakeLock.acquire();
					
					mediaUrl = yfrogService.upload(attachment);					
				}
				
				protected void doAfterUpdate() {
					if (mediaUrl != null) {
						addToText(mediaUrl);
					}
					
					if (powerWakeLock != null) {
						powerWakeLock.release();
					}
				}
				
				protected void doAfterError() {
					if (powerWakeLock != null) {
						powerWakeLock.release();
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
	
	private void activateSensor() {
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		
		for (Sensor sensor : sensors) {
			sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
//			System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>> " + sensor.getName());
		}
	}

	private void deactivateSensor() {
		sensorManager.unregisterListener(sensorEventListener);
	}
	
	private float[] savedSensorValues = new float[3];
	private Object lock = new Object();
	private volatile float azimuth = 0.0f;
	private volatile float pitch = 0.0f;
	private volatile float roll = 0.0f;
	
	
	private SensorEventListener sensorEventListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int accuracy) {};
		
		public void onSensorChanged(SensorEvent event) {
				if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
					synchronized (lock) {	
						azimuth = event.values[0];
						pitch = event.values[1];
						roll = event.values[2];
//						TextView tv = (TextView) findViewById(R.id.test);
//						tv.setText("Orientation:\nAzimuth: " + event.values[0] + "\nPitch: " + event.values[1] + "\nRoll: " + event.values[2]);
					}
				}

		};
	};
	
	@Override
	protected void onStop() {
		super.onStop();
		deactivateSensor();
	}
}