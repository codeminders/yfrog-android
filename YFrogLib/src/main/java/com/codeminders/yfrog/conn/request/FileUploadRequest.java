/**
 * 
 */
package com.codeminders.yfrog.conn.request;

import java.io.File;

/**
 * @author idemydenko
 *
 */
public class FileUploadRequest extends UploadRequest {
	private File file;

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.conn.conn.conn.request.UploadRequest#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(FIELD_MEDIA);
		builder.append(": ");
		builder.append(file);
		builder.append("\n");

		return builder.toString();
	}
}
