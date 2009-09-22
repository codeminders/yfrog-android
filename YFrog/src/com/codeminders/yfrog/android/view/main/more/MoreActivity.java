/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import com.codeminders.yfrog.android.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author idemydenko
 *
 */
public class MoreActivity extends ListActivity {
	private static final int ITEM_FOLLOWERS = 0;
	private static final int ITEM_FOLLOWING = 1;
	
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
		case ITEM_FOLLOWERS:
			intent = new Intent(this, FollowersActivity.class);
			startActivity(intent);
			break;

		case ITEM_FOLLOWING:
			intent = new Intent(this, FollowingActivity.class);
			startActivity(intent);			
			break;
		}
	}
}
