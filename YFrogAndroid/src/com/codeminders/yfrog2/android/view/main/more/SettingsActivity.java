/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.more;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.controller.service.AccountService;
import com.codeminders.yfrog2.android.controller.service.ServiceFactory;
import com.codeminders.yfrog2.android.model.Account;
import com.codeminders.yfrog2.android.util.StringUtils;

/**
 * @author idemydenko
 *
 */
public class SettingsActivity extends Activity implements OnCheckedChangeListener {
	private AccountService accountService;
	
	private Account account;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountService = ServiceFactory.getAccountService();
		
		account = accountService.getLogged();
		
		setContentView(R.layout.settings);
		setTitle(StringUtils.formatTitle(accountService.getLogged().getUsername(),
				getResources().getString(R.string.st_title)));
		
		CheckBox checkBox = (CheckBox) findViewById(R.id.st_locations);
		checkBox.setChecked(account.isPostLocation());
		checkBox.setOnCheckedChangeListener(this);
		
		checkBox = (CheckBox) findViewById(R.id.st_scale);
		checkBox.setChecked(account.isScaleImage());
		checkBox.setOnCheckedChangeListener(this);
		
		checkBox = (CheckBox) findViewById(R.id.st_update_loc);
		checkBox.setChecked(account.isUpdateProfileLocation());
		checkBox.setOnCheckedChangeListener(this);
		if (!account.isPostLocation()) {
			checkBox.setVisibility(View.GONE);
		}
		
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.st_locations:
			account.setPostLocationStatus(isChecked);
			if (!isChecked) {
				account.setUpdateProfileLocation(isChecked);
				CheckBox checkBox = (CheckBox) findViewById(R.id.st_update_loc);
				checkBox.setVisibility(View.GONE);
			} else {
				CheckBox checkBox = (CheckBox) findViewById(R.id.st_update_loc);
				checkBox.setChecked(account.isUpdateProfileLocation());
				checkBox.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.st_scale:
			account.setScaleImage(isChecked);
			break;
			
		case R.id.st_update_loc:
			account.setUpdateProfileLocation(isChecked);
			break;
		}
		updateAccount();
	}
	
	private void updateAccount() {
		try {
			accountService.updateAccount(account, false);
		} catch (YFrogTwitterException e) {
			// TODO: We don't use user and apssword verification
		}
	}
}
