/**
 * 
 */
package com.codeminders.yfrog.conn.request;

/**
 * @author idemydenko
 *
 */
public class UrlUploadRequest extends UploadRequest {
	private String url;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(FIELD_URL + ": " + getUrl() + "\n");
		return builder.toString();
	}

}
