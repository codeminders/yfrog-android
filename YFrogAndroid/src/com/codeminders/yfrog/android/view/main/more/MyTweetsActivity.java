/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;


import java.util.ArrayList;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.view.main.AbstractTwitterStatusesListActivity;

/**
 * @author idemydenko
 *
 */
public class MyTweetsActivity extends AbstractTwitterStatusesListActivity {
	@Override
	protected ArrayList<TwitterStatus> getStatuses(int page, int count)
			throws YFrogTwitterException {
		return twitterService.getMyTweets(page, count);
	}
	
	protected String createTitle() {
		StringBuilder builder = new StringBuilder(super.createTitle());
		return builder.append(getResources().getString(R.string.mtw_title)).toString();
	}
}
