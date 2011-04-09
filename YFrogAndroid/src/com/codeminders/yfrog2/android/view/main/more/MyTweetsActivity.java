/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.more;


import java.util.ArrayList;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.TwitterStatus;
import com.codeminders.yfrog2.android.view.main.AbstractTwitterStatusesListActivity;

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
