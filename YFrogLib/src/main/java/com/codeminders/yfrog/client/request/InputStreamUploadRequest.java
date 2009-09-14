/**
 * 
 */
package com.codeminders.yfrog.client.request;

import java.io.InputStream;

/**
 * @author idemydenko
 *
 */
public class InputStreamUploadRequest extends UploadRequest {
	private InputStream is;
	private String filename;
	
	public InputStream getInputStream() {
		return is;
	}
	
	public void setInputStream(InputStream is) {
		this.is = is;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(FIELD_MEDIA);
		builder.append(": ");
		builder.append(is);
		builder.append(" | ");
		builder.append(filename);
		builder.append("\n");

		return builder.toString();
	}
}
