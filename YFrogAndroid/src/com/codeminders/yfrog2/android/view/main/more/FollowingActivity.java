/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.more;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.TwitterUser;
import com.codeminders.yfrog2.android.view.main.AbstractTwitterUsersListActivity;
import com.codeminders.yfrog2.android.view.user.UserDetailsActivity;

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
				Serializable serializable = extras.getSerializable(UserDetailsActivity.KEY_USERS);
				if (serializable != null) {
				    @SuppressWarnings("unchecked")
					ArrayList<TwitterUser> usrs = (ArrayList<TwitterUser>) serializable;
					removeUnfollow(usrs);
					users = usrs;
					
				}
				
				int pos = extras.getInt(UserDetailsActivity.KEY_USER_POS);
				
				if (pos > -1 && pos < users.size()) {
					selected = pos;
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
		return builder.append(getResources().getString(R.string.fng_title)).toString();
	}
}
