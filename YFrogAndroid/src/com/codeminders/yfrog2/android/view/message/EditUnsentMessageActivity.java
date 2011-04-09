/**
 * 
 */
package com.codeminders.yfrog2.android.view.message;

import android.os.Bundle;
import android.widget.EditText;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.UnsentMessage;

/**
 * @author idemydenko
 * 
 */
public class EditUnsentMessageActivity extends WritableActivity {
	public static final String KEY_UNSENT_MESSAGE = "unsent";

	private UnsentMessage message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		message = (UnsentMessage) getIntent().getExtras().getSerializable(
				KEY_UNSENT_MESSAGE);
	
		super.onCreate(savedInstanceState);
		
		EditText editText = (EditText) findViewById(R.id.wr_text);
		editText.setText(message.getText());
	}

	@Override
	protected UnsentMessage createUnsentMessage() {
		return message;
	}

	@Override
	protected void send(String text) throws YFrogTwitterException {
		message.setText(text);
		twitterService.sendUnsentMessage(message);
	}

	protected void saveToQueue(String text) {
		UnsentMessage toSave = createUnsentMessage();
		toSave.setText(text);
		toSave.setAccountId(accountService.getLogged().getId());
		unsentMessageService.updateUnsentMessage(toSave);
	}

	protected String createTitle() {
		StringBuilder title = new StringBuilder(super.createTitle());
		
		
		switch (message.getType()) {
		case UnsentMessage.TYPE_DIRECT_MESSAGE:
			title.append(getResources().getString(R.string.wr_edit_dir_msg_title));
			title.append(" " + message.getTo());
			break;
		case UnsentMessage.TYPE_PUBLIC_REPLAY:
			title.append(getResources().getString(R.string.wr_edit_pub_reply_title));
			break;
		case UnsentMessage.TYPE_REPLAY:
			title.append(getResources().getString(R.string.wr_edit_reply_title));
			break;
		case UnsentMessage.TYPE_STATUS:
			title.append(getResources().getString(R.string.wr_edit_new_status_title));
			break;
		}
		return title.toString();
	}

}
