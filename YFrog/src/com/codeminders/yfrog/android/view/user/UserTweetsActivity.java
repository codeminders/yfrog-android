/**
 * 
 */
package com.codeminders.yfrog.android.view.user;


import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.view.main.AbstractTwitterStatusesListActivity;


/**
 * @author idemydenko
 *
 */
public class UserTweetsActivity extends AbstractTwitterStatusesListActivity {
	private TwitterUser user;
	
	@Override
	protected ArrayList<TwitterStatus> getStatuses()
			throws YFrogTwitterException {
		user = (TwitterUser) getIntent().getExtras().getSerializable(UserDetailsActivity.KEY_USER);
		return twitterService.getUserTweets(user.getUsername());
	}
}
