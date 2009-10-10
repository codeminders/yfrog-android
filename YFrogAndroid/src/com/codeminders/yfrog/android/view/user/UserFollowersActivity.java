/**
 * 
 */
package com.codeminders.yfrog.android.view.user;

import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.view.main.AbstractTwitterUsersListActivity;

/**
 * @author idemydenko
 *
 */
public class UserFollowersActivity extends AbstractTwitterUsersListActivity {
	
	private TwitterUser user;
	
	
	@Override
	protected ArrayList<TwitterUser> getUsers() throws YFrogTwitterException {
		user = (TwitterUser) getIntent().getExtras().getSerializable(UserDetailsActivity.KEY_USER);
		return twitterService.getUserFollowers(user.getUsername());
	}

}
