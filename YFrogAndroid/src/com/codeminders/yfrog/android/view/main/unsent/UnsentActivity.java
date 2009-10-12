/**
 * 
 */
package com.codeminders.yfrog.android.view.main.unsent;

import java.util.ArrayList;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.controller.service.UnsentMessageService;
import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.model.UnsentMessage;
import com.codeminders.yfrog.android.view.account.EditAccountActivity;
import com.codeminders.yfrog.android.view.adapter.UnsentMessageAdapter;
import com.codeminders.yfrog.android.view.message.EditUnsentMessageActivity;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * @author idemydenko
 *
 */
// TODO May be need refresh here, but not from DB
public class UnsentActivity extends ListActivity {
	private static final int MENU_SEND = 0;
	private static final int MENU_EDIT = 1;
	private static final int MENU_DELETE = 2;
	
	private UnsentMessageService unsentMessageService;
	private AccountService accountService;
	private TwitterService twitterService;
	
	private ArrayList<UnsentMessage> messages;
	private Account loggedAccount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		unsentMessageService = ServiceFactory.getUnsentMessageService();
		accountService = ServiceFactory.getAccountService();
		twitterService = ServiceFactory.getTwitterService();
		
		loggedAccount = accountService.getLogged();
		
		createList();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		createList();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		createList();
	}
	private void createList() {
		messages = unsentMessageService.getUnsentMessagesForAccount(loggedAccount.getId());
		
		setListAdapter(new UnsentMessageAdapter<UnsentMessage>(this, messages));
		registerForContextMenu(getListView());
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, MENU_SEND, 0, R.string.send);
		menu.add(0, MENU_EDIT, 0, R.string.edit);
		menu.add(0, MENU_DELETE, 0, R.string.delete);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		UnsentMessage message = messages.get(info.position);
		
		switch(item.getItemId()) {
		case MENU_SEND:
			try {
				twitterService.sendUnsentMessage(message);
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
				return false;
			}
			createList();
			return true;
		case MENU_DELETE:
			unsentMessageService.deleteUnsentMessage(message.getId());
			createList();
			return true;
		case MENU_EDIT:
			Intent intent = new Intent(this, EditUnsentMessageActivity.class);
			intent.putExtra(EditUnsentMessageActivity.KEY_UNSENT_MESSAGE, message);
			startActivity(intent);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.unsent_tab, menu);
		inflater.inflate(R.menu.common_add_tweet, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.umt_send_all:
			try {
				twitterService.sendAllUnsentMessages();
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			createList();
			return true;
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
			return true;

		}
		return false;
	}

}
