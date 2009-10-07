/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author idemydenko
 *
 */
public class Account implements Serializable {
	public static final int METHOD_COMMON = 0;
	public static final int METHOD_OAUTH = 1;
	
	public static final int OAUTH_STATUS_NOT_AUTHORIZED = 0;
	public static final int OAUTH_STATUS_WAIT_VERIFICATION = 1;
	public static final int OAUTH_STATUS_VERIFIED = 2;
	
	private long id;
	private String name;
	private String email;
	private String nickname;
	private String password;
	private String oauthToken;
	private String oauthTokenSecret;
	private int authMethod = METHOD_COMMON;
	private int oauthStatus = OAUTH_STATUS_NOT_AUTHORIZED;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public int getAuthMethod() {
		return authMethod;
	}

	public void setAuthMethod(int authMethod) {
		this.authMethod = authMethod;
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
		return authMethod == METHOD_OAUTH && oauthStatus != OAUTH_STATUS_VERIFIED;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeLong(id);
		out.writeUTF(name == null ? "" : name);
		out.writeUTF(nickname == null ? "" : nickname);
		out.writeUTF(password == null ? "" : password);
		out.writeUTF(email == null ? "" : email);
		out.writeUTF(oauthToken == null ? "" : oauthToken);
		out.writeUTF(oauthTokenSecret == null ? "" : oauthTokenSecret);
		out.writeInt(authMethod);
		out.writeInt(oauthStatus);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readLong();
		name = in.readUTF();
		nickname = in.readUTF();
		password = in.readUTF();
		email = in.readUTF();
		oauthToken = in.readUTF();
		oauthTokenSecret = in.readUTF();
		authMethod = in.readInt();
		oauthStatus = in.readInt();
	}
}
