/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.more;


import java.util.ArrayList;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.TwitterUser;
import com.codeminders.yfrog2.android.view.main.AbstractTwitterUsersListActivity;


/**
 * @author idemydenko
 * 
 */
public class FollowersActivity extends AbstractTwitterUsersListActivity {
	@Override
	protected ArrayList<TwitterUser> getUsers() throws YFrogTwitterException {
		return twitterService.getFollowers();
	}
	
	@Override
	protected String createTitle() {
		StringBuilder builder = new StringBuilder(super.createTitle());
		return builder.append(getResources().getString(R.string.frs_title)).toString();
	}
}
