/**
 * 
 */
package com.codeminders.yfrog.android.view.user;

import java.util.ArrayList;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.*;
import com.codeminders.yfrog.android.util.async.AsyncTwitterUpdater;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;
import com.codeminders.yfrog.android.view.message.*;

/**
 * @author idemydenko
 *
 */
public class UserDetailsActivity extends Activity implements OnClickListener {
	public static final String KEY_USER_POS = "user";
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
		position = extras.getInt(KEY_USER_POS);
		count = users.size();
		setCurrentUser();
		setTitle(createTitle());
		showUser();
	}

	private void setCurrentUser() {
		user = users.get(position);
	}
	
	private void showUser() {
		ImageView imageView = (ImageView) findViewById(R.id.tu_user_icon);
		ImageCache.getInstance().putImage(user.getProfileImageURL(), imageView);
		
		TextView view = (TextView) findViewById(R.id.tu_fullname);
		view.setText(user.getFullname());
		view = (TextView) findViewById(R.id.tu_username);
		view.setText(user.getScreenUsername());
		view = (TextView) findViewById(R.id.tud_location);
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(user.getLocation());
		buffer.append("\n");
		buffer.append(user.getDescription());
		view.setText(buffer.toString());
		
		view = (TextView) findViewById(R.id.tud_counter);
		view.setText((position + 1) + "/" + count);
		
		
		Button button = (Button) findViewById(R.id.tud_recent_tweets);
		button.setOnClickListener(this);
		
		if (user.isProtected()) {
			button.setVisibility(View.GONE);
			showDialog(ALERT_PROTECTED);
		}
		
		button = (Button) findViewById(R.id.tud_send_pub_replay);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tud_followers);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tud_follow);
		button.setText(user.isFollowing() ? R.string.tud_btn_unfollow : R.string.tud_btn_follow);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tud_send_dir_message);
		if (!user.isFollower()) {
			button.setVisibility(View.INVISIBLE);
		}
		button.setOnClickListener(this);	
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tud_recent_tweets:
			intent = new Intent(this, UserTweetsActivity.class);
			intent.putExtra(KEY_USER_POS, user);
			startActivity(intent);
			break;
		case R.id.tud_followers:
			intent = new Intent(this, UserFollowersActivity.class);
			intent.putExtra(KEY_USER_POS, user);
			startActivity(intent);
			break;
		case R.id.tud_follow:
			new AsyncTwitterUpdater(this) {
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
		case R.id.tud_send_pub_replay:
			intent = new Intent(this, WritePublicReplayActivity.class);
			intent.putExtra(WritePublicReplayActivity.KEY_WRITER_USERNAME, user.getUsername());
			startActivity(intent);
			break;
		case R.id.tud_send_dir_message:
			intent = new Intent(this, WriteDirectMessageActivity.class);
			intent.putExtra(WriteDirectMessageActivity.KEY_WRITER_USERNAME, user.getUsername());
			startActivity(intent);
			break;

		}
		
			
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
			setResult(RESULT_OK, intent);
		}

		return super.onKeyDown(keyCode, event);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.common_add_tweet, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
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
		case ALERT_PROTECTED:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.tud_protected_msg);
			builder.setNegativeButton(R.string.twitter_error_btn, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface d, int which) {
					d.dismiss();
				}
			});
			
			dialiog = builder.create();
			break;
		}
		return dialiog;
	}

	protected String createTitle() {
		StringBuilder title = new StringBuilder(StringUtils.formatTitle(twitterService.getLoggedUser().getUsername()));
		title.append(getResources().getString(R.string.tud_title));
		title.append(" " + user.getUsername());
		return title.toString();
	}
}
