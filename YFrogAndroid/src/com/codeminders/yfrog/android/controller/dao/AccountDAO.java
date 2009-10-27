/**
 * 
 */
package com.codeminders.yfrog.android.controller.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.*;

import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.util.StringUtils;

/**
 * @author idemydenko
 *
 */
public class AccountDAO extends AbstractDAO {
	private static final String TABLE_NAME = "account";
	private static final String ID = "account_id";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "email";
	private static final String OAUTH_TOLKEN = "oauth_token";
	private static final String OAUTH_TOLKEN_SECRET = "oauth_token_secret";
	private static final String AUTHORIZATION_METHOD = "auth_method";
	private static final String OAUTH_STATUS = "oauth_status";
	private static final String POST_LOCATION = "post_location";
	private static final String SCALE_IMAGES = "scale_images";

	public static final String ACCOUNT_DDL = 
		" CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
		ID + " INTEGER PRIMARY KEY, " +
		USERNAME + " TEXT, " +
		PASSWORD + " TEXT, " +
		EMAIL + " TEXT, " +
		OAUTH_TOLKEN + " TEXT," +
		OAUTH_TOLKEN_SECRET + " TEXT, " +
		OAUTH_STATUS + " INTEGER NOT NULL, " +
		AUTHORIZATION_METHOD + " INTEGER NOT NULL, " +
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
																+ " where " + OAUTH_STATUS + " = " + Account.OAUTH_STATUS_WAIT_VERIFICATION 
																+ " and " + AUTHORIZATION_METHOD + " = " + Account.METHOD_OAUTH;

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
			int idxPassword = cursor.getColumnIndex(PASSWORD);
			int idxEmail = cursor.getColumnIndex(EMAIL);
			int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
			int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
			int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
			int idxAuthMethod = cursor.getColumnIndex(AUTHORIZATION_METHOD);
			int idxPostLocStatus = cursor.getColumnIndex(POST_LOCATION);
			int idxScaleImages = cursor.getColumnIndex(SCALE_IMAGES);
			
			while(cursor.moveToNext()) {
				Account account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setUsername(cursor.getString(idxNickname));
				account.setPassword(cursor.getString(idxPassword));
				account.setEmail(cursor.getString(idxEmail));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setAuthMethod(cursor.getInt(idxAuthMethod));
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
				int idxPassword = cursor.getColumnIndex(PASSWORD);
				int idxEmail = cursor.getColumnIndex(EMAIL);
				int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
				int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
				int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
				int idxAuthMethod = cursor.getColumnIndex(AUTHORIZATION_METHOD);
				int idxPostLocStatus = cursor.getColumnIndex(POST_LOCATION);
				int idxScaleImages = cursor.getColumnIndex(SCALE_IMAGES);
				
				cursor.moveToNext();
				
				account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setUsername(cursor.getString(idxNickname));
				account.setPassword(cursor.getString(idxPassword));
				account.setEmail(cursor.getString(idxEmail));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setAuthMethod(cursor.getInt(idxAuthMethod));
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
			values.put(AUTHORIZATION_METHOD, Account.METHOD_COMMON);
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
				int idxPassword = cursor.getColumnIndex(PASSWORD);
				int idxEmail = cursor.getColumnIndex(EMAIL);
				int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
				int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
				int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
				int idxAuthMethod = cursor.getColumnIndex(AUTHORIZATION_METHOD);
				int idxPostLocStatus = cursor.getColumnIndex(POST_LOCATION);
				int idxScaleImages = cursor.getColumnIndex(SCALE_IMAGES);
				

				
				cursor.moveToNext();
				
				account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setUsername(cursor.getString(idxNickname));
				account.setPassword(cursor.getString(idxPassword));
				account.setEmail(cursor.getString(idxEmail));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setAuthMethod(cursor.getInt(idxAuthMethod));
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

	public long addAccount(Account account) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(USERNAME, account.getUsername());
			values.put(PASSWORD, account.getPassword());
			values.put(OAUTH_TOLKEN, account.getOauthToken());
			values.put(OAUTH_TOLKEN_SECRET, account.getOauthTokenSecret());
			values.put(AUTHORIZATION_METHOD, account.getAuthMethod());
			values.put(OAUTH_STATUS, account.getOauthStatus());
			values.put(EMAIL, account.getEmail());
			values.put(POST_LOCATION, account.getPostLocation());
			values.put(SCALE_IMAGES, account.getScaleImage());
			id = db.insert(TABLE_NAME, null, values);
		} finally {
			db.close();
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
			values.put(PASSWORD, account.getPassword());
			values.put(OAUTH_TOLKEN, account.getOauthToken());
			values.put(OAUTH_TOLKEN_SECRET, account.getOauthTokenSecret());
			values.put(AUTHORIZATION_METHOD, account.getAuthMethod());
			values.put(OAUTH_STATUS, account.getOauthStatus());
			values.put(EMAIL, account.getEmail());
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
