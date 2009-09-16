/**
 * 
 */
package com.codeminders.yfrog.android.view;

import java.util.ArrayList;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * @author idemydenko
 *
 */
public class LoginActivity extends ListActivity {
	private AccountService accountService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountService = ServiceFactory.getAccountService();
		
		ArrayList<Account> accounts = accountService.getAllAccounts();
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAccountsNames(accounts)));
		getListView().setTextFilterEnabled(true);
	}
	
	private String[] getAccountsNames(ArrayList<Account> accounts) {
		int size = accounts.size();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = accounts.get(i).getNickname();
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
			Toast.makeText(this, "Add account", Toast.LENGTH_SHORT);
			Intent intent = new Intent(this, AddAccountActivity.class);
			startActivity(intent);
			return true;
		case R.id.login:
			Toast.makeText(this, "Login", Toast.LENGTH_SHORT);
			return true;			
		}
		return false;
	}
}
