/**
 * 
 */
package com.codeminders.yfrog.android.view.user;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogException;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;
import com.codeminders.yfrog.android.view.message.WriteDirectMessageActivity;
import com.codeminders.yfrog.android.view.message.WritePublicReplayActivity;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;

/**
 * @author idemydenko
 *
 */
public class UserDetailsActivity extends Activity implements OnClickListener {
	private static final String SAVED_POSITION = "sstatus_pos";
	
	public static final String KEY_USER_POS = "user_pos";
	public static final String KEY_USERS = "users";
	
	private static final int ALERT_PROTECTED = 0;
	
	private ArrayList<TwitterUser> users;
	private TwitterUser user;
	private int position;
	private int count;
	private TwitterService twitterService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterService = ServiceFactory.getTwitterService();
		
		setContentView(R.layout.twitter_user_details);
		
		Bundle extras = getIntent().getExtras();
		
		users = (ArrayList<TwitterUser>) extras.getSerializable(KEY_USERS);
		
		boolean restored = restoreState(savedInstanceState);
		
		if (!restored) {
			position = extras.getInt(KEY_USER_POS);
		}
		
		count = users.size();
		setCurrentUser();
		showUser();
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

	private void setCurrentUser() {
		user = users.get(position);
	}
	
	private void updateUserInList() {
		users.set(position, user);
	}
	
	private void showUser() {
		setTitle(createTitle());
		ImageView imageView = (ImageView) findViewById(R.id.tu_user_icon);
		ImageCache.getInstance().putImage(user.getProfileImageURL(), imageView);
		
		TextView view = (TextView) findViewById(R.id.tu_fullname);
		view.setText(user.getFullname());

		view = (TextView) findViewById(R.id.tu_username);
		view.setText(user.getScreenUsername());
		
		view = (TextView) findViewById(R.id.tud_location);
		view.setText(appendCaption(user.getLocation(), R.string.tud_location));
		
		view = (TextView) findViewById(R.id.tud_descrition);
		view.setText(appendCaption(user.getDescription(), R.string.tud_description));
		
		view = (TextView) findViewById(R.id.tud_followers);
		view.setText(appendCaption(user.getFollowersCount() + "", R.string.tud_followers));
		
		view = (TextView) findViewById(R.id.tud_following);
		view.setText(appendCaption(user.getFollowingsCount() + "" , R.string.tud_following));
		
		view = (TextView) findViewById(R.id.tud_counter);
		view.setText((position + 1) + "/" + count);

		if (count == 1) {
			view.setVisibility(View.GONE);
		}

		
	
		if (isUserProtected()) {
//			button.setVisibility(View.GONE);
			showDialog(ALERT_PROTECTED);
		}
		
	
		Button button = (Button) findViewById(R.id.tud_follow);
		button.setText(user.isFollowing() ? R.string.tud_btn_unfollow : R.string.tud_btn_follow);
		button.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tud_follow:
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					if (user.isFollowing()) {
						twitterService.unfollow(user.getUsername());
					} else {
						twitterService.follow(user.getUsername());
					}
					user.setFollowing(!user.isFollowing());					
				}
				
				protected void doAfterUpdate() {
					Button button = (Button) findViewById(R.id.tud_follow);
					button.setText(user.isFollowing() ? R.string.tud_btn_unfollow : R.string.tud_btn_follow);
					button.setOnClickListener(UserDetailsActivity.this);					
				}
			}.update();

			break;
		}
		
			
	}
	
	private void recentTweets() {
		Intent intent = new Intent(this, UserTweetsActivity.class);
		intent.putExtra(KEY_USER_POS, user);
		startActivity(intent);		
	}

	private void sendPublicReply() {
		Intent intent = new Intent(this, WritePublicReplayActivity.class);
		intent.putExtra(WritePublicReplayActivity.KEY_WRITER_USERNAME, user.getUsername());
		startActivity(intent);		
	}

	private void sendDirectMessage() {
		Intent intent = new Intent(this, WriteDirectMessageActivity.class);
		intent.putExtra(WriteDirectMessageActivity.KEY_WRITER_USERNAME, user.getUsername());
		startActivity(intent);		
	}

	private void followers() {
		Intent intent = new Intent(this, UserFollowersActivity.class);
		intent.putExtra(KEY_USER_POS, user);
		startActivity(intent);		
	}

	private boolean isUserProtected() {
		if (twitterService.getLoggedUser().equals(user)) {
			return false;
		}
		return user.isProtected();
	}
	
	private void refresh() {
		new AsyncYFrogUpdater(this) {
			protected void doUpdate() throws YFrogException {
				user = twitterService.getUser(user.getUsername());				
			}
			
			@Override
			protected void doAfterUpdate() {
				updateUserInList();
				showUser();
			}
		}.update();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (--position < 0) {
				position = count - 1;
			}
			setCurrentUser();
			showUser();
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (++position >= count) {
				position = 0;
			}
			setCurrentUser();
			showUser();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra(KEY_USERS, users);
			intent.putExtra(KEY_USER_POS, position);
			setResult(RESULT_OK, intent);
		}

		return super.onKeyDown(keyCode, event);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common_add_tweet, menu);
		inflater.inflate(R.menu.common_refresh_list, menu);
		inflater.inflate(R.menu.user_details, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.tudm_recent_tweets);
		item.setEnabled(!isUserProtected());
		item = menu.findItem(R.id.tudm_send_dir_msg);
		item.setEnabled(user.isFollower());
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
			return true;
		case R.id.reload_list:
			refresh();
			return true;
		case R.id.tudm_followers:
			followers();
			return true;
		case R.id.tudm_recent_tweets:
			recentTweets();
			return true;
		case R.id.tudm_send_dir_msg:
			sendDirectMessage();
			return true;
		case R.id.tudm_send_pub_reply:
			sendPublicReply();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case ALERT_PROTECTED:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.tud_protected_msg);
			builder.setNegativeButton(R.string.error_alert_btn, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface d, int which) {
					d.dismiss();
				}
			});
			
			dialog = builder.create();
			break;
		default:
			dialog = AlertUtils.createErrorAlert(this, id);
			break;
		}
		return dialog;
	}

	protected String createTitle() {
		StringBuilder title = new StringBuilder(StringUtils.formatTitle(twitterService.getLoggedUser().getUsername()));
		title.append(getResources().getString(R.string.tud_title));
		title.append(" " + user.getUsername());
		return title.toString();
	}

	private Spannable appendCaption(String appendable, int captionId) {
		String caption = getResources().getString(captionId);
		return appendCaption(appendable, caption);
		
	}

	private Spannable appendCaption(String appendable, String caption) {
		SpannableString spannable = new SpannableString(caption + " " + appendable);
		spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, caption.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new ForegroundColorSpan(R.color.caption), 0, caption.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}
}
