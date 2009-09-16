/**
 * 
 */
package com.codeminders.yfrog.android.view;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.model.Account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author idemydenko
 *
 */
public class AddAccountActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_account);
		
		Button button = (Button) findViewById(R.id.create_button);
		button.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Account account = new Account();
		String nickname = ((EditText) findViewById(R.id.nickname)).getText().toString();
		String password = ((EditText) findViewById(R.id.password)).getText().toString();
		String email = ((EditText) findViewById(R.id.email)).getText().toString();
		String oauthKey = ((EditText) findViewById(R.id.oauth_key)).getText().toString();
		
		account.setNickname(nickname);
		account.setPassword(password);
		account.setEmail(email);
		account.setOauthKey(oauthKey);
		
		ServiceFactory.getAccountService().addAccount(account);
	}
}
