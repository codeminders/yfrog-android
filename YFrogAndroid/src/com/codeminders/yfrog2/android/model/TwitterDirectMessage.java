/**
 * 
 */
package com.codeminders.yfrog2.android.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * @author idemydenko
 *
 */
public class TwitterDirectMessage implements Serializable {
	private long id;
	private TwitterUser sender;
	private String text;
	private Date createdAt;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the sender
	 */
	public TwitterUser getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(TwitterUser sender) {
		this.sender = sender;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeLong(id);
		out.writeObject(sender);
		out.writeUTF(text);
		out.writeObject(createdAt);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readInt();
		sender = (TwitterUser) in.readObject();
		text = in.readUTF();
		createdAt = (Date) in.readObject();
	}

}
