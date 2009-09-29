/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * @author idemydenko
 *
 */
public class TwitterUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String fullname;
	private URL profileImageURL;
	private String username;
	private String location;
	private String description;
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
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeLong(id);
		out.writeUTF(username);
		out.writeUTF(fullname);
		out.writeUTF(location);
		out.writeUTF(description);
		out.writeObject(profileImageURL);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readLong();
		username = in.readUTF();
		fullname = in.readUTF();
		location = in.readUTF();
		description = in.readUTF();
		profileImageURL = (URL) in.readObject();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof TwitterUser)) {
			return false;
		}
		
		TwitterUser castObj = (TwitterUser) o;
		
		if (id != castObj.id) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 13;
		
		result = result * prime + (int) (id >>> 32);
		return result;
	}
}
