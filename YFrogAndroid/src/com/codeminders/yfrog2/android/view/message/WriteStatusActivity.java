/**
 * 
 */
package com.codeminders.yfrog2.android.view.message;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.TwitterStatus;
import com.codeminders.yfrog2.android.model.UnsentMessage;

/**
 * @author idemydenko
 * 
 */
public class WriteStatusActivity extends WritableActivity {
	private TwitterStatus sent = null;

	protected void send(String text) throws YFrogTwitterException {
		sent = twitterService.updateStatus(text);
	}

	@Override
	protected UnsentMessage createUnsentMessage() {
		UnsentMessage message = new UnsentMessage();
		message.setType(UnsentMessage.TYPE_STATUS);
		return message;
	}

	@Override
	protected void callback() {
		if (sent != null) {
			getIntent().putExtra(KET_SENT, sent);
			setResult(RESULT_SEND, getIntent());
		}
	}
	
	@Override
	protected String createTitle() {
		StringBuilder title = new StringBuilder(super.createTitle());
		return title.append(getResources().getString(R.string.wr_new_status_title)).toString();
	}
}
