/**
 * 
 */
package com.codeminders.yfrog.android;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author idemydenko
 *
 */
public final class YFrogProperties {
	private static final String PREFS_NAME = "yfrog_prefs";
	
	private static final String DEV_KEY = "imageshack_dev_key";
	private static final String DEV_KEY_DEF_VAL = "@dev_key@";
	
	private static final String CONSUMER_KEY = "consumer_key";
	private static final String CONSUMER_SECRET = "consumer_secret";
	
	private static YFrogProperties instance = null;
	
	private Context context;
	private SharedPreferences preferences;
	
	static void init(Context ctx) {
		if (instance == null) {
			instance = new YFrogProperties(ctx); 
		}
	}
	
	public static final YFrogProperties getProperies() {
		if (instance == null) {
			throw new IllegalStateException("YFrog properties doesn't init");
		}
		return instance;
	}
	
	private YFrogProperties(Context ctx) {
		context = ctx;
		preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		initAppProperties();
	}
	
	private void initAppProperties() {
		try {
			Properties properties = new Properties();
			properties.load(context.getResources().openRawResource(R.raw.app));
			
			putString(DEV_KEY, properties.getProperty(DEV_KEY, ""));
			putString(CONSUMER_KEY, properties.getProperty(CONSUMER_KEY, ""));
			putString(CONSUMER_SECRET, properties.getProperty(CONSUMER_SECRET, ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(String key) {
		Editor editor = preferences.edit();
		
		editor.remove(key);
		editor.commit();
	}

	public void putString(String key, String value) {
		Editor editor = preferences.edit();
		
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		return preferences.getString(key, "");
	}

	public void putLong(String key, long value) {
		Editor editor = preferences.edit();
		
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key) {
		return preferences.getLong(key, 0);
	}

	public String getDeveloperKey() {
		String key = getString(DEV_KEY);
		
		if (DEV_KEY_DEF_VAL.equals(key)) {
			return "";
		}
		return key;
	}
	
	public String getConsumerKey() {
		return getString(CONSUMER_KEY);
	}
	
	public String getConsumerSecret() {
		return getString(CONSUMER_SECRET);
	}
}
