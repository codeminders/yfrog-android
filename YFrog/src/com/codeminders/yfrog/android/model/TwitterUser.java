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
	private String name;
	private URL profileImageURL;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	
}
