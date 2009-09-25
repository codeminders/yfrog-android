/**
 * 
 */
package com.codeminders.yfrog.android.view.user;

import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterAuthException;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.view.main.adapter.TwitterUserAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * @author idemydenko
 *
 */
public abstract class AbstractTwitterUsersListActivity extends ListActivity {

	protected TwitterService twitterService;
	protected ArrayList<TwitterUser> users;

	/**
	 * 
	 */
	public AbstractTwitterUsersListActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterService = ServiceFactory.getTwitterService();
		
		try {
			users = getUsers();
		} catch (Exception e) {
		}
		
		
		setListAdapter(new TwitterUserAdapter<TwitterUser>(this, users));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position,
			long id) {
				Intent intent = new Intent(this, UserDetailsActivity.class);
				TwitterUser user = getSelected(position);
				
				intent.putExtra(UserDetailsActivity.KEY_USERNAME, user.getUsername());
				intent.putExtra(UserDetailsActivity.KEY_FULLNAME, user.getFullname());
				intent.putExtra(UserDetailsActivity.KEY_PROFILE_IMAGE_URL, user.getProfileImageURL());
				intent.putExtra(UserDetailsActivity.KEY_LOCATION, user.getLocation());
				intent.putExtra(UserDetailsActivity.KEY_DESCRIPTION, user.getDescription());
				
				startActivity(intent);
			}

	private TwitterUser getSelected(int position) {
		if (position > -1) {
			return users.get(position);
		}
		
		return null;
	}

	protected abstract ArrayList<TwitterUser> getUsers() throws YFrogTwitterException; 
}