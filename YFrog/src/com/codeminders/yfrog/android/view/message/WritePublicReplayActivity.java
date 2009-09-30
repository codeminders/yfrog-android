/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import android.os.Bundle;
import android.widget.EditText;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;

/**
 * @author idemydenko
 *
 */
public class WritePublicReplayActivity extends WritableActivity {
	private static final String START_REPLAY_PREFIX = "@";
	private static final String START_REPLAY_SUFFIX = " ";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		EditText editText = (EditText) findViewById(R.id.wr_text);
		editText.setText(getReplayStart());
	}
	
	@Override
	protected void send(String text) {
		
		try {
			twitterService.publicReplay(text);
		} catch (YFrogTwitterException e) {
			// TODO: handle exception
		}
	}

	private String getReplayStart() {
		String writerNickname = getIntent().getExtras().getString(KEY_WRITER_USERNAME);
		
		return START_REPLAY_PREFIX + writerNickname + START_REPLAY_SUFFIX;
	}}
