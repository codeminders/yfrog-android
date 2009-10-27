/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import java.util.ArrayList;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.*;
import com.codeminders.yfrog.android.util.async.*;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;

/**
 * @author idemydenko
 *
 */

// TODO may be need StatusChangeListener
public class StatusDetailsActivity extends Activity implements OnClickListener {
	public static final String KEY_STATUS_POS = "status_pos";
	public static final String KEY_STATUSES = "statuses";
	
	private TwitterService twitterService;
	private ArrayList<TwitterStatus> statuses = new ArrayList<TwitterStatus>(0);
	private TwitterStatus status;
	private int position;
	private int count;
	private boolean favorited;
	private boolean my;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitterService = ServiceFactory.getTwitterService();
		setContentView(R.layout.twitter_status_details);
		setTitle(createTitle());
		
		Bundle extras = getIntent().getExtras();
		statuses = (ArrayList<TwitterStatus>) extras.getSerializable(KEY_STATUSES);
		position = extras.getInt(KEY_STATUS_POS);
		count = statuses.size();
		setCurrentStatus();
		

		showStatus();
	}
	
	private void setCurrentStatus() {
		status = statuses.get(position);
	}
	
	private void showStatus() {
		favorited = status.isFavorited();
		my = twitterService.getLoggedUser().equals(status.getUser());

		Button button = (Button) findViewById(R.id.tm_replay);
//		if (my) {
//			button.setVisibility(View.GONE);
//		} else {
//			button.setVisibility(View.VISIBLE);
//		}
		
		button.setOnClickListener(this);

		
		button = (Button) findViewById(R.id.tm_favorite);
		
		if (favorited) {
			button.setText(R.string.tm_btn_unfavorite);
		} else {
			button.setText(R.string.tm_btn_favorite);
		}
		
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tm_forward);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tm_delete);
//		button.setVisibility(my ? View.VISIBLE : View.GONE);
		button.setOnClickListener(this);

		ImageView imageView = (ImageView) findViewById(R.id.tu_user_icon);
		ImageCache.getInstance().putImage(status.getUser().getProfileImageURL(), imageView);
		
		TextView view = (TextView) findViewById(R.id.tu_username);
		view.setText(status.getUser().getUsername());
		
		view = (TextView) findViewById(R.id.tu_fullname);
		view.setText(status.getUser().getFullname());
	
		view = (TextView) findViewById(R.id.tm_created_at);
		view.setText(StringUtils.formatDate(getResources(), status.getCreatedAt()));

		view = (TextView) findViewById(R.id.tm_counter);
		view.setText((position + 1) + "/" + count);
		
		setText();
	}
	
	private void setText() {
		final TextView textView = (TextView) findViewById(R.id.tm_text);
		final String text = status.getText();
		if (YFrogUtils.hasYFrogImageContent(text)) {
			textView.setText(StringUtils.EMPTY_STRING);
			new AsyncUpdater(this) {
				private CharSequence spannable;
				protected void doUpdate() throws Exception {
					spannable = StringUtils.parseURLs(text, StatusDetailsActivity.this);
				}
				
				protected void doAfterUpdate() {
					textView.setText(spannable);					
				}
			}.update();			
		} else {
			textView.setText(StringUtils.parseURLs(text, StatusDetailsActivity.this));
		}
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tm_replay:
//			reply();
			break;
		case R.id.tm_favorite:
			favorite();
			break;
		case R.id.tm_forward:
			break;
		case R.id.tm_delete:
//			delete();
			break;
		}
	}

	private void reply() {
		Intent intent = new Intent(this, WriteReplayActivity.class);
		intent.putExtra(WriteReplayActivity.KEY_MESSAGE_ID, status.getId());
		intent.putExtra(WritableActivity.KEY_WRITER_USERNAME, status.getUser().getUsername());
		startActivity(intent);
	}
	
	private void forwardByEmail() {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, status.getText());
			startActivity(intent);
	}

	private void forwardBySMS() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setType("vnd.android-dir/mms-sms");
		intent.putExtra("sms_body", status.getText());
		startActivity(intent);
	}

	private void delete() {
		new AsyncTwitterUpdater(this) {
			@Override
			protected void doUpdate() throws YFrogTwitterException {
				twitterService.deleteStatus(status.getId());
			}
			
			protected void doAfterUpdate() {
				statuses.remove(status);
				prepareResult();
				finish();
			}
		}.update();
	}
	
	private void favorite() {
		new AsyncTwitterUpdater(this) {
			@Override
			protected void doUpdate() throws YFrogTwitterException {
				if (!favorited) {
					twitterService.favorite(status.getId());
				} else {
					twitterService.unfavorite(status.getId());
				}
				favorited = !favorited;
				status.setFavorited(favorited);
			}
			
			protected void doAfterUpdate() {
				Button button = (Button) findViewById(R.id.tm_favorite);
				button.setText(favorited ? R.string.tm_btn_unfavorite
						: R.string.tm_btn_favorite);
			}
		}.update();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (--position < 0) {
				position = count - 1;
			}
			setCurrentStatus();
			showStatus();
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (++position >= count) {
				position = 0;
			}
			setCurrentStatus();
			showStatus();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			prepareResult();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private void prepareResult() {
		Intent intent = new Intent();
		intent.putExtra(KEY_STATUSES, statuses);
		setResult(RESULT_OK, intent);		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater(); 
		inflater.inflate(R.menu.common_add_tweet, menu);
		inflater.inflate(R.menu.status_details, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.tsd_delete);
		item.setEnabled(my);
		item = menu.findItem(R.id.tsd_reply);
		item.setEnabled(!my);

		return super.onPrepareOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		
		switch (item.getItemId()) {
		case R.id.add_tweet:
			intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
			return true;
		
		case R.id.tsd_delete:
			delete();
			return true;
			
		case R.id.tsd_forward_email:
			forwardByEmail();
			return true;

		case R.id.tsd_forward_sms:
			forwardBySMS();
			return true;

		case R.id.tsd_reply:
			reply();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialiog = null;
		switch (id) {
		case DialogUtils.ALERT_TWITTER_ERROR:
			dialiog = DialogUtils.createTwitterErrorAlert(this);
			break;
		}
		return dialiog;
	}
	
	protected String createTitle() {
		StringBuilder status = new StringBuilder(StringUtils.formatTitle(twitterService.getLoggedUser().getUsername()));
		status.append(getResources().getString(R.string.tm_title));
		return status.toString();
	}
}
