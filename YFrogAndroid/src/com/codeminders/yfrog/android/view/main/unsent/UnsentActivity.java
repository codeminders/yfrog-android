/**
 * 
 */
package com.codeminders.yfrog.android.view.main.unsent;

import java.util.ArrayList;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.model.*;
import com.codeminders.yfrog.android.util.DialogUtils;
import com.codeminders.yfrog.android.util.async.AsyncTwitterUpdater;
import com.codeminders.yfrog.android.view.adapter.UnsentMessageAdapter;
import com.codeminders.yfrog.android.view.message.*;

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
		
		final UnsentMessage message = messages.get(info.position);
		
		switch(item.getItemId()) {
		case MENU_SEND:
			
			new AsyncTwitterUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					twitterService.sendUnsentMessage(message);
				}
				
				protected void doAfterUpdate() {
					createList();
					Toast.makeText(getApplicationContext(), R.string.msg_sent, Toast.LENGTH_SHORT).show();
				}
			}.update();
			
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
			new AsyncTwitterUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					twitterService.sendAllUnsentMessages();
				}
				
				protected void doAfterUpdate() {
					createList();
					Toast.makeText(getApplicationContext(), R.string.msg_sent, Toast.LENGTH_SHORT).show();
				}
			}.update();
			
			return true;
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
			return true;

		}
		return false;
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
}
