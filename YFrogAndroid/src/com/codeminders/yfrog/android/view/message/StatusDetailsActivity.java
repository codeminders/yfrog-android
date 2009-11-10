/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.util.YFrogUtils;
import com.codeminders.yfrog.android.util.async.AsyncIOUpdater;
import com.codeminders.yfrog.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;
import com.codeminders.yfrog.android.view.user.UserDetailsActivity;

/**
 * @author idemydenko
 *
 */

// TODO may be need StatusChangeListener
public class StatusDetailsActivity extends Activity implements OnClickListener {
	private static final String SAVED_POSITION = "sstatus_pos";
	
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
		boolean restored = restoreState(savedInstanceState);
		
		if (!restored) {
			position = extras.getInt(KEY_STATUS_POS);
		}
		count = statuses.size();
		setCurrentStatus();
		

		showStatus();
	}
	
	private boolean restoreState(Bundle savedState) {
		if (savedState == null) {
			return false;
		}
	
		int value = savedState.getInt(SAVED_POSITION);
		if (value < 0) {
			return false;
		}
		position = value;
		
		return true;
	}
	
	private void saveState(Bundle savedState) {
		if (savedState == null) {
			return;
		}
		
		savedState.putInt(SAVED_POSITION, position);
	}

	private void setCurrentStatus() {
		status = statuses.get(position);
	}
	
	private void showStatus() {
		favorited = status.isFavorited();
		my = twitterService.getLoggedUser().equals(status.getUser());

		Button button = (Button) findViewById(R.id.tm_favorite);
		
		if (favorited) {
			button.setText(R.string.tm_btn_unfavorite);
		} else {
			button.setText(R.string.tm_btn_favorite);
		}
		
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
		
		if (count == 1) {
			view.setVisibility(View.GONE);
		}
		
		setText();
	}
	
	private void setText() {
		final TextView textView = (TextView) findViewById(R.id.tm_text);
		final String text = status.getText();
		if (YFrogUtils.hasYFrogContent(text)) {
			textView.setText(StringUtils.EMPTY_STRING);
			new AsyncIOUpdater(this) {
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
		case R.id.tm_favorite:
			favorite();
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
		new AsyncYFrogUpdater(this) {
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
		new AsyncYFrogUpdater(this) {
			@Override
			protected void doUpdate() throws YFrogTwitterException {
//				if (!favorited) {
//					status = twitterService.favorite(status.getId());
//				} else {
//					status = twitterService.unfavorite(status.getId());
//				}
//				
//				favorited = status.isFavorited();

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
	
	private void userInfo() {
		new AsyncYFrogUpdater(this) {
			TwitterUser u;
			
			@Override
			protected void doUpdate() throws YFrogTwitterException {
				u = twitterService.getUser(status.getUser().getUsername());
			}
			
			@Override
			protected void doAfterUpdate() {
				Intent intent = new Intent(StatusDetailsActivity.this, UserDetailsActivity.class);
				
				ArrayList<TwitterUser> users = new ArrayList<TwitterUser>(1);
				users.add(u);
				
				intent.putExtra(UserDetailsActivity.KEY_USERS, (Serializable) users);
				intent.putExtra(UserDetailsActivity.KEY_USER_POS, 0);
				
				startActivityForResult(intent, 0);
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
		intent.putExtra(KEY_STATUS_POS, position);
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
		case R.id.tsd_userinfo:
			userInfo();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return AlertUtils.createErrorAlert(this, id);
	}
	
	protected String createTitle() {
		StringBuilder status = new StringBuilder(StringUtils.formatTitle(twitterService.getLoggedUser().getUsername()));
		status.append(getResources().getString(R.string.tm_title));
		return status.toString();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}
}
