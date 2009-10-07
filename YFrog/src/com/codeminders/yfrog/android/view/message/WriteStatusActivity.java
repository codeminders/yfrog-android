/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 *
 */
public class WriteStatusActivity extends WritableActivity {
	protected void send(String text) {
		try {
			twitterService.updateStatus(text);
		} catch (YFrogTwitterException e) {
			
		}		
	}
	
	@Override
	protected UnsentMessage createUnsentMessage() {
		UnsentMessage message = new UnsentMessage();
		message.setType(UnsentMessage.TYPE_STATUS);
		return message;
	}
}
