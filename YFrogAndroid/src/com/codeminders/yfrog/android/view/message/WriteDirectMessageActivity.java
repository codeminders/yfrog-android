/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import android.os.Bundle;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 * 
 */
public class WriteDirectMessageActivity extends WritableActivity {
	private String username;

	protected void onCreate(Bundle savedInstanceState) {
		username = getIntent().getExtras().getString(KEY_WRITER_USERNAME);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void send(String text) throws YFrogTwitterException {
		twitterService.sendDirectMessage(username, text);		
	}

	@Override
	protected UnsentMessage createUnsentMessage() {
		UnsentMessage message = new UnsentMessage();
		message.setType(UnsentMessage.TYPE_DIRECT_MESSAGE);
		message.setTo(username);
		return message;
	}
	
	protected String createTitle() {
		StringBuilder title = new StringBuilder(super.createTitle());
		title.append(getResources().getString(R.string.wr_dir_msg_title));
		return title.append(" " + username).toString();
	}

	
}
