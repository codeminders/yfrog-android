/**
 * 
 */
package com.codeminders.yfrog.view;

import com.codeminders.yfrog.controller.service.AccountService;
import com.codeminders.yfrog.controller.service.ServiceFactory;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author idemydenko
 *
 */
public class LoginActivity extends Activity {
	private AccountService accountService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accountService = ServiceFactory.getAccountService(getApplicationContext());
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		
	}
}
