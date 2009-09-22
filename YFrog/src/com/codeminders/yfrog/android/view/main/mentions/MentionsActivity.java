/**
 * 
 */
package com.codeminders.yfrog.android.view.main.mentions;

import java.util.ArrayList;

import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.view.main.adapter.TwitterStatusAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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

}
