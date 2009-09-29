/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import com.codeminders.yfrog.android.YFrogTwitterException;

/**
 * @author idemydenko
 *
 */
public class WriteTweetActivity extends WritableActivity {
	protected void send(String text) {
		try {
			twitterService.updateStatus(text);
		} catch (YFrogTwitterException e) {
			
		}		
	}
}
