/**
 * 
 */
package com.codeminders.yfrog.android.controller.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.codeminders.yfrog.android.controller.dao.AbstractDAO;

/**
 * @author idemydenko
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper instatnce = null;
	
	public static final void init(Context context) {
		if (instatnce == null) {
			instatnce = new DatabaseHelper(context);
		}
	}
	
	public static final DatabaseHelper getInstance() {
		return instatnce;
	}
	
	private DatabaseHelper(Context context) {
		super(context, AbstractDAO.DB_NAME, null, 2);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		AbstractDAO.onCreateDatabase(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		AbstractDAO.onUpgradeDatabase(db, oldVersion, newVersion);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
}
