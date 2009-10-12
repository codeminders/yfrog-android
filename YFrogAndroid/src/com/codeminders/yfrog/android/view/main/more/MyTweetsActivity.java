/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;


import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.view.main.AbstractTwitterStatusesListActivity;

/**
 * @author idemydenko
 *
 */
public class MyTweetsActivity extends AbstractTwitterStatusesListActivity {
	@Override
	protected ArrayList<TwitterStatus> getStatuses()
			throws YFrogTwitterException {
		return twitterService.getMyTweets();
	}
}
