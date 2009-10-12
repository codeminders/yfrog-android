/**
 * 
 */
package com.codeminders.yfrog.android.view.main;

import java.util.ArrayList;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.view.adapter.TwitterStatusAdapter;
import com.codeminders.yfrog.android.view.message.StatusDetailsActivity;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * @author idemydenko
 * 
 */
public abstract class AbstractTwitterStatusesListActivity extends ListActivity {
	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;

	protected TwitterService twitterService;
	protected ArrayList<TwitterStatus> statuses;
	private YFrogTwitterException toHandle;

	/**
	 * 
	 */
	public AbstractTwitterStatusesListActivity() {
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
		super.onResume();
		createList(false);

	}

	private void createList(boolean twitterUpdate) {
		if (twitterUpdate) {
			attempts = 1;
		}
		
		boolean needReload = twitterUpdate || isNeedReload();
		
		if (needReload) {
			try {
				statuses = getStatuses();
			} catch (YFrogTwitterException e) {
				statuses = new ArrayList<TwitterStatus>(0);
				toHandle = e;
				showDialog(AlertUtils.ALERT_TWITTER_ERROR);
			}
		}

		setListAdapter(new TwitterStatusAdapter<TwitterStatus>(this, statuses));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	}

	private boolean isNeedReload() {
		return (++attempts % ATTEMPTS_TO_RELOAD == 0);
	}

	protected abstract ArrayList<TwitterStatus> getStatuses() throws YFrogTwitterException;
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, StatusDetailsActivity.class);

		intent.putExtra(StatusDetailsActivity.KEY_STATUS_POS, position);
		intent.putExtra(StatusDetailsActivity.KEY_STATUSES, statuses);

		startActivity(intent);
	}

	protected TwitterStatus getSelected(int position) {
		if (position > -1) {
			return statuses.get(position);
		}
		return null;
	}

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.common_refresh_list, menu);
		return super.onCreateOptionsMenu(menu);
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reload_list:
			createList(true);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}