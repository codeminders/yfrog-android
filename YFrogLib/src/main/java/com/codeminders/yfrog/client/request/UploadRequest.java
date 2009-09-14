/**
 * 
 */
package com.codeminders.yfrog.client.request;

/**
 * @author idemydenko
 *
 */
public abstract class UploadRequest {
	public static final String FIELD_USERNAME = "username";
	public static final String FIELD_PASSWORD = "password";
	public static final String FIELD_TAGS = "tags";
	public static final String FIELD_KEY = "key";
	public static final String FIELD_PUBLIC = "public";
	public static final String FIELD_URL = "url";
	public static final String FIELD_MEDIA = "media";
	
	private static final String PUBLIC_VALUE_YES = "yes";
	private static final String PUBLIC_VALUE_NO = "no";
	
	private String username;
	private String password;
	private String tags;
	private boolean isPublic;
	private String key;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username == null ? "" : username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password == null ? "" : password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags == null ? "" : tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}
	/**
	 * @return the isPublic
	 */
	public boolean isPublic() {
		return isPublic;
	}
	/**
	 * @param isPublic the isPublic to set
	 */
	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public String getPublicAsString() {
		return isPublic() ? PUBLIC_VALUE_YES : PUBLIC_VALUE_NO;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key == null ? "" : key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(FIELD_USERNAME);
		builder.append(": ");
		builder.append(username);
		builder.append("\n");

		builder.append(FIELD_PASSWORD);
		builder.append(": ");
		builder.append(password);
		builder.append("\n");

		builder.append(FIELD_TAGS);
		builder.append(": ");
		builder.append(tags);
		builder.append("\n");

		builder.append(FIELD_PUBLIC);
		builder.append(": ");
		builder.append(getPublicAsString());
		builder.append("\n");

		builder.append(FIELD_KEY + ": " + getKey() + "\n");
		builder.append(": ");
		builder.append(getKey());
		builder.append("\n");

		return builder.toString();
	}
}
