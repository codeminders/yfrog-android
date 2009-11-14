/**
 * 
 */
package com.codeminders.yfrog.android.view.user;


import java.util.ArrayList;

import android.os.Bundle;

import com.codeminders.yfrog.android.R;
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
	protected ArrayList<TwitterStatus> getStatuses(int page, int count)
			throws YFrogTwitterException {
		user = (TwitterUser) getIntent().getExtras().getSerializable(UserDetailsActivity.KEY_USER_POS);
		return twitterService.getUserTweets(user.getUsername(), page, count);
	}
	
	@Override
	protected boolean restoreState(Bundle savedState) {
		boolean result = super.restoreState(savedState);
		user = (TwitterUser) getIntent().getExtras().getSerializable(UserDetailsActivity.KEY_USER_POS);
		return result;
	}
	
	protected String createTitle() {
		StringBuilder builder = new StringBuilder(super.createTitle());
		builder.append(getResources().getString(R.string.utw_title));
		builder.append(" ");
		if (user != null) {
			builder.append(user.getUsername());
		}
		return builder.toString();
	}
}
