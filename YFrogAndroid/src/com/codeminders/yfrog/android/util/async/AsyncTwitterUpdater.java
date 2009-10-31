/**
 * 
 */
package com.codeminders.yfrog.android.util.async;

import android.app.*;
import android.os.Handler;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.util.ProgressDialogUtils;

/**
 * @author idemydenko
 *
 */
public abstract class AsyncTwitterUpdater {
	Activity activity;
	Handler handler;
	ProgressDialog dialog;
	Thread thread;
	
	public AsyncTwitterUpdater(Activity a) {
		activity = a;
		handler = new Handler();
		dialog = ProgressDialogUtils.showProgressAlert(activity);
		thread = new Thread(new Updater());
	}
	
	protected abstract void doUpdate() throws YFrogTwitterException;
	
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
			} catch (YFrogTwitterException e) {
				error = true;
				final int errorCode = e.getErrorCode();
				
				handler.post(new Runnable() {
					public void run() {
						dismissDialog();
						activity.showDialog(errorCode);
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
}
