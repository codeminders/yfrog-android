/**
 * 
 */
package com.codeminders.yfrog2.android;

import android.app.Application;

import com.codeminders.yfrog2.android.controller.dao.db.DatabaseHelper;
import com.codeminders.yfrog2.android.controller.service.ServiceFactory;

/**
 * @author idemydenko
 *
 */
public final class YFrog extends Application {
	public static final String TAG = "YFrog";
	
	@Override
	public void onCreate() {
		super.onCreate();
		DatabaseHelper.init(this);
		YFrogProperties.init(this);
		// TODO need other initialization
		ServiceFactory.getGeoLocationService(this);
	}
}
