/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;

import com.codeminders.yfrog.android.StringUtils;

/**
 * @author idemydenko
 *
 */
public class UnsentMessage implements Serializable {
	public static final int TYPE_STATUS = 0;
	public static final int TYPE_REPLAY = 1;
	public static final int TYPE_PUBLIC_REPLAY = 2;
	public static final int TYPE_DIRECT_MESSAGE = 3;
	
	private long id;
	private long accountId;
	private String text;
	private int type = TYPE_STATUS;
	private String to;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long account_id) {
		this.accountId = account_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeLong(id);
		out.writeLong(accountId);
		out.writeUTF(text == null ? StringUtils.EMPTY_STRING : text);
		out.writeInt(type);
		out.writeUTF(to == null ? StringUtils.EMPTY_STRING : to);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readLong();
		accountId = in.readLong();
		text = in.readUTF();
		type = in.readInt();
		to = in.readUTF();
	}

}
