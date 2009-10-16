/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.view.main.AbstractTwitterUsersListActivity;
import com.codeminders.yfrog.android.view.user.UserDetailsActivity;

/**
 * @author idemydenko
 *
 */
public class FollowingActivity extends AbstractTwitterUsersListActivity {
	@Override
	protected ArrayList<TwitterUser> getUsers() throws YFrogTwitterException {
		return twitterService.getFollowings();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == UserDetailsActivity.RESULT_UNFOLLOW) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				TwitterUser toRemove = (TwitterUser) bundle.getSerializable(UserDetailsActivity.KEY_USER_POS);
				users.remove(toRemove);
				createList(false);
			}
		}
	}
	
	
}
