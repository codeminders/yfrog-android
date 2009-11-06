/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.util.StringUtils;

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
	
	private void updateAccount() {
		try {
			accountService.updateAccount(account, false);
		} catch (YFrogTwitterException e) {
			// TODO: We don't use user and apssword verification
		}
	}
}
