/**
 * 
 */
package com.codeminders.yfrog.android.view.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogException;
import com.codeminders.yfrog.android.YFrogTwitterAuthException;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.util.async.AsyncYFrogUpdater;

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
			TextView accountName = (TextView) findViewById(R.id.ae_name);
			
			EditText nickname = (EditText) findViewById(R.id.ae_nickname);
			EditText password = (EditText) findViewById(R.id.ae_password);
			TextView oauthStatus = (TextView) findViewById(R.id.ae_oath_status);
			
			nickname.setText(editable.getUsername());
			accountName.setText(editable.getUsername());
			password.setText(editable.getPassword());
			setAuthMethod(editable.getAuthMethod());
			oauthStatus.setText(editable.isOAuthVerified() ? 
					R.string.ae_oauth_status_authorized : R.string.ae_oauth_status_need_authorize);
		} else {
			TableLayout layout = (TableLayout) findViewById(R.id.ae_account_name);
			layout.setVisibility(View.GONE);
			editable = new Account();
		}
		
		setTitle(isEdit ? R.string.edit_account_activity_title : R.string.add_account_activity_title);

		setAuthMethodListener((RadioButton) findViewById(R.id.ae_common_method_rbutton));
		setAuthMethodListener((RadioButton) findViewById(R.id.ae_oauth_method_rbutton));
		
		Button button = (Button) findViewById(R.id.ae_save_button);
		button.setText(!editable.isOAuthVerified() && editable.getAuthMethod() == Account.METHOD_OAUTH ? R.string.next : R.string.save);
		Drawable d = !editable.isOAuthVerified() && editable.getAuthMethod() == Account.METHOD_OAUTH ?
				getResources().getDrawable(R.drawable.next) : getResources().getDrawable(R.drawable.save);
		button.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
		button.setOnClickListener(this);
		
		TextView textView = (TextView) findViewById(R.id.ae_oauth_desc);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private void setAuthMethodListener(RadioButton radioButton) {
		if (radioButton.isChecked()) {
			setShowLayout(radioButton.getId());
		}
		radioButton.setOnClickListener(authMethodListener);		
	}
	
	private void setShowLayout(int radioId) {
		Button button = (Button) findViewById(R.id.ae_save_button);
		View common = (View) findViewById(R.id.ae_common_layout);
		View oauth = (View) findViewById(R.id.ae_oauth_layout);
		switch (radioId) {
		case R.id.ae_common_method_rbutton:
			common.setVisibility(View.VISIBLE);
			oauth.setVisibility(View.GONE);
			button.setText(R.string.save);
			Drawable d = getResources().getDrawable(R.drawable.save);
			button.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);

			break;

		case R.id.ae_oauth_method_rbutton:
			common.setVisibility(View.GONE);
			oauth.setVisibility(View.VISIBLE);
			if (editable.getOauthStatus() != Account.OAUTH_STATUS_VERIFIED) { 
				button.setText(R.string.next);
				d = getResources().getDrawable(R.drawable.next);
				button.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null);
			}
			
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(button.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
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
			
			editable.setUsername(nickname);
			editable.setPassword(password);

			if (!accountService.isAccountUnique(editable)) {
				showDialog(ALERT_ACCOUNT_NOT_UNIQUE);
				return;
			}
		} 

		editable.setAuthMethod(authMethod);

		if (editable.isNeedOAuthAuthorization()) {
			new AsyncYFrogUpdater(this) {
				String url = null;
				
				@Override
				protected void doUpdate() throws YFrogException {
					saveOrUpdate();
					url = accountService.getOAuthAuthorizationURL(editable);
				}
				
				@Override
				protected void doAfterUpdate() {
					Intent i = new Intent();
					i.setAction(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			}.update();
		} else {
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogException {
					saveOrUpdate();
				}
				
				protected void doAfterUpdate() {
					Toast.makeText(getApplicationContext(), isEdit ? R.string.ae_updated : R.string.ae_created, Toast.LENGTH_SHORT).show();
					editable = null;
					finish();
				}
			}.update();
		}
	}

	private void saveOrUpdate() throws YFrogException {
		if (isEdit) {
			accountService.updateAccount(editable);
		} else {
			editable = accountService.addAccount(editable);
		}		
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
		default:
			return AlertUtils.createErrorAlert(this, id);

		}
		return dialogBuilder.create();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Uri uri = getIntent().getData();
		
		if (accountService.isOAuthCallback(uri)) {
			Account toVerify = accountService.getWatingForOAuthVerificationAccount();
			try {
				toVerify.setOauthToken(accountService.getToken(uri));
				accountService.verifyOAuthAuthorization(toVerify, accountService.getVerifier(uri));
			} catch (YFrogTwitterAuthException e) {
				Toast.makeText(getApplicationContext(), R.string.ae_oauth_message_not_unique, Toast.LENGTH_LONG).show();
			} catch (YFrogTwitterException e) {
				showDialog(e.getErrorCode());
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
