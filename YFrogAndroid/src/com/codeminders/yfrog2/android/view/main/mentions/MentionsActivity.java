/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.mentions;


import java.util.ArrayList;

import android.app.Dialog;
import android.view.KeyEvent;

import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.TwitterStatus;
import com.codeminders.yfrog2.android.util.AlertUtils;
import com.codeminders.yfrog2.android.view.main.AbstractTwitterStatusesListActivity;

/**
 * @author idemydenko
 *
 */
public class MentionsActivity extends AbstractTwitterStatusesListActivity {
	public static final String TAG = "mentions";

	@Override
	protected ArrayList<TwitterStatus> getStatuses(int page, int count)
			throws YFrogTwitterException {
		return twitterService.getMentions(page, count);
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
