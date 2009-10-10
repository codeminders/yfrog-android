/**
 * 
 */
package com.codeminders.yfrog.android;

import com.codeminders.yfrog.android.controller.dao.db.DatabaseHelper;

import android.app.Application;
import android.content.Context;

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
