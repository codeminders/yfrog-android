/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import android.os.Bundle;
import android.widget.EditText;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 *
 */
public class EditUnsentMessageActivity extends WritableActivity {
	public static final String KEY_UNSENT_MESSAGE = "unsent";
	
	private UnsentMessage message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		message = (UnsentMessage) getIntent().getExtras().getSerializable(KEY_UNSENT_MESSAGE);
		
		EditText editText = (EditText) findViewById(R.id.wr_text);
		editText.setText(message.getText());
	}
	
	@Override
	protected UnsentMessage createUnsentMessage() {
		return message;
	}

	@Override
	protected void send(String text) {
		message.setText(text);
		
		try {
			twitterService.sendUnsentMessage(message);
		} catch (YFrogTwitterException e) {
			// TODO: handle exception
		}
	}
	
	protected void saveToQueue(String text) {
		UnsentMessage toSave = createUnsentMessage();
		toSave.setText(text);
		toSave.setAccountId(accountService.getLogged().getId());
		unsentMessageService.updateUnsentMessage(toSave);
	}

}
