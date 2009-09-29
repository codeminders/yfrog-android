/**
 * 
 */
package com.codeminders.yfrog.android.view.user;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.view.main.more.UserTweetsActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author idemydenko
 *
 */
public class UserDetailsActivity extends Activity implements OnClickListener {
	public static final String KEY_USER = "user";
	
	private TwitterUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.twitter_user_details);
		
		Bundle extras = getIntent().getExtras();
		
		user = (TwitterUser) extras.getSerializable(KEY_USER);
		
		TextView view = (TextView) findViewById(R.id.tu_fullname);
		view.setText(user.getFullname());
		view = (TextView) findViewById(R.id.tu_username);
		view.setText(user.getUsername());
		view = (TextView) findViewById(R.id.tud_location);
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(user.getLocation());
		buffer.append("\n");
		buffer.append(user.getDescription());
		view.setText(buffer.toString());
		
		Button button = (Button) findViewById(R.id.tud_recent_tweets);
		button.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tud_recent_tweets:
			Intent intent = new Intent(this, UserTweetsActivity.class);
			intent.putExtra(KEY_USER, user);
			startActivity(intent);
			break;
		}
	}
	
}
