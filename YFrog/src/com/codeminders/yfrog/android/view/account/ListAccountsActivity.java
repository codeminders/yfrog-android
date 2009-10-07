/**
 * 
 */
package com.codeminders.yfrog.android.view.account;

import java.util.ArrayList;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterAuthException;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.view.main.MainTabActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * @author idemydenko
 *
 */
public class ListAccountsActivity extends ListActivity {
	private static final int MENU_DELETE = 0;
	private static final int MENU_EDIT = 1;
	
	private static final int ALERT_DELETE = 0;
	private static final int ALERT_AUTH_FAILED = 2;
	
	private AccountService accountService;
	
	private ArrayList<Account> accounts;
	
	private Account toDelete = null;
	
	private YFrogTwitterException toHandle = null; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountService = ServiceFactory.getAccountService();
		
		createAccountsList();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		createAccountsList();
	}
	
	private void createAccountsList() {
		accounts = accountService.getAllAccounts();
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAccountsNames(accounts)));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	}
	
	private String[] getAccountsNames(ArrayList<Account> accounts) {
		int size = accounts.size();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = accounts.get(i).getName();
		}
		
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.account_list, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_account:
			Intent intent = new Intent(this, EditAccountActivity.class);
			startActivity(intent);
			return true;
		case R.id.login:
			login(getSelectedItemPosition());
			return true;			
		}
		return false;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, MENU_DELETE, 0, R.string.delete);
		menu.add(0, MENU_EDIT, 0, R.string.edit);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		Account account = accounts.get(info.position);

		switch (item.getItemId()) {
			case MENU_DELETE :
				toDelete = account;
				showDialog(ALERT_DELETE);
				return true;
			case MENU_EDIT :
				Intent intent = new Intent(this, EditAccountActivity.class);
				intent.putExtra(EditAccountActivity.KEY_EDIT, true);
				intent.putExtra(EditAccountActivity.KEY_EDITABLE, account);
				startActivity(intent);
				return true;
		}		
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialiog = null;
		switch (id) {
		case ALERT_DELETE:
			dialiog = new AlertDialog.Builder(ListAccountsActivity.this)
			.setTitle(R.string.account_delete_dialog_title)
			.setMessage(R.string.account_delete_dialog_message)
			.setPositiveButton(R.string.account_delete_dialog_btn_yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					accountService.deleteAccount(toDelete);
					toDelete = null;
					createAccountsList();
					Toast.makeText(ListAccountsActivity.this, R.string.account_delete_message, Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton(R.string.account_delete_dialog_btn_no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.create();
			break;
			
		case AlertUtils.ALERT_TWITTER_ERROR:
			dialiog = AlertUtils.createTwitterErrorAlert(this, toHandle);
			toHandle = null;
			break;
			
		case ALERT_AUTH_FAILED:
			dialiog = new AlertDialog.Builder(ListAccountsActivity.this)
			.setTitle(R.string.account_login_failed_title)
			.setMessage(R.string.account_login_failed_msg)
			.setNeutralButton(R.string.account_login_failed_btn, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.create();
			break;
		}
		return dialiog;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		login(position);
	}
	
	private Account getSelectedAccount(int position) {
		if (position > -1) {
			return accounts.get(position);
		}
		return null;
	}
	
	private void login(int itemPos) {
		Account account = getSelectedAccount(itemPos);
		
		if (account == null) {
			return;
		}
		
		try {
			accountService.login(account);
		} catch (YFrogTwitterAuthException e) {
			showDialog(ALERT_AUTH_FAILED);
			return;
		} catch (YFrogTwitterException e) {
			toHandle = e;
			showDialog(AlertUtils.ALERT_TWITTER_ERROR);
			return;
		}
		startActivity(new Intent(this, MainTabActivity.class));
	}	
}
