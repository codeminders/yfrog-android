/**
 * 
 */
package com.codeminders.yfrog2.android.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.codeminders.yfrog2.android.R;

/**
 * @author idemydenko
 *
 */
public final class ProgressDialogUtils {
	private ProgressDialogUtils() {
	}
	
	public static ProgressDialog showProgressAlert(Context context) {
		return ProgressDialog.show(context, null, context.getString(R.string.loading));	
	}

}
