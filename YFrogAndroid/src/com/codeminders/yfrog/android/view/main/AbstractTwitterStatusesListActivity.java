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
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.*;
import com.codeminders.yfrog.android.util.async.AsyncTwitterUpdater;
import com.codeminders.yfrog.android.view.adapter.TwitterStatusAdapter;
import com.codeminders.yfrog.android.view.message.*;

/**
 * @author idemydenko
 * 
 */
public abstract class AbstractTwitterStatusesListActivity extends ListActivity {
	private static final String SAVED_STATUSES = "sstatuses";
	private static final String SAVED_ATTEMPTS = "sattempts";
	private static final String SAVED_PAGE = "spage";
	private static final String SAVED_SELECTED = "sselected";
	
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_COUNT = 3200;

	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;

	protected TwitterService twitterService;
	protected ArrayList<TwitterStatus> statuses = new ArrayList<TwitterStatus>(0);

	private int page = 1;

	protected int selected = -1;
	
	public AbstractTwitterStatusesListActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		twitterService = ServiceFactory.getTwitterService();
		super.onCreate(savedInstanceState);
		boolean restored = restoreState(savedInstanceState);
		
		createList(!restored, false);
		
	}
	
	private boolean restoreState(Bundle savedState) {
		if (savedState == null) {
			return false;
		}
		
		
		Serializable values = savedState.getSerializable(SAVED_STATUSES);
		if (values == null) {
			return false;
		}
		statuses = (ArrayList<TwitterStatus>) values;		
		
		int value = savedState.getInt(SAVED_ATTEMPTS);
		if (value < 0) {
			return false;
		}
		attempts = value;
		
		value = savedState.getInt(SAVED_PAGE);
		if (value < 1) {
			return false;
		}
		page = value;

		value = savedState.getInt(SAVED_SELECTED);
		if (value > -1 && value < statuses.size()) {
			selected = value;
		}

		return true;
	}
	
	private void saveState(Bundle savedState) {
		if (savedState == null) {
			return;
		}
		
		savedState.putSerializable(SAVED_STATUSES, statuses);
		savedState.putInt(SAVED_PAGE, page);
		savedState.putInt(SAVED_ATTEMPTS, attempts);
		savedState.putInt(SAVED_SELECTED, getSelectedItemPosition());
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
				
				@Override
				protected void doAfterError() {
					show();
				}
			}.update();
		} else {
			show();
		}
	}

	private void show() {
		

//		if (getListView() != null) {
//			selected = getSelectedItemPosition();
//		}

		setContentView(R.layout.twitter_statuses_list);
		
		setListAdapter(new TwitterStatusAdapter<TwitterStatus>(this, statuses));

		if (selected > -1) {
			setSelection(selected);
		}
		registerForContextMenu(getListView());
		setTitle(createTitle());
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

		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			
			if (extras != null) {
				Serializable serializable = extras.getSerializable(StatusDetailsActivity.KEY_STATUSES);
				if (serializable != null) {
					statuses = (ArrayList<TwitterStatus>) serializable;
				}
				
				int pos = extras.getInt(StatusDetailsActivity.KEY_STATUS_POS);
				
				if (pos > -1 && pos < statuses.size()) {
					selected = pos;
				}
				
			}
		}
	}
	
	protected TwitterStatus getSelected(int position) {
		if (position > -1) {
			return statuses.get(position);
		}
		return null;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return AlertUtils.createErrorAlert(this, id);
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
				selected = getSelectedItemPosition();
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
	
	protected String createTitle() {
		return StringUtils.formatTitle(twitterService.getLoggedUser().getUsername());
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}
}