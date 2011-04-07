/**
 * 
 */
package com.codeminders.yfrog.android.controller.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.codeminders.yfrog.android.YFrogDatabaseException;
import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.util.StringUtils;

/**
 * @author idemydenko
 *
 */
public class AccountDAO extends AbstractDAO {
	private static final String TABLE_NAME = "account";
	private static final String ID = "account_id";
	private static final String USERNAME = "username";
	private static final String OAUTH_TOLKEN = "oauth_token";
	private static final String OAUTH_TOLKEN_SECRET = "oauth_token_secret";
	private static final String OAUTH_STATUS = "oauth_status";
	private static final String POST_LOCATION = "post_location";
	private static final String SCALE_IMAGES = "scale_images";
	
	public static final String RENAME_TO_TEMP = "ALTER TABLE " + TABLE_NAME + " RENAME TO temp_" + TABLE_NAME + ";";
	public static final String DROP_TEMP = "DROP TABLE temp_" + TABLE_NAME + ";";
	public static final String COPY_VALUES = "INSERT INTO " + TABLE_NAME + " SELECT "+ 
		ID + ", " + USERNAME + ", " + OAUTH_TOLKEN + ", " + OAUTH_TOLKEN_SECRET + ", " + 
		OAUTH_STATUS + ", " + POST_LOCATION + ", " + SCALE_IMAGES + 
		" FROM temp_" + TABLE_NAME + " WHERE auth_method==1;";

	public static final String ACCOUNT_DDL = 
		" CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
		ID + " INTEGER PRIMARY KEY, " +
		USERNAME + " TEXT, " +
		OAUTH_TOLKEN + " TEXT," +
		OAUTH_TOLKEN_SECRET + " TEXT, " +
		OAUTH_STATUS + " INTEGER NOT NULL, " +
		POST_LOCATION + " INTEGER NOT NULL, " +
		SCALE_IMAGES + " INTEGER NOT NULL " +
		"); ";
	
	private static final String COUNT = "count";
	
	private static final String ALL_ACCOUNTS_QUERY = "select * from " + TABLE_NAME;
	private static final String GET_BY_ID_QUERY = "select * from " + TABLE_NAME + " where " + ID + " = ?";
	private static final String IS_UNIQUE_QUERY = "select count(*) as " + COUNT + " from " + TABLE_NAME + " where " 
													+ " lower(" + USERNAME + ") = lower(?)" 
													+ " and " + ID + " != ?";

	private static final String ID_EQUAL_WHERE = ID + " = ?";
	private static final String OAUTH_STATUS_EQUAL_WHERE = OAUTH_STATUS + " = ?";
	private static final String USERNAME_IS_NULL_WHERE = USERNAME + " is null";
	private static final String GET_WATING_FOR_OAUTH_VERIFICATION = "select * from " + TABLE_NAME 
																+ " where " + OAUTH_STATUS + " = " 
																+ Account.OAUTH_STATUS_WAIT_VERIFICATION; 
															
	AccountDAO() {
	}
	
	public ArrayList<Account> getAllAccounts() {
		SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
		Cursor cursor = null;
		ArrayList<Account> result = new ArrayList<Account>();
		try {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(TABLE_NAME);
			cursor = db.rawQuery(ALL_ACCOUNTS_QUERY, null); 
			
			int idxId = cursor.getColumnIndex(ID);
			int idxNickname = cursor.getColumnIndex(USERNAME);
			int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
			int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
			int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
			int idxPostLocStatus = cursor.getColumnIndex(POST_LOCATION);
			int idxScaleImages = cursor.getColumnIndex(SCALE_IMAGES);
			
			while(cursor.moveToNext()) {
				Account account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setUsername(cursor.getString(idxNickname));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setPostLocationStatus(cursor.getInt(idxPostLocStatus));
				account.setScaleImage(cursor.getInt(idxScaleImages));
				
				result.add(account);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		
		return result;
	}
	
	public Account getAccount(long id) {
		SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
		Cursor cursor = null;
		Account account = null;
		
		try {
			cursor = db.rawQuery(GET_BY_ID_QUERY, new String[] { id + "" });
			
			if (cursor.getCount() == 1) {
				int idxId = cursor.getColumnIndex(ID);
				int idxNickname = cursor.getColumnIndex(USERNAME);
				int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
				int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
				int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
				int idxPostLocStatus = cursor.getColumnIndex(POST_LOCATION);
				int idxScaleImages = cursor.getColumnIndex(SCALE_IMAGES);
				
				cursor.moveToNext();
				
				account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setUsername(cursor.getString(idxNickname));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setPostLocationStatus(cursor.getInt(idxPostLocStatus));
				account.setScaleImage(cursor.getInt(idxScaleImages));

			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return account;
	}

	public void resetWatingForOAuthVerificationAccounts() {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		try {
			ContentValues values = new ContentValues();
			values.put(OAUTH_TOLKEN, StringUtils.EMPTY_STRING);
			values.put(OAUTH_TOLKEN_SECRET, StringUtils.EMPTY_STRING);
			values.put(OAUTH_STATUS, Account.OAUTH_STATUS_NOT_AUTHORIZED);
			db.update(TABLE_NAME, values, OAUTH_STATUS_EQUAL_WHERE, new String[] {Account.OAUTH_STATUS_WAIT_VERIFICATION + ""});
		} finally {
			db.close();
		}

	}
	
	public Account getWatingForOAuthVerificationAccount() {
		SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
		Cursor cursor = null;
		Account account = null;
		
		try {
			cursor = db.rawQuery(GET_WATING_FOR_OAUTH_VERIFICATION, new String[0]);
			
			if (cursor.getCount() == 1) {
				int idxId = cursor.getColumnIndex(ID);
				int idxNickname = cursor.getColumnIndex(USERNAME);
				int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
				int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
				int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
				int idxPostLocStatus = cursor.getColumnIndex(POST_LOCATION);
				int idxScaleImages = cursor.getColumnIndex(SCALE_IMAGES);
				
				cursor.moveToNext();
				
				account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setUsername(cursor.getString(idxNickname));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setPostLocationStatus(cursor.getInt(idxPostLocStatus));
				account.setScaleImage(cursor.getInt(idxScaleImages));

			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return account;
	}

	public long addAccount(Account account) throws YFrogDatabaseException {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(USERNAME, account.getUsername());
			values.put(OAUTH_TOLKEN, account.getOauthToken());
			values.put(OAUTH_TOLKEN_SECRET, account.getOauthTokenSecret());
			values.put(OAUTH_STATUS, account.getOauthStatus());
			values.put(POST_LOCATION, account.getPostLocation());
			values.put(SCALE_IMAGES, account.getScaleImage());
			id = db.insert(TABLE_NAME, null, values);
		} finally {
			db.close();
		}
		
		if (id == -1) {
			throw new YFrogDatabaseException(AlertUtils.DB_ACCOUNT_INSERT_ERROR);
		}
		
		return id;
	}
	
	public void deleteAccount(long id) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		try {
			db.delete(TABLE_NAME, ID_EQUAL_WHERE, new String[] {id + ""});
		} finally {
			db.close();
		}
	}

	public void deleteEmptyAccounts() {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		try {
			db.delete(TABLE_NAME, USERNAME_IS_NULL_WHERE, new String[] {});
		} finally {
			db.close();
		}
	}

	public void updateAccount(Account account) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		try {
			ContentValues values = new ContentValues();
			values.put(USERNAME, account.getUsername());
			values.put(OAUTH_TOLKEN, account.getOauthToken());
			values.put(OAUTH_TOLKEN_SECRET, account.getOauthTokenSecret());
			values.put(OAUTH_STATUS, account.getOauthStatus());
			values.put(POST_LOCATION, account.getPostLocation());
			values.put(SCALE_IMAGES, account.getScaleImage());
			db.update(TABLE_NAME, values, ID_EQUAL_WHERE, new String[] {account.getId() + ""});
		} finally {
			db.close();
		}
	}

	// TODO Bad solution for design, but better for performance 
	public boolean isAccountUnique(Account account) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		Cursor cursor = null;
		int count = 0;
		
		try {
			cursor = db.rawQuery(IS_UNIQUE_QUERY, new String[] { account.getUsername(), account.getId() + ""});
			int idx = cursor.getColumnIndex(COUNT);
			cursor.moveToNext();
			count = cursor.getInt(idx);
		} finally {
			db.close();
			if (cursor !=null) {
				cursor.close();
			}
		}
		return count == 0;
	}
}
