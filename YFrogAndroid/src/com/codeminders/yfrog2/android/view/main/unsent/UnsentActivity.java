/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.unsent;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.controller.service.*;
import com.codeminders.yfrog2.android.model.Account;
import com.codeminders.yfrog2.android.model.UnsentMessage;
import com.codeminders.yfrog2.android.util.AlertUtils;
import com.codeminders.yfrog2.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog2.android.view.adapter.UnsentMessageAdapter;
import com.codeminders.yfrog2.android.view.message.EditUnsentMessageActivity;
import com.codeminders.yfrog2.android.view.message.WriteStatusActivity;

/**
 * @author idemydenko
 *
 */
// TODO May be need refresh here, but not from DB
public class UnsentActivity extends ListActivity {
	public static final String TAG = "unsent";

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
		setContentView(R.layout.unsent_messages_list);
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
			
			new AsyncYFrogUpdater(this) {
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
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					twitterService.sendAllUnsentMessages(UnsentActivity.this);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(AlertUtils.LOGOUT);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == AlertUtils.LOGOUT) {
			return AlertUtils.createLogoutAlert(this);
		}
		return super.onCreateDialog(id);
	}
}
