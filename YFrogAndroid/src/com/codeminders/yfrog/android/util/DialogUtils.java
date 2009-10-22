/**
 * 
 */
package com.codeminders.yfrog.android.util;

import android.app.*;
import android.content.*;

import com.codeminders.yfrog.android.R;

/**
 * @author idemydenko
 *
 */
public final class DialogUtils {
	public static final int ALERT_TWITTER_ERROR = 0x0000ff;
	public static final int ALERT_IO_ERROR = 0x000fff;
	
	
	private DialogUtils() {
	}
	
	public static AlertDialog createTwitterErrorAlert(Context context) {
		String message = context.getResources().getString(R.string.twitter_error_msg);
		return new AlertDialog.Builder(context)
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

	public static AlertDialog createIOErrorAlert(Context context) {
		String message = context.getResources().getString(R.string.io_error_msg);
		return new AlertDialog.Builder(context)
		.setTitle(R.string.io_error_title)
		.setMessage(message)
		.setNeutralButton(R.string.io_error_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.create();
	}

	public static ProgressDialog showProgressAlert(Context context) {
		return ProgressDialog.show(context, null, context.getString(R.string.loading));	
	}

}
