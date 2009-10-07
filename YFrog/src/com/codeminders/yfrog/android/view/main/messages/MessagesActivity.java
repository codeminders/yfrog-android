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
import com.codeminders.yfrog.android.view.main.adapter.TwitterDirectMessageAdapter;

import android.app.ListActivity;
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
	public static final int MENU_DELETE = 0;

	private TwitterService twitterService;
	private ArrayList<TwitterDirectMessage> messages;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		twitterService = ServiceFactory.getTwitterService();

		createMessagesList();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		createMessagesList();
	}
	
	private void createMessagesList() {
		try {
			messages = twitterService.getDirectMessages();
		} catch (Exception e) {
		}

		setListAdapter(new TwitterDirectMessageAdapter<TwitterDirectMessage>(
				this, messages));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.messages_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reload_messages:
			createMessagesList();
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
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			createMessagesList();
			
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
