/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.util.Date;

/**
 * @author idemydenko
 *
 */
public class TwitterSearchResult {
	private long id;
	private String text;
	private Date createdAt;
	private String fromUser;
	private String profileImageUrl;
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
	 * @return the fromUser
	 */
	public String getFromUser() {
		return fromUser;
	}
	/**
	 * @return the fromUser
	 */
	public String getScreenFromUser() {
		return "@" + fromUser;
	}

	/**
	 * @param fromUser the fromUser to set
	 */
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	/**
	 * @return the profileImageUrl
	 */
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	/**
	 * @param profileImageUrl the profileImageUrl to set
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}
