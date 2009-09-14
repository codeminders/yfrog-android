/**
 * 
 */
package com.codeminders.yfrog.android.model;

/**
 * @author idemydenko
 *
 */
public class Account {
	private Integer id;
	private String email;
	private String nickname;
	private String password;
	private String oauthKey;
	
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the oauthKey
	 */
	public String getOauthKey() {
		return oauthKey;
	}
	/**
	 * @param oauthKey the oauthKey to set
	 */
	public void setOauthKey(String oauthKey) {
		this.oauthKey = oauthKey;
	}
	
	
}
