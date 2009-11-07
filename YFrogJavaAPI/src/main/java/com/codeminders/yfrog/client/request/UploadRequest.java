/**
 * 
 */
package com.codeminders.yfrog.client.request;

/**
 * @author idemydenko
 *
 */
public abstract class UploadRequest {
	private static final String EMPTY_STRING = ""; 
	
	public static final String FIELD_USERNAME = "username";
	public static final String FIELD_PASSWORD = "password";
	public static final String FIELD_TAGS = "tags";
	public static final String FIELD_KEY = "key";
	public static final String FIELD_PUBLIC = "public";
	public static final String FIELD_URL = "url";
	public static final String FIELD_MEDIA = "media";
	public static final String FIELD_MESSAGE = "message";
	public static final String FIELD_VERIFY_URL = "verify_url";
	public static final String FIELD_AUTH = "auth";
	
	public static final String VAL_AUTH_OAUTH = "oauth";
	
	private static final String PUBLIC_VALUE_YES = "yes";
	private static final String PUBLIC_VALUE_NO = "no";
	
	private String username;
	private String password;
	private String tags;
	private boolean isPublic = true;
	private String key;
	private String message;
	private String verifyUrl;
	private String mediaMimeType;

	public String getUsername() {
		return username == null ? EMPTY_STRING : username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password == null ? EMPTY_STRING : password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTags() {
		return tags == null ? EMPTY_STRING : tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	public String getPublicAsString() {
		return isPublic() ? PUBLIC_VALUE_YES : PUBLIC_VALUE_NO;
	}

	public String getKey() {
		return key == null ? EMPTY_STRING : key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}
	
	public String getMediaMimeType() {
		return mediaMimeType == null ? "application/octet-stream" : mediaMimeType;
	}

	public void setMediaMimeType(String mimeType) {
		this.mediaMimeType = mimeType;
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
