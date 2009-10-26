/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.codeminders.yfrog.android.*;
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			
			if (extras != null) {
				ArrayList<TwitterUser> usrs = (ArrayList<TwitterUser>) extras.getSerializable(UserDetailsActivity.KEY_USERS);
				if (usrs != null) {
					removeUnfollow(usrs);
					users = usrs;
					
				}
				
			}
		}
	}
	
	private void removeUnfollow(ArrayList<TwitterUser> users) {
		int size = users.size();
		
		for (int i = 0; i < size; i++) {
			TwitterUser user = users.get(i);
			if (!user.isFollowing()) {
				users.remove(i);
				size--;
			}
		}
	}
	
	@Override
	protected String createTitle() {
		StringBuilder builder = new StringBuilder(super.createTitle());
		return builder.append(getResources().getString(R.string.frs_title)).toString();
	}
}
