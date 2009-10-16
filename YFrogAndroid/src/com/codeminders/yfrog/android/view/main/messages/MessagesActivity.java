/**
 * 
 */
package com.codeminders.yfrog.android.view.main.messages;

import java.util.ArrayList;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterDirectMessage;
import com.codeminders.yfrog.android.view.adapter.TwitterDirectMessageAdapter;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * @author idemydenko
 * 
 */
public class MessagesActivity extends ListActivity {
	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;

	public static final int MENU_DELETE = 0;

	private TwitterService twitterService;
	private ArrayList<TwitterDirectMessage> messages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitterService = ServiceFactory.getTwitterService();

		createList(true);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		createList(true);
	}
	
	private void createList(boolean twitterUpdate) {
		if (twitterUpdate) {
			attempts = 1;
		}
		
		boolean needReload = twitterUpdate || isNeedReload();
		
		if (needReload) {
			try {
				messages = twitterService.getDirectMessages();
			} catch (YFrogTwitterException e) {
			}			
		}
		
		// TODO Do we need to update messages in other thread?
		setListAdapter(new TwitterDirectMessageAdapter<TwitterDirectMessage>(this, messages));
		registerForContextMenu(getListView());
	}

	private boolean isNeedReload() {
		return (++attempts % ATTEMPTS_TO_RELOAD == 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.messages_tab, menu);
		getMenuInflater().inflate(R.menu.common_refresh_list, menu);
		getMenuInflater().inflate(R.menu.common_add_tweet, menu);
		return true;
	}

	@Override
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
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, MENU_DELETE, 0, R.string.delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch (item.getItemId()) {
		case MENU_DELETE:
			TwitterDirectMessage toDelete = getSelected(info.position);
			
			try {
				twitterService.deleteDirectMessage(toDelete.getId());
				messages.remove(info.position);
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			createList(false); // View Adapter don't support add/remove operations from list
			
			return true;
		}
		return false;
	}
	
	private TwitterDirectMessage getSelected(int position) {
		if (position > -1) {
			return messages.get(position);
		}
		return null;
	}
}
