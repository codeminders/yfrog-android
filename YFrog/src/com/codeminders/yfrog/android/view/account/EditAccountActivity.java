/**
 * 
 */
package com.codeminders.yfrog.android.view.account;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.StringUtils;
import com.codeminders.yfrog.android.controller.service.AccountService;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author idemydenko
 *
 */
public class EditAccountActivity extends Activity implements OnClickListener {
	public static final String KEY_EDIT = "isEdit";
	public static final String KEY_EDITABLE_ID = "id";
	
	private static final int ALERT_NICKNAME = 0;
	private static final int ALERT_PASSWORD = 1;
	private static final int ALERT_EMAIL = 2;
	private static final int ALERT_ACCOUNT_NOT_UNIQUE = 3;
	
	AccountService accountService;
	private Account editable = null;
	private boolean isEdit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.edit_account);
		
		accountService = ServiceFactory.getAccountService();
		
		Button button = (Button) findViewById(R.id.save_button);
		button.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			isEdit = extras.getBoolean(KEY_EDIT);
			if (isEdit) {
				int editableId = extras.getInt(KEY_EDITABLE_ID);
				editable = accountService.getAccount(editableId);
				
				EditText nickname = (EditText) findViewById(R.id.nickname);
				EditText password = (EditText) findViewById(R.id.password);
				EditText email = (EditText) findViewById(R.id.email);
				EditText oauthKey = (EditText) findViewById(R.id.oauth_key);
				
				nickname.setText(editable.getNickname());
				password.setText(editable.getPassword());
				email.setText(editable.getEmail());
				oauthKey.setText(editable.getOauthKey());
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		String nickname = ((EditText) findViewById(R.id.nickname)).getText().toString();
		
		if (StringUtils.isEmpty(nickname)) {
			showDialog(ALERT_NICKNAME);
			return;
		}
		
		String password = ((EditText) findViewById(R.id.password)).getText().toString();
		
		if (StringUtils.isEmpty(nickname)) {
			showDialog(ALERT_PASSWORD);
			return;			
		}

		String email = ((EditText) findViewById(R.id.email)).getText().toString();
		
		if (!StringUtils.isEmail(email)) {
			showDialog(ALERT_EMAIL);
			return;	
		}
		
		String oauthKey = ((EditText) findViewById(R.id.oauth_key)).getText().toString();

		Account account = new Account();
		account.setNickname(nickname);
		account.setPassword(password);
		account.setEmail(email);
		account.setOauthKey(oauthKey);

		
		if (isEdit) {
			account.setId(editable.getId());

			if (!accountService.isAccountUnique(account)) {
				showDialog(ALERT_ACCOUNT_NOT_UNIQUE);
				return;
			}

			accountService.updateAccount(account);
			editable = null;
			Toast.makeText(getApplicationContext(), R.string.account_edit_updated, Toast.LENGTH_SHORT).show(); //NEED TO SHOW ON Parent activity screen
		} else {
			if (!accountService.isAccountUnique(account)) {
				showDialog(ALERT_ACCOUNT_NOT_UNIQUE);
				return;
			}

			accountService.addAccount(account);
			Toast.makeText(getApplicationContext(), R.string.account_edit_created, Toast.LENGTH_SHORT).show();
		}
		
		finish();
	}

	
	@Override
	protected Dialog onCreateDialog(int id) {
		Builder dialogBuilder = new AlertDialog.Builder(EditAccountActivity.this)
		.setTitle(R.string.account_edit_dialog_title)
		.setNeutralButton(R.string.account_edit_dialog_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		switch (id) {
		case ALERT_NICKNAME:
			dialogBuilder.setMessage(R.string.account_edit_dialog_message_nickname);
			break;
		case ALERT_PASSWORD:
			dialogBuilder.setMessage(R.string.account_edit_dialog_message_passwd);
			break;
		case ALERT_EMAIL:
			dialogBuilder.setMessage(R.string.account_edit_dialog_message_email);
			break;
		case ALERT_ACCOUNT_NOT_UNIQUE:
			dialogBuilder.setMessage(R.string.account_edit_dialog_message_not_unique);
			break;
		}
		return dialogBuilder.create();
	}
}
