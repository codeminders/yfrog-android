/**
 * 
 */
package com.codeminders.yfrog.android.view.main;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;

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
public class MainTabActivity extends TabActivity implements OnTabChangeListener {

	private String currentTab = null;
	private boolean created = false;
	private TabHost tabHost;
	
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
		
		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);
		
		tabHost.addTab(tabHost.newTabSpec(HomeActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_home_caption), getResources().getDrawable(R.drawable.home))
				.setContent(new Intent(this, HomeActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(MentionsActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_mentions_caption), getResources().getDrawable(R.drawable.mentions))
				.setContent(new Intent(this, MentionsActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(MessagesActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_messages_caption), getResources().getDrawable(R.drawable.messages))
				.setContent(new Intent(this, MessagesActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(UnsentActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_unsent_caption), getResources().getDrawable(R.drawable.unsent))
				.setContent(new Intent(this, UnsentActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(MoreActivity.TAG)
				.setIndicator(getResources().getString(R.string.tab_more_caption), getResources().getDrawable(R.drawable.more))
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
	
	@Override
	public void onTabChanged(String tabId) {
		tabHost.getCurrentTab();
	}
}
