/**
 * 
 */
package com.codeminders.yfrog.android.view.main;

import java.util.ArrayList;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.DialogUtils;
import com.codeminders.yfrog.android.util.async.AsyncTwitterUpdater;
import com.codeminders.yfrog.android.view.adapter.TwitterUserAdapter;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;
import com.codeminders.yfrog.android.view.user.UserDetailsActivity;

/**
 * @author idemydenko
 *
 */
public abstract class AbstractTwitterUsersListActivity extends ListActivity {
	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;

	protected TwitterService twitterService;
	protected ArrayList<TwitterUser> users = new ArrayList<TwitterUser>(0);

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
		
		createList(true);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		createList(false);
	}

	protected final void createList(boolean twitterUpdate) {
		if (twitterUpdate) {
			attempts = 1;
		}
		
		boolean needReload = twitterUpdate || isNeedReload();
		
		if (needReload) {
			new AsyncTwitterUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					users = getUsers();
				}
				
				protected void doAfterUpdate() {
					show();
				}
			}.update();
		} else {
			show();
		}
		
		
	}

	private void show() {
		setListAdapter(new TwitterUserAdapter<TwitterUser>(this, users));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());				
	}
	
	private boolean isNeedReload() {
		return (++attempts % ATTEMPTS_TO_RELOAD == 0);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position,
			long id) {
				Intent intent = new Intent(this, UserDetailsActivity.class);
				
				intent.putExtra(UserDetailsActivity.KEY_USERS, users);
				intent.putExtra(UserDetailsActivity.KEY_USER_POS, position);
				
				startActivityForResult(intent, 0);
			}

	protected abstract ArrayList<TwitterUser> getUsers() throws YFrogTwitterException; 
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialiog = null;
		switch (id) {
		case DialogUtils.ALERT_TWITTER_ERROR:
			dialiog = DialogUtils.createTwitterErrorAlert(this);
			break;

		}
		return dialiog;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.common_refresh_list, menu);
		getMenuInflater().inflate(R.menu.common_add_tweet, menu);
		return super.onCreateOptionsMenu(menu);
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reload_list:
			createList(true);
			return true;
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}