/**
 * 
 */
package com.codeminders.yfrog.conn.response;

/**
 * @author idemydenko
 *
 */
public class UploadResponse {
	public static final String RESPONSE_NAME = "rsp";
	public static final String RESPONSE_STATUS_NAME = "stat";
	public static final String RESPONSE_MEDIAID_NAME = "mediaid";
	public static final String RESPONSE_MEDIAURL_NAME = "mediaurl";
	public static final String RESPONSE_USERID_NAME = "userid";
	public static final String RESPONSE_STATUSID_NAME = "statusid";
	public static final String RESPONSE_ERROR_NAME = "err";
	public static final String RESPONSE_ERROR_CODE_NAME = "code";
	public static final String RESPONSE_ERROR_MESSAGE_NAME = "msg";
	
	
	public static final String STATUS_OK = "ok";
	public static final String STATUS_FAIL = "fail";
	
	private String status;
	private String mediaId;
	private String mediaUrl;
	private Long userId;
	private Long statusId;
	private Integer errorCode;
	private String errorMessage;
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the mediaId
	 */
	public String getMediaId() {
		return mediaId;
	}
	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl() {
		return mediaUrl;
	}
	/**
	 * @param mediaUrl the mediaUrl to set
	 */
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the statusId
	 */
	public Long getStatusId() {
		return statusId;
	}
	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	/**
	 * @return the errorCode
	 */
	public Integer getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(RESPONSE_STATUS_NAME);
		builder.append(": ");
		builder.append(status);
		builder.append("\n");
		
		if (STATUS_OK.equals(status)) {
			builder.append(RESPONSE_MEDIAID_NAME);
			builder.append(": ");
			builder.append(mediaId);
			builder.append("\n");
			
			builder.append(RESPONSE_MEDIAURL_NAME);
			builder.append(": ");
			builder.append(mediaUrl);
			builder.append("\n");

			builder.append(RESPONSE_USERID_NAME);
			builder.append(": ");
			builder.append(userId);
			builder.append("\n");

			builder.append(RESPONSE_STATUSID_NAME);
			builder.append(": ");
			builder.append(statusId);
			builder.append("\n");
		}
		
		if (STATUS_FAIL.equals(status)) {
			builder.append(RESPONSE_ERROR_CODE_NAME);
			builder.append(": ");
			builder.append(errorCode);
			builder.append("\n");

			builder.append(RESPONSE_ERROR_MESSAGE_NAME + ": " + errorMessage + "\n");
			builder.append(": ");
			builder.append(errorMessage);
			builder.append("\n");

		}
		return builder.toString();
	}
}
