/**
 * 
 */
package com.codeminders.yfrog2.android.model;

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
	private boolean follower;
	private boolean following;
	private boolean isProtected;
	private int followersCount;
	private int followingsCount;
	
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
	
	public String getScreenUsername() {
		return "@" + username;
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
	/**
	 * @return the follower
	 */
	public boolean isFollower() {
		return follower;
	}
	/**
	 * @param follower the follower to set
	 */
	public void setFollower(boolean follower) {
		this.follower = follower;
	}
	/**
	 * @return the follower
	 */
	public boolean isFollowing() {
		return following;
	}
	/**
	 * @param follower the follower to set
	 */
	public void setFollowing(boolean following) {
		this.following = following;
	}
	/**
	 * @return the isProtected
	 */
	public boolean isProtected() {
		return isProtected;
	}
	/**
	 * @param isProtected the isProtected to set
	 */
	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

	/**
	 * @return the followersCount
	 */
	public int getFollowersCount() {
		return followersCount;
	}
	/**
	 * @param followersCount the followersCount to set
	 */
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	/**
	 * @return the followingsCount
	 */
	public int getFollowingsCount() {
		return followingsCount;
	}
	/**
	 * @param followingsCount the followingsCount to set
	 */
	public void setFollowingsCount(int followingsCount) {
		this.followingsCount = followingsCount;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeLong(id);
		out.writeUTF(username);
		out.writeUTF(fullname);
		out.writeUTF(location);
		out.writeUTF(description);
		out.writeObject(profileImageURL);
		out.writeBoolean(follower);
		out.writeBoolean(following);
		out.writeBoolean(isProtected);
		out.writeInt(followersCount);
		out.writeInt(followingsCount);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readLong();
		username = in.readUTF();
		fullname = in.readUTF();
		location = in.readUTF();
		description = in.readUTF();
		profileImageURL = (URL) in.readObject();
		follower = in.readBoolean();
		following = in.readBoolean();
		isProtected = in.readBoolean();
		followersCount = in.readInt();
		followingsCount = in.readInt();
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
