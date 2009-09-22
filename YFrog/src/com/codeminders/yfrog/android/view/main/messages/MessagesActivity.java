/**
 * 
 */
package com.codeminders.yfrog.android.view.main.messages;

import java.util.ArrayList;

import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterDirectMessage;
import com.codeminders.yfrog.android.view.main.adapter.TwitterDirectMessageAdapter;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * @author idemydenko
 *
 */
public class MessagesActivity extends ListActivity {
	private TwitterService twitterService;
	private ArrayList<TwitterDirectMessage> messages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterService = ServiceFactory.getTwitterService();
		
		createMessagesList();
	}
	
	private void createMessagesList() {
		try {
			messages = twitterService.getDirectMessages();
		} catch (Exception e) {
		}
		
		
		setListAdapter(new TwitterDirectMessageAdapter<TwitterDirectMessage>(this, messages));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());

	}
}
