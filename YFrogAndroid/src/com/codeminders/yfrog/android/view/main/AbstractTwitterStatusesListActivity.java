/**
 * 
 */
package com.codeminders.yfrog.android.view.main;

import java.util.ArrayList;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.view.adapter.TwitterStatusAdapter;
import com.codeminders.yfrog.android.view.message.StatusDetailsActivity;

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
public abstract class AbstractTwitterStatusesListActivity extends ListActivity {

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

		createStatusesList(false);
	}

	@Override
	protected void onRestart() {
		super.onResume();
		createStatusesList(false);

	}

	private void createStatusesList(boolean twitterUpdate) {
		try {
			statuses = getStatuses();
		} catch (YFrogTwitterException e) {
			statuses = new ArrayList<TwitterStatus>(0);
			toHandle = e;
			showDialog(AlertUtils.ALERT_TWITTER_ERROR);
		}

		setListAdapter(new TwitterStatusAdapter<TwitterStatus>(this, statuses));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	}

	protected abstract ArrayList<TwitterStatus> getStatuses() throws YFrogTwitterException;
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, StatusDetailsActivity.class);
		TwitterStatus status = getSelected(position);

		intent.putExtra(StatusDetailsActivity.KEY_STATUS, status);

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
}