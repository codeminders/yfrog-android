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
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_COUNT = 3200;
	
	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;

	protected TwitterService twitterService;
	protected ArrayList<TwitterStatus> statuses;
	private YFrogTwitterException toHandle;
	
	private int page = 1;

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
				if (twitterUpdate) {
					page = 1;
				}
				
				statuses = getStatuses(page, DEFAULT_PAGE_SIZE);
			} catch (YFrogTwitterException e) {
				statuses = new ArrayList<TwitterStatus>(0);
				toHandle = e;
				showDialog(AlertUtils.ALERT_TWITTER_ERROR);
			}
		}
		
		int selected = -1;
		
		if (getListView() != null) {
			selected = getSelectedItemPosition();
		}
		
		setListAdapter(new TwitterStatusAdapter<TwitterStatus>(this, statuses));

		if (selected > -1) {
			setSelection(selected);
		}
		registerForContextMenu(getListView());
	}

	private void appendList() {
		try {
			ArrayList<TwitterStatus> appended = getStatuses(page, DEFAULT_PAGE_SIZE);
			statuses.addAll(appended);
		} catch (YFrogTwitterException e) {
			toHandle = e;
			showDialog(AlertUtils.ALERT_TWITTER_ERROR);
		}
		
		createList(false);
	}

	private boolean isNeedReload() {
		return (++attempts % ATTEMPTS_TO_RELOAD == 0);
	}

	protected abstract ArrayList<TwitterStatus> getStatuses(int page, int count) throws YFrogTwitterException;
	
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
		getMenuInflater().inflate(R.menu.common_add_tweet, menu);
		getMenuInflater().inflate(R.menu.common_more_tweets, menu);
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
		case R.id.more_tweets:
			if (!isNoMoreItems()) {
				page++;
				appendList();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.more_tweets);
		item.setEnabled(!isNoMoreItems());
		return super.onPrepareOptionsMenu(menu);
	}
	
	private boolean isNoMoreItems() {
		return statuses.size() < page * DEFAULT_PAGE_SIZE && page * DEFAULT_PAGE_SIZE > MAX_COUNT;
	}
}