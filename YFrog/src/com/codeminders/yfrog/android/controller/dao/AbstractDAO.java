/**
 * 
 */
package com.codeminders.yfrog.android.controller.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author idemydenko
 *
 */
public abstract class AbstractDAO {
	public static final String DB_NAME = "yfrog_db";
}

class DatabaseHelper extends SQLiteOpenHelper {
	/**
	 * 
	 */
	public DatabaseHelper(Context context) {
		super(context, AbstractDAO.DB_NAME, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE account (account_id integer primary key, nickname text, password text, email text, oauth_key text);");		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}