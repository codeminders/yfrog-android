/**
 * 
 */
package com.codeminders.yfrog.android.util.async;

import android.app.*;
import android.os.Handler;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.util.DialogUtils;

/**
 * @author idemydenko
 *
 */
public abstract class AsyncTwitterUpdater {
	private Activity activity;
	private Handler handler;
	private ProgressDialog dialog;
	
	public AsyncTwitterUpdater(Activity a) {
		activity = a;
		handler = new Handler();
		dialog = DialogUtils.showProgressAlert(activity);
	}
	
	protected abstract void doUpdate() throws YFrogTwitterException;
	
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
			} catch (YFrogTwitterException e) {
				error = true;
				
				handler.post(new Runnable() {
					public void run() {
						activity.showDialog(DialogUtils.ALERT_TWITTER_ERROR);
						doAfterError();
					}
				});
			}
			
			if (!error) {
				handler.post(new Runnable() {
					public void run() {
						doAfterUpdate();
						dialog.dismiss();
					}
				});
			}
		}
	}
}
