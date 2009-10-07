/**
 * 
 */
package com.codeminders.yfrog.android.view.main.home;

import java.util.ArrayList;
import java.util.Collections;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.view.main.AbstractTwitterStatusesListActivity;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

/**
 * @author idemydenko
 *
 */

//TODO may be need StatusChangeListener
public class HomeActivity extends AbstractTwitterStatusesListActivity {
	protected ArrayList<TwitterStatus> getStatuses() throws YFrogTwitterException {
		return twitterService.getHomeStatuses();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_tab, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, WriteStatusActivity.class);
		startActivity(intent);
		return true;
	}
}
