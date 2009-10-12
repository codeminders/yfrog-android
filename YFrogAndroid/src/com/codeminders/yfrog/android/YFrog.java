/**
 * 
 */
package com.codeminders.yfrog.android;

import android.app.Application;

import com.codeminders.yfrog.android.controller.dao.db.DatabaseHelper;

/**
 * @author idemydenko
 *
 */
public final class YFrog extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		DatabaseHelper.init(this);
	}
}
