/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.util.Date;

/**
 * @author idemydenko
 *
 */
public class TwitterStatus {
	private String text;
	private Date createdAt;
	private TwitterUser user;
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
	
	
}
