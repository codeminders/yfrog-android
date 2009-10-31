/**
 * 
 */
package com.codeminders.yfrog.android.view.main.home;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.view.main.AbstractTwitterStatusesListActivity;
import com.codeminders.yfrog.android.view.message.*;

/**
 * @author idemydenko
 *
 */

//TODO may be need StatusChangeListener
public class HomeActivity extends AbstractTwitterStatusesListActivity {
	public static final String TAG = "home";
	
	protected ArrayList<TwitterStatus> getStatuses(int page, int count) throws YFrogTwitterException {
		return twitterService.getHomeStatuses(page, count);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_tab, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivityForResult(intent, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == WritableActivity.RESULT_SEND) {
			Bundle bundle = data.getExtras();
			
			if (bundle != null) {
				TwitterStatus sent = (TwitterStatus) bundle.getSerializable(WritableActivity.KET_SENT);
				statuses.add(0, sent);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
