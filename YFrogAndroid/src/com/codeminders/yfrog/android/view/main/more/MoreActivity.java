/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.codeminders.yfrog.android.R;

/**
 * @author idemydenko
 *
 */
public class MoreActivity extends ListActivity {
	private static final int ITEM_MY_TWEETS = 0;
	private static final int ITEM_FOLLOWERS = 1;
	private static final int ITEM_FOLLOWING = 2;
	private static final int ITEM_SEARCH =3;
	private static final int ITEM_SETTINGS =4;
	private static final int ITEM_ABOUT =5;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tab_more_items)));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = null;
		switch (position) {
		case ITEM_MY_TWEETS:
			intent = new Intent(this, MyTweetsActivity.class);
			startActivity(intent);
			break;

		case ITEM_FOLLOWERS:
			intent = new Intent(this, FollowersActivity.class);
			startActivity(intent);
			break;

		case ITEM_FOLLOWING:
			intent = new Intent(this, FollowingActivity.class);
			startActivity(intent);			
			break;
		
		case ITEM_SEARCH:
			intent = new Intent(this, SearchActivity.class);
			startActivity(intent);			
			break;
		case ITEM_SETTINGS:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case ITEM_ABOUT:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		}
	}
}
