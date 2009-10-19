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
public class WriteReplayActivity extends WritableActivity {
	public static final String KEY_MESSAGE_ID = "id";

	private static final String START_REPLAY_PREFIX = "@";
	private static final String START_REPLAY_SUFFIX = " ";

	private long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = getIntent().getExtras().getLong(KEY_MESSAGE_ID);

		EditText editText = (EditText) findViewById(R.id.wr_text);
		editText.setText(getReplayStart());
	}

	@Override
	protected void send(String text) throws YFrogTwitterException {
		twitterService.replay(text, id);
	}

	@Override
	protected UnsentMessage createUnsentMessage() {
		UnsentMessage message = new UnsentMessage();
		message.setType(UnsentMessage.TYPE_REPLAY);
		message.setTo(String.valueOf(id));
		return message;
	}

	private String getReplayStart() {
		String writerNickname = getIntent().getExtras().getString(
				KEY_WRITER_USERNAME);

		return START_REPLAY_PREFIX + writerNickname + START_REPLAY_SUFFIX;
	}
}
