/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.net.URL;

/**
 * @author idemydenko
 *
 */
public class TwitterUser {
	private int id;
	private String fullname;
	private URL profileImageURL;
	private String username;
	private String location;
	private String description;
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
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}
	/**
	 * @param fullname the fullname to set
	 */
	public void setFullname(String name) {
		this.fullname = name;
	}
	/**
	 * @return the profileImageURL
	 */
	public URL getProfileImageURL() {
		return profileImageURL;
	}
	/**
	 * @param profileImageURL the profileImageURL to set
	 */
	public void setProfileImageURL(URL profileImageURL) {
		this.profileImageURL = profileImageURL;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
