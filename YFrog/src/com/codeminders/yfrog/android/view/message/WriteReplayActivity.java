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
public class WriteReplayActivity extends WritableActivity {
	private static final String START_REPLAY_PREFIX = "@";
	private static final String START_REPLAY_SUFFIX = " ";
	
	private long id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		id = getIntent().getExtras().getLong(StatusDetailsActivity.KEY_MESSAGE_ID);
		
		
		EditText editText = (EditText) findViewById(R.id.wr_text);
		editText.setText(getReplayStart());
	}
	
	@Override
	protected void send(String text) {
		
		try {
			twitterService.replay(text, id);
		} catch (YFrogTwitterException e) {
			// TODO: handle exception
		}
	}

	private String getReplayStart() {
		String writerNickname = getIntent().getExtras().getString(StatusDetailsActivity.KEY_WRITER_USERNAME);
		
		return START_REPLAY_PREFIX + writerNickname + START_REPLAY_SUFFIX;
	}
}
