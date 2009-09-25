/**
 * 
 */
package com.codeminders.yfrog.android.view.main.home;

import java.util.ArrayList;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.view.main.adapter.TwitterStatusAdapter;
import com.codeminders.yfrog.android.view.message.MessageDetailsActivity;
import com.codeminders.yfrog.android.view.message.WriteActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author idemydenko
 *
 */
public class HomeActivity extends ListActivity {
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
			statuses = twitterService.getFriendsTimeline();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		setListAdapter(new TwitterStatusAdapter<TwitterStatus>(this, statuses));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, MessageDetailsActivity.class);
		TwitterStatus status = getSelected(position); 
		
		intent.putExtra(MessageDetailsActivity.KEY_WRITER_NICKNAME, status.getUser().getUsername());
		intent.putExtra(MessageDetailsActivity.KEY_WRITER_FULLNAME, status.getUser().getFullname());
		intent.putExtra(MessageDetailsActivity.KEY_WRITER_PROFILE_IMAGE_URL, status.getUser().getProfileImageURL());
//		intent.putExtra(MessageDetailsActivity.KEY_MESSAGE_ID, status.get);
		intent.putExtra(MessageDetailsActivity.KEY_MESSAGE_TEXT, status.getText());
		
		startActivity(intent);
	}
	
	private TwitterStatus getSelected(int position) {
		if (position > -1) {
			return statuses.get(position);
		}
		return null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_tab, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, WriteActivity.class);
		startActivity(intent);
		return true;
	}
}
