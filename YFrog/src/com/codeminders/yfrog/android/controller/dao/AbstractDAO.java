/**
 * 
 */
package com.codeminders.yfrog.android.controller.dao;

import com.codeminders.yfrog.android.controller.dao.db.DatabaseHelper;

/**
 * @author idemydenko
 *
 */
public abstract class AbstractDAO {
	public static final String DDL = AccountDAO.ACCOUNT_DDL;
	public static final String DB_NAME = "yfrog_db";
	private static DatabaseHelper dbHelper;
	
	protected DatabaseHelper getDatabaseHelper() {
		if (dbHelper == null) {
			dbHelper = DatabaseHelper.getInstance();
		}
		return dbHelper;
	}
}

