/**
 * 
 */
package com.codeminders.yfrog.android.view.main.mentions;

import java.util.ArrayList;

import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.view.main.adapter.TwitterStatusAdapter;
import com.codeminders.yfrog.android.view.message.StatusDetailsActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author idemydenko
 *
 */
public class MentionsActivity extends ListActivity {
	private TwitterService twitterService;
	private ArrayList<TwitterStatus> statuses;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterService = ServiceFactory.getTwitterService();
		
		createStatusesList();
	}
	
	private void createStatusesList() {
		try {
			statuses = twitterService.getMentions();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		setListAdapter(new TwitterStatusAdapter<TwitterStatus>(this, statuses));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, StatusDetailsActivity.class);
		TwitterStatus status = getSelected(position); 
		
		intent.putExtra(StatusDetailsActivity.KEY_STATUS, status);
		
		startActivity(intent);
	}
	
	private TwitterStatus getSelected(int position) {
		if (position > -1) {
			return statuses.get(position);
		}
		return null;
	}
}
