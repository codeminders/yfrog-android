/**
 * 
 */
package com.codeminders.yfrog.android.view.main;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.*;
import com.codeminders.yfrog.android.util.async.AsyncTwitterUpdater;
import com.codeminders.yfrog.android.view.adapter.TwitterUserAdapter;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;
import com.codeminders.yfrog.android.view.user.UserDetailsActivity;

/**
 * @author idemydenko
 *
 */
public abstract class AbstractTwitterUsersListActivity extends ListActivity {
	private static final String SAVED_USERS = "susers";
	private static final String SAVED_ATTEMPTS = "sattempts";
	private static final String SAVED_SELECTED = "sselected";

	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;
	
	protected int selected = -1;

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
		
		boolean restored = restoreState(savedInstanceState);
		
		createList(!restored);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}
	
	private boolean restoreState(Bundle savedState) {
		if (savedState == null) {
			return false;
		}
		
		Serializable values = savedState.getSerializable(SAVED_USERS);
		if (values == null) {
			return false;
		}
		users = (ArrayList<TwitterUser>) values;		
		
		int value = savedState.getInt(SAVED_ATTEMPTS);
		if (value < 0) {
			return false;
		}
		attempts = value;
		
		value = savedState.getInt(SAVED_SELECTED);
		if (value > -1 && value < users.size()) {
			selected = value;
		}

		return true;
	}
	
	private void saveState(Bundle savedState) {
		if (savedState == null) {
			return;
		}
		
		savedState.putSerializable(SAVED_USERS, users);
		savedState.putInt(SAVED_ATTEMPTS, attempts);
		savedState.putInt(SAVED_SELECTED, getSelectedItemPosition());
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
				
				protected void doAfterError() {
					show();
				}
			}.update();
		} else {
			show();
		}
		
		
	}

	private void show() {
		setContentView(R.layout.twitter_users_list);
		setListAdapter(new TwitterUserAdapter<TwitterUser>(this, users));
		
		if (selected > -1) {
			setSelection(selected);
		}
		
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
		setTitle(createTitle());
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			
			if (extras != null) {
				Serializable serializable = extras.getSerializable(UserDetailsActivity.KEY_USERS);
				if (serializable != null) {
					users = (ArrayList<TwitterUser>) serializable;
				}
				
				int pos = extras.getInt(UserDetailsActivity.KEY_USER_POS);
				
				if (pos > -1 && pos < users.size()) {
					selected = pos;
				}
				
			}
		}
	}
	
	protected abstract ArrayList<TwitterUser> getUsers() throws YFrogTwitterException; 
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return AlertUtils.createErrorAlert(this, id);
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

	protected String createTitle() {
		return StringUtils.formatTitle(twitterService.getLoggedUser().getUsername());
	}
}