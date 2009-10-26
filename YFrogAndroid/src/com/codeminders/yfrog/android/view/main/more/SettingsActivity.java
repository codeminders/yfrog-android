/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.util.StringUtils;

/**
 * @author idemydenko
 *
 */
public class SettingsActivity extends Activity implements OnCheckedChangeListener, OnClickListener {
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
		
		RadioButton radioButton = (RadioButton) findViewById(R.id.st_fwd_email);
		radioButton.setChecked(account.getForwardType() == Account.FORWARD_BY_EMAIL);
		radioButton.setOnClickListener(this);
		
		radioButton = (RadioButton) findViewById(R.id.st_fwd_sms);
		radioButton.setChecked(account.getForwardType() == Account.FORWARD_BY_SMS);
		radioButton.setOnClickListener(this);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.st_locations:
			account.setPostLocationStatus(isChecked);
			
			break;

		case R.id.st_scale:
			account.setScaleImage(isChecked);
			break;
		}
		updateAccount();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.st_fwd_email:
			account.setForwardType(Account.FORWARD_BY_EMAIL);
			break;
		case R.id.st_fwd_sms:
			account.setForwardType(Account.FORWARD_BY_SMS);
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
