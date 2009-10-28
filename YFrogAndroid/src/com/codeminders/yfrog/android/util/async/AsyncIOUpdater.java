/**
 * 
 */
package com.codeminders.yfrog.android.util.async;

import android.app.*;
import android.os.Handler;

import com.codeminders.yfrog.android.util.*;

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
						dialog.dismiss();
						activity.showDialog(AlertUtils.IO_ERROR);
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
