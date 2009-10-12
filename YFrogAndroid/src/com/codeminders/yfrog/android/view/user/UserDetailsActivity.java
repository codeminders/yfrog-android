/**
 * 
 */
package com.codeminders.yfrog.android.view.user;

import java.util.ArrayList;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;
import com.codeminders.yfrog.android.view.message.WriteDirectMessageActivity;
import com.codeminders.yfrog.android.view.message.WritePublicReplayActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author idemydenko
 *
 */
public class UserDetailsActivity extends Activity implements OnClickListener {
	public static final String KEY_USER_POS = "user";
	public static final String KEY_USERS = "users";
	
	public static final int RESULT_UNFOLLOW = 102;
	
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
			try {
				if (user.isFollowing()) {
					twitterService.unfollow(user.getUsername());
				} else {
					twitterService.follow(user.getUsername());
				}
				user.setFollowing(!user.isFollowing());
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			Button button = (Button) findViewById(R.id.tud_follow);
			button.setText(user.isFollowing() ? R.string.tud_btn_unfollow : R.string.tud_btn_follow);
			button.setOnClickListener(this);

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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!user.isFollowing()) {
				getIntent().putExtra(KEY_USER_POS, user);
				setResult(RESULT_UNFOLLOW, getIntent());
				finish();
				return false;
			}

		}
		
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

		return super.onKeyDown(keyCode, event);
	}
}
