/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.codeminders.yfrog.android.model.Account;

/**
 * @author idemydenko
 *
 */
public final class AccountService {
	private static final String TABLE_NAME = "account";
	
	private Context context;
	private DatabaseHelper dbHelper;
	
	AccountService(Context c) {
		context = c;
		dbHelper = new DatabaseHelper(c);
	}
	
	public ArrayList<Account> getAllAccounts() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor;
		try {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(TABLE_NAME);
			cursor = qb.query(db, null, null, null, null, null, null);
		} finally {
			db.close();
		}
		
		int idxId = cursor.getColumnIndex("account_id");
		int idxNickname = cursor.getColumnIndex("account_id");
		int idxPassword = cursor.getColumnIndex("account_id");
		int idxOauthKey = cursor.getColumnIndex("account_id");
		int idxEmail = cursor.getColumnIndex("account_id");
		
		ArrayList<Account> result = new ArrayList<Account>();
		do {
			Account account = new Account();
			account.setId(cursor.getInt(idxId));
			account.setNickname(cursor.getString(idxNickname));
			account.setPassword(cursor.getString(idxPassword));
			account.setOauthKey(cursor.getString(idxOauthKey));
			account.setEmail(cursor.getString(idxEmail));
			
			result.add(account);
		} while(cursor.moveToNext());
		
		return result;
	}
	
	public Account getAccount(String username) {
		return null;
	}
	
	public void addAccount(Account account) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("nickname", account.getNickname());
			values.put("password", account.getPassword());
			values.put("oauth_key", account.getOauthKey());
			values.put("email", account.getEmail());
			db.insert(TABLE_NAME, null, values);
		} finally {
			db.close();
		}
	}
}

class DatabaseHelper extends SQLiteOpenHelper {
	/**
	 * 
	 */
	public DatabaseHelper(Context context) {
		super(context, "yfrog", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE account (account_id integer primary key, nickname text, password text, email text, oauth_key text);");		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}