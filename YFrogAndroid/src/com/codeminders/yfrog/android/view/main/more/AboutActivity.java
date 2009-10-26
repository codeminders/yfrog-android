/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import android.app.Activity;
import android.os.Bundle;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.util.StringUtils;

/**
 * @author idemydenko
 *
 */
public class AboutActivity extends Activity {
	private TwitterService twitterService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		twitterService = ServiceFactory.getTwitterService();
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about);
		
		setTitle(StringUtils.formatTitle(twitterService.getLoggedUser().getUsername(),
				getResources().getString(R.string.ab_title)));
	}
}
