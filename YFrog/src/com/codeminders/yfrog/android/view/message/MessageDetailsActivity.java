/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import com.codeminders.yfrog.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author idemydenko
 *
 */
public class MessageDetailsActivity extends Activity {
	public static final String KEY_WRITER_NICKNAME = "nickname";
	public static final String KEY_WRITER_FULLNAME = "fullname";
	public static final String KEY_WRITER_PROFILE_IMAGE_URL = "profileImageUrl";
	public static final String KEY_MESSAGE_ID = "id";
	public static final String KEY_MESSAGE_TEXT = "text";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.twitter_message_detail);
		
		Bundle extras = getIntent().getExtras();
		
		TextView view = (TextView) findViewById(R.id.tu_username);
		view.setText(extras.getString(KEY_WRITER_NICKNAME));
		view = (TextView) findViewById(R.id.tu_fullname);
		view.setText(extras.getString(KEY_WRITER_FULLNAME));
		view = (TextView) findViewById(R.id.tm_text);
		view.setText(extras.getString(KEY_MESSAGE_TEXT));

	}
}
