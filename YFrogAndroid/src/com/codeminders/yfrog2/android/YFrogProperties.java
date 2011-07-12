/**
 * 
 */

package com.codeminders.yfrog2.android;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.util.StringUtils;

/**
 * @author idemydenko
 * 
 */
public final class YFrogProperties
{
    private static final String    PREFS_NAME      = "yfrog_prefs";

    private static final String    DEV_KEY         = "imageshack_dev_key";
    private static final String    CONSUMER_KEY    = "consumer_key";
    private static final String    CONSUMER_SECRET = "consumer_secret";

    private static final String    TAG             = "YFrogProperties";

    private static YFrogProperties instance        = null;

    private Context                context;
    private SharedPreferences      preferences;

    static void init(Context ctx)
    {
        if(instance == null)
        {
            instance = new YFrogProperties(ctx);
        }
    }

    public static final YFrogProperties getProperies()
    {
        if(instance == null)
        {
            throw new IllegalStateException("YFrog properties has not been initialized");
        }
        return instance;
    }

    private YFrogProperties(Context ctx)
    {
        context = ctx;
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        initAppProperties();
    }

    private void initAppProperties()
    {
        try
        {
            Properties properties = new Properties();
            properties.load(context.getResources().openRawResource(R.raw.app));

            initDevKey(properties);
            initOauthKey(properties);
        } catch(IOException e)
        {
            Log.e(TAG,"Error reading properties",e);
        }
    }

    private void initDevKey(Properties properties)
    {
        String devKey = properties.getProperty(DEV_KEY, "");

        if(StringUtils.isEmpty(devKey))
        {
            Log.e(TAG, "Missing imageshack dev key");
        }

        //System.out.println("developer key: " + devKey);
        putString(DEV_KEY, devKey);
    }

    private void initOauthKey(Properties properties)
    {
        String val = properties.getProperty(CONSUMER_KEY, "");

        if(StringUtils.isEmpty(val))
            Log.e(TAG, "Missing OAuth consumer key");
        
        //Log.d(TAG,"Consumer key="+val);
        putString(CONSUMER_KEY, val);

        val = properties.getProperty(CONSUMER_SECRET, "");
        if(StringUtils.isEmpty(val))
            Log.e(TAG, "Missing OAuth consumer secret");

        //Log.d(TAG,"Consumer secret="+val);
        putString(CONSUMER_SECRET, val);

    }

    public void delete(String key)
    {
        Editor editor = preferences.edit();

        editor.remove(key);
        editor.commit();
    }

    public void putString(String key, String value)
    {
        Editor editor = preferences.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key)
    {
        return preferences.getString(key, "");
    }

    public void putLong(String key, long value)
    {
        Editor editor = preferences.edit();

        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key)
    {
        return preferences.getLong(key, 0);
    }

    public String getDeveloperKey()
    {
        return getString(DEV_KEY);
    }

    public String getConsumerKey()
    {
        return getString(CONSUMER_KEY);
    }

    public String getConsumerSecret()
    {
        return getString(CONSUMER_SECRET);
    }
}
