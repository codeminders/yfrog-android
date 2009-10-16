/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import android.os.Bundle;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 *
 */
public class WriteDirectMessageActivity extends WritableActivity {
	private String username;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		username = getIntent().getExtras().getString(KEY_WRITER_USERNAME);
	}

	@Override
	protected void send(String text) {
		try {
			twitterService.sendDirectMessage(username, text);
		} catch (YFrogTwitterException e) {
			// TODO: handle exception
		}
	}

	@Override
	protected UnsentMessage createUnsentMessage() {
		UnsentMessage message = new UnsentMessage();
		message.setType(UnsentMessage.TYPE_DIRECT_MESSAGE);
		message.setTo(username);
		return message;
	}
}
