/**
 * 
 */
package com.codeminders.yfrog.android.controller.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.codeminders.yfrog.android.model.Account;

/**
 * @author idemydenko
 *
 */
public class AccountDAO extends AbstractDAO {
	private static final String TABLE_NAME = "account";
	private static final String ID = "account_id";
	private static final String NAME = "account_name";
	private static final String NICKNAME = "nickname";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "email";
	private static final String OAUTH_TOLKEN = "oauth_token";
	private static final String OAUTH_TOLKEN_SECRET = "oauth_token_secret";
	private static final String AUTHORIZATION_METHOD = "auth_method";
	private static final String OAUTH_STATUS = "oauth_status";

	public static final String ACCOUNT_DDL = 
		" CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
		ID + " INTEGER PRIMARY KEY, " +
		NAME + " TEXT, " +
		NICKNAME + " TEXT, " +
		PASSWORD + " TEXT, " +
		EMAIL + " TEXT, " +
		OAUTH_TOLKEN + " TEXT," +
		OAUTH_TOLKEN_SECRET + " TEXT, " +
		OAUTH_STATUS + " INTEGER NOT NULL, " +
		AUTHORIZATION_METHOD + " INTEGER NOT NULL); ";
	
	private static final String COUNT = "count";
	
	private static final String ALL_ACCOUNTS_QUERY = "select * from " + TABLE_NAME;
	private static final String GET_BY_ID_QUERY = "select * from " + TABLE_NAME + " where " + ID + " = ?";
	private static final String IS_UNIQUE_QUERY = "select count(*) as " + COUNT + " from " + TABLE_NAME + " where " 
													+ " " + NAME + " = ?" 
													+ " and " + ID + " != ?";

	private static final String ID_EQUAL_WHERE = ID + " = ?";
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
			int idxName = cursor.getColumnIndex(NAME);
			int idxNickname = cursor.getColumnIndex(NICKNAME);
			int idxPassword = cursor.getColumnIndex(PASSWORD);
			int idxEmail = cursor.getColumnIndex(EMAIL);
			int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
			int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
			int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
			int idxAuthMethod = cursor.getColumnIndex(AUTHORIZATION_METHOD);
			
			while(cursor.moveToNext()) {
				Account account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setName(cursor.getString(idxName));
				account.setNickname(cursor.getString(idxNickname));
				account.setPassword(cursor.getString(idxPassword));
				account.setEmail(cursor.getString(idxEmail));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setAuthMethod(cursor.getInt(idxAuthMethod));
				
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
				int idxName = cursor.getColumnIndex(NAME);
				int idxNickname = cursor.getColumnIndex(NICKNAME);
				int idxPassword = cursor.getColumnIndex(PASSWORD);
				int idxEmail = cursor.getColumnIndex(EMAIL);
				int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
				int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
				int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
				int idxAuthMethod = cursor.getColumnIndex(AUTHORIZATION_METHOD);
				
				cursor.moveToNext();
				
				account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setName(cursor.getString(idxName));
				account.setNickname(cursor.getString(idxNickname));
				account.setPassword(cursor.getString(idxPassword));
				account.setEmail(cursor.getString(idxEmail));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setAuthMethod(cursor.getInt(idxAuthMethod));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return account;
	}

	public Account getWatingForOAuthVerificationAccount() {
		SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
		Cursor cursor = null;
		Account account = null;
		
		try {
			cursor = db.rawQuery(GET_WATING_FOR_OAUTH_VERIFICATION, new String[0]);
			
			if (cursor.getCount() == 1) {
				int idxId = cursor.getColumnIndex(ID);
				int idxName = cursor.getColumnIndex(NAME);
				int idxNickname = cursor.getColumnIndex(NICKNAME);
				int idxPassword = cursor.getColumnIndex(PASSWORD);
				int idxEmail = cursor.getColumnIndex(EMAIL);
				int idxOauthTolken = cursor.getColumnIndex(OAUTH_TOLKEN);
				int idxOauthSecretTolken = cursor.getColumnIndex(OAUTH_TOLKEN_SECRET);
				int idxOauthStatus = cursor.getColumnIndex(OAUTH_STATUS);
				int idxAuthMethod = cursor.getColumnIndex(AUTHORIZATION_METHOD);
				
				cursor.moveToNext();
				
				account = new Account();
				account.setId(cursor.getLong(idxId));
				account.setName(cursor.getString(idxName));
				account.setNickname(cursor.getString(idxNickname));
				account.setPassword(cursor.getString(idxPassword));
				account.setEmail(cursor.getString(idxEmail));
				account.setOauthToken(cursor.getString(idxOauthTolken));
				account.setOauthTokenSecret(cursor.getString(idxOauthSecretTolken));
				account.setOauthStatus(cursor.getInt(idxOauthStatus));
				account.setAuthMethod(cursor.getInt(idxAuthMethod));
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
			values.put(NAME, account.getName());
			values.put(NICKNAME, account.getNickname());
			values.put(PASSWORD, account.getPassword());
			values.put(OAUTH_TOLKEN, account.getOauthToken());
			values.put(OAUTH_TOLKEN_SECRET, account.getOauthTokenSecret());
			values.put(AUTHORIZATION_METHOD, account.getAuthMethod());
			values.put(OAUTH_STATUS, account.getOauthStatus());
			values.put(EMAIL, account.getEmail());
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
	
	public void updateAccount(Account account) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		try {
			ContentValues values = new ContentValues();
			values.put(NAME, account.getName());
			values.put(NICKNAME, account.getNickname());
			values.put(PASSWORD, account.getPassword());
			values.put(OAUTH_TOLKEN, account.getOauthToken());
			values.put(OAUTH_TOLKEN_SECRET, account.getOauthTokenSecret());
			values.put(AUTHORIZATION_METHOD, account.getAuthMethod());
			values.put(OAUTH_STATUS, account.getOauthStatus());
			values.put(EMAIL, account.getEmail());
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
			cursor = db.rawQuery(IS_UNIQUE_QUERY, new String[] { account.getName(), account.getId() + ""});
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