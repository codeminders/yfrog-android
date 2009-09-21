/**
 * 
 */
package com.codeminders.yfrog.android.view.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * @author idemydenko
 *
 */
public class ListAccountsActivity extends ListActivity {
	private static final int MENU_DELETE = 0;
	private static final int MENU_EDIT = 1;
	
	private AccountService accountService;
	
	private ArrayList<Account> accounts;
	
	private Account toDelete = null;
	
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
			result[i] = accounts.get(i).getNickname();
		}
		
		return result;
	}

	private Account getAccountByNickname(String nickname) {
		Account acc = new Account();
		acc.setNickname(nickname);
		int pos = Collections.binarySearch(accounts, acc, new Comparator<Account> () {
			public int compare(Account object1, Account object2) {
				return object1.getNickname().equals(object2.getNickname()) ? 0 : 1;
			}
		});
		
		return accounts.get(pos);
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
			Account account = getSelectedAccount(getSelectedItemPosition());
			try {
				accountService.login(account);
			} catch (YFrogTwitterException e) {
				
			}
			startActivity(new Intent(this, MainTabActivity.class));
			return true;			
		}
		return false;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, MENU_DELETE, 0, R.string.ctx_menu_delete);
		menu.add(0, MENU_EDIT, 0, R.string.ctx_menu_edit);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		String title = ((TextView) info.targetView).getText().toString();
		Account account = getAccountByNickname(title);
		switch (item.getItemId()) {
			case MENU_DELETE :
				toDelete = account;
				showDialog(0);
				return true;
			case MENU_EDIT :
				Intent intent = new Intent(this, EditAccountActivity.class);
				intent.putExtra(EditAccountActivity.KEY_EDIT, true);
				intent.putExtra(EditAccountActivity.KEY_EDITABLE_ID, account.getId());
				startActivity(intent);
				return true;
		}		
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(ListAccountsActivity.this)
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
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Account account = getSelectedAccount(position);
		try {
		accountService.login(account);
		} catch (YFrogTwitterException e) {
		}
		startActivity(new Intent(this, MainTabActivity.class));
	}
	
	private Account getSelectedAccount(int position) {
		if (position > -1) {
			return accounts.get(position);
		}
		return null;
	}
}
