/**
 * 
 */
package com.codeminders.yfrog2.android.util.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;

import com.codeminders.yfrog2.android.util.AlertUtils;
import com.codeminders.yfrog2.android.util.ProgressDialogUtils;

/**
 * @author idemydenko
 *
 */
public abstract class AsyncIOUpdater {
	Activity activity;
	Handler handler;
	ProgressDialog dialog;
	
	public AsyncIOUpdater(Activity a) {
		activity = a;
		handler = new Handler();
		dialog = ProgressDialogUtils.showProgressAlert(activity);
	}
	
	protected abstract void doUpdate() throws Exception;
	
	protected void doAfterUpdate() {
		
	}
	
	protected void doAfterError() {
		
	}
	
	public final void update() {
		new Thread(new Updater()).start();
	}
	
	private class Updater implements Runnable {
		public void run() {
			boolean error = false;
			
			try {
				doUpdate();
			} catch (Exception e) {
				error = true;
				
				handler.post(new Runnable() {
					public void run() {
						dismissDialog();
						activity.showDialog(AlertUtils.IO_ERROR);
						doAfterError();
					}
				});
			}
			
			if (!error) {
				handler.post(new Runnable() {
					public void run() {
						doAfterUpdate();
						dismissDialog();
					}
				});
			}
		}
	}

	protected void dismissDialog() {
		try {
			dialog.dismiss();
		} catch (RuntimeException e) {
			// If window lost
		}
	}
}
