/**
 * 
 */
package com.codeminders.yfrog.android.controller.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.*;

import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 *
 */
public class UnsentMessageDAO extends AbstractDAO {
	private static final String TABLE_NAME = "unsent_message";
	private static final String ID = "unsent_message_id";
	private static final String ACCOUNT_ID = "account_id";
	private static final String TEXT = "message_text";
	private static final String TYPE = "type";
	private static final String TO = "receiver";
	private static final String ATTACHMENT = "attach_uri";
	
	public static final String UNSENT_MESSAGE_DDL = 
		" CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
		ID + " INTEGER PRIMARY KEY, " +
		ACCOUNT_ID + " INTEGER NOT NULL, " +
		TEXT + " TEXT NOT NULL, " +
		TYPE + " INTEGER NOT NULL, " +
		TO + " TEXT, " +
		ATTACHMENT + " TEXT " +
		"); ";
	
	
	private static final String COUNT = "count";
	
	private static final String GET_FOR_ACCOUNT_QUERY = "select * from " + TABLE_NAME + " where " + ACCOUNT_ID + " = ?";
	private static final String GET_COUNT_FOR_ACCOUNT_QUERY = "select count(*) as " + COUNT + " from " + TABLE_NAME + " where " + ACCOUNT_ID + " = ?";
	private static final String GET_BY_ID_QUERY = "select * from " + TABLE_NAME + " where " + ID + " = ?";
	
	private static final String ID_EQUAL_WHERE = ID + " = ?";
	
	UnsentMessageDAO() {
	}
	
	public UnsentMessage getUnsentMessage(long id) {
		SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
		Cursor cursor = null;
		UnsentMessage message = null;
		
		try {
			cursor = db.rawQuery(GET_BY_ID_QUERY, new String[] { id + "" });
			
			if (cursor.getCount() == 1) {
				int idxId = cursor.getColumnIndex(ID);
				int idxAccountId = cursor.getColumnIndex(ACCOUNT_ID);
				int idxText = cursor.getColumnIndex(TEXT);
				int idxType = cursor.getColumnIndex(TYPE);
				int idxTo = cursor.getColumnIndex(TO);
				int idxAttachment = cursor.getColumnIndex(ATTACHMENT);
				
				cursor.moveToNext();
				
				message = new UnsentMessage();
				message.setId(cursor.getLong(idxId));
				message.setAccountId(cursor.getLong(idxAccountId));
				message.setText(cursor.getString(idxText));
				message.setType(cursor.getInt(idxType));
				message.setTo(cursor.getString(idxTo));
				message.setAttachmentUrl(cursor.getString(idxAttachment));
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		return message;

	}
	
	public ArrayList<UnsentMessage> getUnsentMessagesForAccount(long accountId) {
		SQLiteDatabase db = getDatabaseHelper().getReadableDatabase();
		Cursor cursor = null;
		ArrayList<UnsentMessage> result = new ArrayList<UnsentMessage>();
		try {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(TABLE_NAME);
			cursor = db.rawQuery(GET_FOR_ACCOUNT_QUERY, new String[] {"" + accountId}); 
			
			int idxId = cursor.getColumnIndex(ID);
			int idxAccountId = cursor.getColumnIndex(ACCOUNT_ID);
			int idxText = cursor.getColumnIndex(TEXT);
			int idxType = cursor.getColumnIndex(TYPE);
			int idxTo = cursor.getColumnIndex(TO);
			int idxAttachment = cursor.getColumnIndex(ATTACHMENT);
			
			while(cursor.moveToNext()) {
				UnsentMessage message = new UnsentMessage();
				message = new UnsentMessage();
				message.setId(cursor.getLong(idxId));
				message.setAccountId(cursor.getLong(idxAccountId));
				message.setText(cursor.getString(idxText));
				message.setType(cursor.getInt(idxType));
				message.setTo(cursor.getString(idxTo));
				message.setAttachmentUrl(cursor.getString(idxAttachment));
				
				result.add(message);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
		}
		
		return result;

	}

	public long addUnsentMessage(UnsentMessage message) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(ACCOUNT_ID, message.getAccountId());
			values.put(TEXT, message.getText());
			values.put(TYPE, message.getType());
			values.put(TO, message.getTo());
			values.put(ATTACHMENT, message.getAttachmentUrl());
			id = db.insert(TABLE_NAME, null, values);
		} finally {
			db.close();
		}
		
		return id;

	}
	
	public void updateUnsentMessage(UnsentMessage message) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		try {
			ContentValues values = new ContentValues();
			values.put(ACCOUNT_ID, message.getAccountId());
			values.put(TEXT, message.getText());
			values.put(TYPE, message.getType());
			values.put(TO, message.getTo());
			values.put(ATTACHMENT, message.getAttachmentUrl());
			db.update(TABLE_NAME, values, ID_EQUAL_WHERE, new String[] {message.getId() + ""});
		} finally {
			db.close();
		}
	}
	
	public void deleteUnsentMessage(long id) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		
		try {
			db.delete(TABLE_NAME, ID_EQUAL_WHERE, new String[] {id + ""});
		} finally {
			db.close();
		}
	}
	
	public int getUnsentMessagesCountForAccount(long accountId) {
		SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
		Cursor cursor = null;
		int count = 0;
		
		try {
			cursor = db.rawQuery(GET_COUNT_FOR_ACCOUNT_QUERY, new String[] { accountId + ""});
			int idx = cursor.getColumnIndex(COUNT);
			cursor.moveToNext();
			count = cursor.getInt(idx);
		} finally {
			db.close();
			if (cursor !=null) {
				cursor.close();
			}
		}
		return count;
	}
}
