/**
 * 
 */
package com.codeminders.yfrog2.android.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author idemydenko
 *
 */
public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2298093644093301976L;
	public static final int OAUTH_STATUS_NOT_AUTHORIZED = 0;
	public static final int OAUTH_STATUS_WAIT_VERIFICATION = 1;
	public static final int OAUTH_STATUS_VERIFIED = 2;
	
	private static final int FALSE = 0;
	
	private long id;
	private String username;
	private String oauthToken;
	private String oauthTokenSecret;
	private int oauthStatus = OAUTH_STATUS_NOT_AUTHORIZED;
	private int postLocation = FALSE;
	private int scaleImage = FALSE;
	private int updateProfileLoc = FALSE;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String nickname) {
		this.username = nickname;
	}
	
	public String getOauthToken() {
		return oauthToken;
	}

	public void setOauthToken(String oauthKey) {
		this.oauthToken = oauthKey;
	}

	public String getOauthTokenSecret() {
		return oauthTokenSecret;
	}

	public void setOauthTokenSecret(String tolkenSecret) {
		this.oauthTokenSecret = tolkenSecret;
	}

	public int getOauthStatus() {
		return oauthStatus;
	}

	public void setOauthStatus(int oauthStatus) {
		this.oauthStatus = oauthStatus;
	}
	
	public boolean isOAuthVerified() {
		return oauthStatus == OAUTH_STATUS_VERIFIED;
	}
	
	public boolean isNeedOAuthAuthorization() {
		return oauthStatus != OAUTH_STATUS_VERIFIED;
	}

	public void setPostLocationStatus(int postlocation) {
		postLocation = postlocation;
	}

	public void setPostLocationStatus(boolean postlocation) {
		postLocation = !postlocation ? FALSE : 1;
	}
	
	public boolean isPostLocation() {
		return postLocation != FALSE;
	}
	
	public int getPostLocation() {
		return postLocation;
	}
	
	public void setScaleImage(int scale) {
		this.scaleImage = scale;
	}

	public void setScaleImage(boolean scaleImage) {
		this.scaleImage = !scaleImage ? FALSE : 1;
	}
	
	public boolean isScaleImage() {
		return scaleImage != FALSE;
	}

	public int getScaleImage() {
		return scaleImage;
	}
	
	public void setUpdateProfileLocation(int updateProfileLoc) {
		this.updateProfileLoc = updateProfileLoc;
	}
	
	public void setUpdateProfileLocation(boolean updateProfileLoc) {
		this.updateProfileLoc = !updateProfileLoc ? FALSE : 1;
	}

	public int getUpdateProfileLocation() {
		return updateProfileLoc;
	}
	
	public boolean isUpdateProfileLocation() {
		return updateProfileLoc != FALSE;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeLong(id);
		out.writeUTF(username == null ? "" : username);
		out.writeUTF(oauthToken == null ? "" : oauthToken);
		out.writeUTF(oauthTokenSecret == null ? "" : oauthTokenSecret);
		out.writeInt(oauthStatus);
		out.writeInt(postLocation);
		out.writeInt(scaleImage);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readLong();
		username = in.readUTF();
		oauthToken = in.readUTF();
		oauthTokenSecret = in.readUTF();
		oauthStatus = in.readInt();
		postLocation = in.readInt();
		scaleImage = in.readInt();
	}
	
}
