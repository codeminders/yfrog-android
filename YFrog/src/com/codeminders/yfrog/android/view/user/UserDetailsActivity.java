/**
 * 
 */
package com.codeminders.yfrog.android.view.user;

import com.codeminders.yfrog.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author idemydenko
 *
 */
public class UserDetailsActivity extends Activity {
	public static final String KEY_FULLNAME = "fullname";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PROFILE_IMAGE_URL = "profileImageUrl";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_DESCRIPTION = "desc";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.twitter_user_details);
		
		Bundle extras = getIntent().getExtras();
		
		TextView view = (TextView) findViewById(R.id.tu_fullname);
		view.setText(extras.getString(KEY_FULLNAME));
		view = (TextView) findViewById(R.id.tu_username);
		view.setText(extras.getString(KEY_USERNAME));
		view = (TextView) findViewById(R.id.tud_location);
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(extras.getString(KEY_LOCATION));
		buffer.append("\n");
		buffer.append(extras.getString(KEY_DESCRIPTION));
		view.setText(buffer.toString());
	}
}
