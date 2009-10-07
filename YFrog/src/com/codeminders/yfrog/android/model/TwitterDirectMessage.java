/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.util.Date;

/**
 * @author idemydenko
 *
 */
public class TwitterDirectMessage {
	private int id;
	private TwitterUser sender;
	private String text;
	private Date createdAt;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
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
	
}