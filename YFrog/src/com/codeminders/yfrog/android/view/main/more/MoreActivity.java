/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import com.codeminders.yfrog.android.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * @author idemydenko
 *
 */
public class MoreActivity extends ListActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tab_more_items)));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	}
	
	
}
