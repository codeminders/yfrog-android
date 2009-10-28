/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import android.os.Bundle;
import android.widget.EditText;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 * 
 */
public class WritePublicReplayActivity extends WritableActivity {
	private static final String START_REPLAY_PREFIX = "@";
	private static final String START_REPLAY_SUFFIX = " ";

	private String writerNickname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		writerNickname = getIntent().getExtras().getString(
				KEY_WRITER_USERNAME);
		super.onCreate(savedInstanceState);
		EditText editText = (EditText) findViewById(R.id.wr_text);
		editText.setText(getReplayStart());
	}

	@Override
	protected void send(String text) throws YFrogTwitterException {
		twitterService.publicReplay(text);
	}

	@Override
	protected UnsentMessage createUnsentMessage() {
		UnsentMessage message = new UnsentMessage();
		message.setType(UnsentMessage.TYPE_PUBLIC_REPLAY);
		return message;
	}

	private String getReplayStart() {
		return START_REPLAY_PREFIX + writerNickname + START_REPLAY_SUFFIX;
	}
	
	protected String createTitle() {
		StringBuilder title = new StringBuilder(super.createTitle());
		title.append(getResources().getString(R.string.wr_pub_reply_title));
		return title.append(" " + writerNickname).toString();
	}
}
