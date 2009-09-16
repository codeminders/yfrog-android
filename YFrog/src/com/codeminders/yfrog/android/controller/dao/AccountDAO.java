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
	public static final String ACCOUNT_DDL = 
		"CREATE TABLE IF NOT EXISTS account (account_id INTEGER PRIMARY KEY, nickname TEXT, password TEXT, email TEXT, oauth_key TEXT);";
	
	private static final String TABLE_NAME = "account";
	private static final String ID = "account_id";
	private static final String NICKNAME = "nickname";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "email";
	private static final String OAUTH_KEY = "oauth_key";
	
	private static final String ALL_ACCOUNTS_QUERY = "select * from " + TABLE_NAME;
	private static final String GET_BY_NICKNAME = "select * from " + TABLE_NAME + " where " + ID + " = ?";
	
	public ArrayList<Account> getAllAccounts() {
		SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
		Cursor cursor = null;
		ArrayList<Account> result = new ArrayList<Account>();
		try {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(TABLE_NAME);
			cursor = db.rawQuery(ALL_ACCOUNTS_QUERY, null); 
			
			int idxId = cursor.getColumnIndex(ID);
			int idxNickname = cursor.getColumnIndex(NICKNAME);
			int idxPassword = cursor.getColumnIndex(PASSWORD);
			int idxEmail = cursor.getColumnIndex(EMAIL);
			int idxOauthKey = cursor.getColumnIndex(OAUTH_KEY);
			
			
			
			while(cursor.moveToNext()) {
				Account account = new Account();
				account.setId(cursor.getInt(idxId));
				account.setNickname(cursor.getString(idxNickname));
				account.setPassword(cursor.getString(idxPassword));
				account.setOauthKey(cursor.getString(idxOauthKey));
				account.setEmail(cursor.getString(idxEmail));
				
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
	
	public Account getAccount(int id) {
		SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
		Cursor cursor = null;
		Account account = null;
		
		try {
			cursor = db.rawQuery(GET_BY_NICKNAME, new String[] { id + "" });
			
			if (cursor.getCount() == 1) {
				int idxId = cursor.getColumnIndex(ID);
				int idxNickname = cursor.getColumnIndex(NICKNAME);
				int idxPassword = cursor.getColumnIndex(PASSWORD);
				int idxEmail = cursor.getColumnIndex(EMAIL);
				int idxOauthKey = cursor.getColumnIndex(OAUTH_KEY);
				
				cursor.moveToNext();
				
				account = new Account();
				account.setId(cursor.getInt(idxId));
				account.setNickname(cursor.getString(idxNickname));
				account.setPassword(cursor.getString(idxPassword));
				account.setOauthKey(cursor.getString(idxOauthKey));
				account.setEmail(cursor.getString(idxEmail));
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
			values.put(NICKNAME, account.getNickname());
			values.put(PASSWORD, account.getPassword());
			values.put(OAUTH_KEY, account.getOauthKey());
			values.put(EMAIL, account.getEmail());
			id = db.insert(TABLE_NAME, null, values);
		} finally {
			db.close();
		}
		
		return id;
	}
}
