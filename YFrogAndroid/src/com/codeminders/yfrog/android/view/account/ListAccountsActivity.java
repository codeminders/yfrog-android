/**
 * 
 */
package com.codeminders.yfrog.android.view.account;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogProperties;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog.android.view.main.MainTabActivity;

/**
 * @author idemydenko
 * 
 */
public class ListAccountsActivity extends ListActivity {
	private static final int MENU_DELETE = 0;
	private static final int MENU_EDIT = 1;
	private static final int MENU_LOGIN = 2;

	private static final int ALERT_DELETE = 0;
	private static final int ALERT_AUTH_FAILED = 2;

	private static final String KEY_LAST_LOGGED = "lastLogged";

	private YFrogProperties properties;
	
	private AccountService accountService;
	private ArrayList<Account> accounts;
	private Account toDelete = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		accountService = ServiceFactory.getAccountService();
		properties = YFrogProperties.getProperies();

		createAccountsList();
		
		if (getLastLogged() != null) {
			login(getLastLogged());
			return;
		}
		
		if (accounts.size() == 0) {
			addAccount();
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		createAccountsList();
	}

	@Override
	protected void onResume() {
		super.onResume();
		createAccountsList();
	}

	private void createAccountsList() {
		accounts = accountService.getAllAccounts();

		setContentView(R.layout.list_accounts);
		
		setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_single_choice, getAccountsNames(accounts)));

		getListView().setItemsCanFocus(false);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		if (accounts.size() > 0) {
			long lastLogedId = properties.getLong(KEY_LAST_LOGGED);
			int position = 0;
			for (int i=0; i<accounts.size(); i++)
				if (accounts.get(i).getId() == lastLogedId)
					position = i;
			getListView().setItemChecked(position, true);
		}
		
		registerForContextMenu(getListView());
		setTitle(R.string.account_list_activity_title);
	}

	private Account getLastLogged() {
		long lastLoggedId = 0;
		lastLoggedId = properties.getLong(KEY_LAST_LOGGED);
		return accountService.getAccount(lastLoggedId);
	}

	private void saveLastLogged(Account account) {
		properties.putLong(KEY_LAST_LOGGED, account.getId());
	}

	private void deleteLastLogged(Account account) {
		properties.delete(KEY_LAST_LOGGED);
	}

	private String[] getAccountsNames(ArrayList<Account> accounts) {
		int size = accounts.size();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = accounts.get(i).getUsername();
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
		int position = getListView().getCheckedItemPosition();
			
		switch (item.getItemId()) {
		case R.id.add_account:
			addAccount();
			return true;
		case R.id.login_account:
			login(position);
			return true;
		case R.id.edit_account:
			editAccount(getSelectedAccount(position));
			return true;
		case R.id.delete_account:
			deleteAccount(getSelectedAccount(position));
			return true;
		}
		return false;

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, MENU_LOGIN, 0, R.string.login);
		menu.add(0, MENU_EDIT, 0, R.string.edit);
		menu.add(0, MENU_DELETE, 0, R.string.delete);
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		Account account = accounts.get(info.position);

		switch (item.getItemId()) {
		case MENU_DELETE:
			deleteAccount(account);
			return true;
		case MENU_EDIT:
			editAccount(account);
			return true;
		case MENU_LOGIN:
			login(account);
			return true;			
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case ALERT_DELETE:
			dialog = new AlertDialog.Builder(ListAccountsActivity.this)
					.setTitle(R.string.account_delete_dialog_title).setMessage(
							R.string.account_delete_dialog_message)
					.setPositiveButton(R.string.account_delete_dialog_btn_yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									accountService.deleteAccount(toDelete);
									deleteLastLogged(toDelete);
									toDelete = null;
									createAccountsList();
									Toast.makeText(ListAccountsActivity.this,
											R.string.account_delete_message,
											Toast.LENGTH_SHORT).show();
								}
							}).setNegativeButton(
							R.string.account_delete_dialog_btn_no,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create();
			break;

		case ALERT_AUTH_FAILED:
			dialog = new AlertDialog.Builder(ListAccountsActivity.this)
					.setTitle(R.string.account_login_failed_title).setMessage(
							R.string.account_login_failed_msg)
					.setNeutralButton(R.string.account_login_failed_btn,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create();
			break;
		default:
			dialog = AlertUtils.createErrorAlert(this, id);
			break;
		}
		return dialog;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		setSelection(position);
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

		login(account);
	}

	private void login(final Account account) {
		new AsyncYFrogUpdater(this) {
			@Override
			protected void doUpdate() throws YFrogTwitterException {
				accountService.login(account);
			}
			
			protected void doAfterUpdate() {
				saveLastLogged(account);
				startActivity(new Intent(ListAccountsActivity.this, MainTabActivity.class));
			}
		}.update();		
	}

	private void deleteAccount(Account account) {
		if (account == null) {
			return;
		}

		toDelete = account;
		showDialog(ALERT_DELETE);
	}

	private void editAccount(Account account) {
		if (account == null) {
			return;
		}

		Intent intent = new Intent(this, EditAccountActivity.class);
		intent.putExtra(EditAccountActivity.KEY_EDIT, true);
		intent.putExtra(EditAccountActivity.KEY_EDITABLE, account);
		startActivity(intent);
	}

	private void addAccount() {
		Intent intent = new Intent(this, EditAccountActivity.class);
		properties.delete(KEY_LAST_LOGGED);
		startActivity(intent);		
	}
}
