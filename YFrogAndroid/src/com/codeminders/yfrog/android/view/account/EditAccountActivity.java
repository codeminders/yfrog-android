/**
 * 
 */
package com.codeminders.yfrog.android.view.account;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.StringUtils;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author idemydenko
 *
 */
public class EditAccountActivity extends Activity implements OnClickListener {
	public static final String KEY_EDIT = "isEdit";
	public static final String KEY_EDITABLE = "editable";

	private static final int ALERT_NICKNAME = 0;
	private static final int ALERT_PASSWORD = 1;
	private static final int ALERT_ACCOUNT_NOT_UNIQUE = 2;
	private static final int ALERT_NAME = 3;

	private AccountService accountService;
	private Account editable = null;
	private boolean isEdit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_account);
		accountService = ServiceFactory.getAccountService();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isEdit = extras.getBoolean(KEY_EDIT);
		}
		
		if (isEdit) {
			
			editable = (Account) extras.getSerializable(KEY_EDITABLE);
			
			EditText name = (EditText) findViewById(R.id.ae_name);
			EditText nickname = (EditText) findViewById(R.id.ae_nickname);
			EditText password = (EditText) findViewById(R.id.ae_password);
			TextView oauthStatus = (TextView) findViewById(R.id.ae_oath_status);
			
			name.setText(editable.getName());
			nickname.setText(editable.getNickname());
			password.setText(editable.getPassword());
			setAuthMethod(editable.getAuthMethod());
			oauthStatus.setText(editable.isOAuthVerified() ? 
					R.string.ae_oauth_status_authorized : R.string.ae_oauth_status_need_authorize);
		} else {
			editable = new Account();
		}
		
		setTitle(isEdit ? R.string.edit_account_activity : R.string.edit_account_activity);

		setAuthMethodListener((RadioButton) findViewById(R.id.ae_common_method_rbutton));
		setAuthMethodListener((RadioButton) findViewById(R.id.ae_oauth_method_rbutton));
		
		Button button = (Button) findViewById(R.id.ae_save_button);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.ae_oauth_auth_button);
		button.setVisibility(View.GONE);
	}

	private void setAuthMethodListener(RadioButton radioButton) {
		if (radioButton.isChecked()) {
			setShowLayout(radioButton.getId());
		}
		radioButton.setOnClickListener(authMethodListener);		
	}
	
	private void setShowLayout(int radioId) {
		View common = (View) findViewById(R.id.ae_common_layout);
		View oauth = (View) findViewById(R.id.ae_oauth_layout);
		switch (radioId) {
		case R.id.ae_common_method_rbutton:
			common.setVisibility(View.VISIBLE);
			oauth.setVisibility(View.GONE);
			break;

		case R.id.ae_oauth_method_rbutton:
			common.setVisibility(View.GONE);
			oauth.setVisibility(View.VISIBLE);

			break;
		}		
	}

	private void setAuthMethod(int method) {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.ae_auth_methods);
		switch (method) {
		case Account.METHOD_COMMON:
			radioGroup.check(R.id.ae_common_method_rbutton);
			break;
		case Account.METHOD_OAUTH:
			radioGroup.check(R.id.ae_oauth_method_rbutton);
			break;
		}
	}
	
	private int getAuthMethod() {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.ae_auth_methods);
		switch(radioGroup.getCheckedRadioButtonId()) {
		case R.id.ae_common_method_rbutton:
			return Account.METHOD_COMMON;

		case R.id.ae_oauth_method_rbutton:
			return Account.METHOD_OAUTH;
		}
		return Account.METHOD_COMMON;
	}
	
	@Override
	public void onClick(View v) {
		String name = ((EditText) findViewById(R.id.ae_name)).getText().toString();
		
		if (StringUtils.isEmpty(name)) {
			showDialog(ALERT_NAME);
			return;
		}

		int authMethod = getAuthMethod();
		String nickname = ((EditText) findViewById(R.id.ae_nickname)).getText().toString();
		String password = ((EditText) findViewById(R.id.ae_password)).getText().toString();
		
		if (authMethod == Account.METHOD_COMMON) {
			if (StringUtils.isEmpty(nickname)) {
				showDialog(ALERT_NICKNAME);
				return;
			}
			
			if (StringUtils.isEmpty(password)) {
				showDialog(ALERT_PASSWORD);
				return;			
			}
		} 

		
		editable.setNickname(nickname);
		editable.setPassword(password);
		editable.setName(name);
		editable.setAuthMethod(authMethod);

		if (!accountService.isAccountUnique(editable)) {
			showDialog(ALERT_ACCOUNT_NOT_UNIQUE);
			return;
		}

		if (isEdit) {
			accountService.updateAccount(editable);
		} else {
			editable = accountService.addAccount(editable);
		}
		
		if (editable.isNeedOAuthAuthorization()) {
			String url = null;
			
			try {
				url = accountService.getOAuthAuthorizationURL(editable);
			} catch (YFrogTwitterException e) {
				
			}
			
			Intent i = new Intent();
			i.setAction(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
			return;
		}
		
		Toast.makeText(getApplicationContext(), isEdit ? R.string.ae_updated : R.string.ae_created, Toast.LENGTH_SHORT).show();
		editable = null;
		finish();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Builder dialogBuilder = new AlertDialog.Builder(EditAccountActivity.this)
		.setTitle(R.string.ae_dialog_title)
		.setNeutralButton(R.string.ae_dialog_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		switch (id) {
		case ALERT_NICKNAME:
			dialogBuilder.setMessage(R.string.ae_dialog_message_nickname);
			break;
		case ALERT_PASSWORD:
			dialogBuilder.setMessage(R.string.ae_dialog_message_passwd);
			break;
		case ALERT_ACCOUNT_NOT_UNIQUE:
			dialogBuilder.setMessage(R.string.ae_dialog_message_not_unique);
			break;
		case ALERT_NAME:
			dialogBuilder.setMessage(R.string.ae_dialog_message_name);
			break;

		}
		return dialogBuilder.create();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Uri uri = getIntent().getData();
		
		if (accountService.isOAuthCallback(uri)) {
			try {
				Account toVerify = accountService.getWatingForOAuthVerificationAccount();
				toVerify.setOauthToken(accountService.getToken(uri));
				accountService.verifyOAuthAuthorization(toVerify, accountService.getVerifier(uri));
			} catch (YFrogTwitterException e) {
				return;
			}
			startActivity(new Intent(this, ListAccountsActivity.class));
			finish();
			
		}

	}
	
	OnClickListener authMethodListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setShowLayout(v.getId());
		}
	};
}
