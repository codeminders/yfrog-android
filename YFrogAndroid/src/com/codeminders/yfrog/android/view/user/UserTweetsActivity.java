/**
 * 
 */
package com.codeminders.yfrog.android.view.user;


import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.*;
import com.codeminders.yfrog.android.view.main.AbstractTwitterStatusesListActivity;


/**
 * @author idemydenko
 *
 */
public class UserTweetsActivity extends AbstractTwitterStatusesListActivity {
	private TwitterUser user;
	
	@Override
	protected ArrayList<TwitterStatus> getStatuses(int page, int count)
			throws YFrogTwitterException {
		user = (TwitterUser) getIntent().getExtras().getSerializable(UserDetailsActivity.KEY_USER_POS);
		return twitterService.getUserTweets(user.getUsername(), page, count);
	}
}
