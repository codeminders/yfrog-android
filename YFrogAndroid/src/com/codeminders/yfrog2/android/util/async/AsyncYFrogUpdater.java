/**
 * 
 */
package com.codeminders.yfrog2.android.util.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;

import com.codeminders.yfrog2.android.YFrogException;
import com.codeminders.yfrog2.android.util.ProgressDialogUtils;

/**
 * @author idemydenko
 *
 */
public abstract class AsyncYFrogUpdater {
	Activity activity;
	Handler handler;
	ProgressDialog dialog;
	Thread thread;
	
	public AsyncYFrogUpdater(Activity a) {
		activity = a;
		handler = new Handler();
		dialog = ProgressDialogUtils.showProgressAlert(activity);
		thread = new Thread(new Updater());
	}
	
	protected abstract void doUpdate() throws YFrogException;
	
	protected void doAfterUpdate() {
		
	}
	
	protected void doAfterError() {
		
	}
	
	public final void update() {
		thread.start();
	}
	
	public Thread getThread() {
		return thread;
	}
	
	protected void dismissDialog() {
		try {
			dialog.dismiss();
		} catch (RuntimeException e) {
			// If window lost
		}
	}
	
	private class Updater implements Runnable {
		public void run() {
			boolean error = false;
			
			try {
				doUpdate();
			} catch (YFrogException e) {
				error = true;
				final int errorCode = e.getErrorCode();
				
				handler.post(new Runnable() {
					public void run() {
						dismissDialog();
                        try{
						    activity.showDialog(errorCode);
                            doAfterError();
                        } catch (Exception ignored) {
                        }

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
}
