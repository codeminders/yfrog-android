/**
 * 
 */
package com.codeminders.yfrog.android.view.main;

import android.R.bool;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.view.main.home.HomeActivity;
import com.codeminders.yfrog.android.view.main.mentions.MentionsActivity;
import com.codeminders.yfrog.android.view.main.messages.MessagesActivity;
import com.codeminders.yfrog.android.view.main.more.MoreActivity;
import com.codeminders.yfrog.android.view.main.unsent.UnsentActivity;

/**
 * @author idemydenko
 *
 */
public class MainTabActivity extends TabActivity {

	private String currentTab = null;
	private boolean created = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(createTitle());
				
		if (savedInstanceState != null) {
			currentTab = savedInstanceState.getString("currentTab"); 
		}
		
		
	}
	
	protected String createTitle() {
		return StringUtils.formatTitle(ServiceFactory.getTwitterService().getLoggedUser().getUsername());
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		if (created) {
			return;
		}
		
		TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost.newTabSpec(HomeActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_home_caption))
//				.setIndicator(null, getResources().getDrawable(R.drawable.yfrog_tab_selector))
				.setContent(new Intent(this, HomeActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(MentionsActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_mentions_caption))
				.setContent(new Intent(this, MentionsActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(MessagesActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_messages_caption))
				.setContent(new Intent(this, MessagesActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(UnsentActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_unsent_caption))
				.setContent(new Intent(this, UnsentActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(MoreActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_more_caption))
				.setContent(new Intent(this, MoreActivity.class)));

		if (!StringUtils.isEmpty(currentTab)) {
			tabHost.setCurrentTabByTag(currentTab);
		}

		TabWidget tw = getTabWidget();
		int size = tw.getChildCount();
		
		for (int i = 0; i < size; i++) {
			View v = tw.getChildAt(i);
			v.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_selector));
		}		

		created = true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.TabActivity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	/* (non-Javadoc)
	 * @see android.app.TabActivity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onPostCreate(icicle);
	}
}
