/**
 * 
 */
package com.codeminders.yfrog.android.view.user;

import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.view.main.adapter.TwitterUserAdapter;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * @author idemydenko
 *
 */
public class FollowingActivity extends AbstractTwitterUsersListActivity {
	@Override
	protected ArrayList<TwitterUser> getUsers() throws YFrogTwitterException {
		return twitterService.getFollowings();
	}
}
