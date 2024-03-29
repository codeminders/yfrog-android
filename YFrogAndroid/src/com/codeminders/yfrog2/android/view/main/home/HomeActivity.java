/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.home;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.TwitterStatus;
import com.codeminders.yfrog2.android.util.AlertUtils;
import com.codeminders.yfrog2.android.view.main.AbstractTwitterStatusesListActivity;
import com.codeminders.yfrog2.android.view.message.WritableActivity;
import com.codeminders.yfrog2.android.view.message.WriteStatusActivity;

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(AlertUtils.LOGOUT);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == AlertUtils.LOGOUT) {
			return AlertUtils.createLogoutAlert(this);
		}
		return super.onCreateDialog(id);
	}
}
