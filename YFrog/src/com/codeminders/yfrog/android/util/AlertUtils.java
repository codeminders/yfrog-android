/**
 * 
 */
package com.codeminders.yfrog.android.util;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * @author idemydenko
 *
 */
public final class AlertUtils {
	public static final int ALERT_TWITTER_ERROR = 0x0000ff;
	
	private AlertUtils() {
	}
	
	public static AlertDialog createTwitterErrorAlert(Activity activity, YFrogTwitterException e) {
		String message = activity.getResources().getString(R.string.twitter_error_msg) + e.getErrorCode();
		return new AlertDialog.Builder(activity)
		.setTitle(R.string.twitter_error_title)
		.setMessage(message)
		.setNeutralButton(R.string.twitter_error_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.create();
	}
}
