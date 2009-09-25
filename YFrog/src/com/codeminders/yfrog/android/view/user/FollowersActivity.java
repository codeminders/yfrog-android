/**
 * 
 */
package com.codeminders.yfrog.android.view.user;


import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterUser;


/**
 * @author idemydenko
 * 
 */
public class FollowersActivity extends AbstractTwitterUsersListActivity {
	@Override
	protected ArrayList<TwitterUser> getUsers() throws YFrogTwitterException {
		return twitterService.getFollowers();
	}
}
