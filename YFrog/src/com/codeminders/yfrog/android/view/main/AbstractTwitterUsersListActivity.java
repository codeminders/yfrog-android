/**
 * 
 */
package com.codeminders.yfrog.android.view.main;

import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterAuthException;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.view.main.adapter.TwitterUserAdapter;
import com.codeminders.yfrog.android.view.user.UserDetailsActivity;

import android.app.Dialog;
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
	protected YFrogTwitterException toHandle;

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
		} catch (YFrogTwitterException e) {
			users = new ArrayList<TwitterUser>(0);
			toHandle = e;
			showDialog(AlertUtils.ALERT_TWITTER_ERROR);
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
				
				intent.putExtra(UserDetailsActivity.KEY_USER, user);
				
				startActivity(intent);
			}

	private TwitterUser getSelected(int position) {
		if (position > -1) {
			return users.get(position);
		}
		
		return null;
	}

	protected abstract ArrayList<TwitterUser> getUsers() throws YFrogTwitterException; 
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialiog = null;
		switch (id) {
		case AlertUtils.ALERT_TWITTER_ERROR:
			dialiog = AlertUtils.createTwitterErrorAlert(this, toHandle);
			toHandle = null;
			
			break;

		}
		return dialiog;
	}
}