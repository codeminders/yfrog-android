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
	private String writerNickname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		writerNickname = getIntent().getExtras().getString(
				KEY_WRITER_USERNAME);
		id = getIntent().getExtras().getLong(KEY_MESSAGE_ID);

		super.onCreate(savedInstanceState);
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
		return START_REPLAY_PREFIX + writerNickname + START_REPLAY_SUFFIX;
	}
	
	@Override
	protected String createTitle() {
		StringBuilder title = new StringBuilder(super.createTitle());
		title.append(getResources().getString(R.string.wr_reply_title));
		return title.append(" " + writerNickname).toString();
	}

}
