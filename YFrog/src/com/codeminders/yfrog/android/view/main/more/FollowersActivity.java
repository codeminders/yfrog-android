/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import java.util.ArrayList;

import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterDirectMessage;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.view.main.adapter.TwitterDirectMessageAdapter;
import com.codeminders.yfrog.android.view.main.adapter.TwitterUserAdapter;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * @author idemydenko
 * 
 */
public class FollowersActivity extends ListActivity {
	private TwitterService twitterService;
	private ArrayList<TwitterUser> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterService = ServiceFactory.getTwitterService();
		
		try {
			users = twitterService.getFollowers();
		} catch (Exception e) {
		}
		
		
		setListAdapter(new TwitterUserAdapter<TwitterUser>(this, users));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());

	}
}
