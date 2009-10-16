/**
 * 
 */
package com.codeminders.yfrog.android.view.main;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.codeminders.yfrog.android.R;
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
	private static final String TAB_HOME_TAG = "home";
	private static final String TAB_MENTIONS_TAG = "mentions";
	private static final String TAB_MESSAGES_TAG = "messages";
	private static final String TAB_UNSENT_TAG = "unsent";
	private static final String TAB_MORE_TAG = "more";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TabHost tabHost = getTabHost();
		
		tabHost.addTab(tabHost.newTabSpec(TAB_HOME_TAG)
				.setIndicator(getResources().getString(R.string.tab_home_caption))
				.setContent(new Intent(this, HomeActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(TAB_MENTIONS_TAG)
				.setIndicator(getResources().getString(R.string.tab_mentions_caption))
				.setContent(new Intent(this, MentionsActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(TAB_MESSAGES_TAG)
				.setIndicator(getResources().getString(R.string.tab_messages_caption))
				.setContent(new Intent(this, MessagesActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(TAB_UNSENT_TAG)
				.setIndicator(getResources().getString(R.string.tab_unsent_caption))
				.setContent(new Intent(this, UnsentActivity.class)));
		
		tabHost.addTab(tabHost.newTabSpec(TAB_MORE_TAG)
				.setIndicator(getResources().getString(R.string.tab_more_caption))
				.setContent(new Intent(this, MoreActivity.class)));
	}
}
