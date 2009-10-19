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
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.DialogUtils;
import com.codeminders.yfrog.android.util.async.AsyncTwitterUpdater;
import com.codeminders.yfrog.android.view.adapter.TwitterStatusAdapter;
import com.codeminders.yfrog.android.view.message.*;

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
	protected ArrayList<TwitterStatus> statuses = new ArrayList<TwitterStatus>(0);

	private int page = 1;

	public AbstractTwitterStatusesListActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitterService = ServiceFactory.getTwitterService();

		createList(true, false);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		createList(false, false);

	}

	@Override
	protected void onResume() {
		super.onResume();
		createList(false, false);
	}

	private void createList(final boolean twitterUpdate, final boolean append) {
		if (twitterUpdate) {
			attempts = 1;
		}

		boolean needReload = twitterUpdate || isNeedReload();

		if (needReload) {
			if (twitterUpdate && !append) {
				page = 1;
			}

			new AsyncTwitterUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					if (append) {
						statuses.addAll(getStatuses(page, DEFAULT_PAGE_SIZE));
					} else {
						statuses = getStatuses(1, DEFAULT_PAGE_SIZE * page);
					}					
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

	private boolean isNeedReload() {
		return (++attempts % ATTEMPTS_TO_RELOAD == 0);
	}

	protected abstract ArrayList<TwitterStatus> getStatuses(int page, int count)
			throws YFrogTwitterException;

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
		getMenuInflater().inflate(R.menu.common_more_tweets, menu);
		return super.onCreateOptionsMenu(menu);

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reload_list:
			createList(true, false);
			return true;
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
			return true;
		case R.id.more_tweets:
			if (!isNoMoreItems()) {
				page++;
				createList(true, true);
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
		return statuses.size() < page * DEFAULT_PAGE_SIZE
				&& page * DEFAULT_PAGE_SIZE > MAX_COUNT;
	}
}