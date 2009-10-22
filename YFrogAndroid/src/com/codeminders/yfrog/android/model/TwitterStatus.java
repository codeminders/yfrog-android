/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.io.*;
import java.util.Date;

/**
 * @author idemydenko
 *
 */
public class TwitterStatus implements Serializable {
	private static final long serialVersionUID = 2L;

	private long id;
	private String text;
	private Date createdAt;
	private TwitterUser user;
	private boolean favorited;
	
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
	/**
	 * @return the user
	 */
	public TwitterUser getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(TwitterUser user) {
		this.user = user;
	}
	
	/**
	 * @return the favorited
	 */
	public boolean isFavorited() {
		return favorited;
	}
	/**
	 * @param favorited the favorited to set
	 */
	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeLong(id);
		out.writeUTF(text);
		out.writeObject(user);
		out.writeObject(createdAt);
		out.writeBoolean(favorited);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readLong();
		text = in.readUTF();
		user = (TwitterUser) in.readObject();
		createdAt = (Date) in.readObject();
		favorited = in.readBoolean();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 13;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof TwitterStatus))
			return false;

		TwitterStatus castObj = (TwitterStatus) obj;
		if (id != castObj.id)
			return false;
		return true;
	}
	
	
}
