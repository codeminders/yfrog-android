/**
 * 
 */
package com.codeminders.yfrog.client.response;

/**
 * @author idemydenko
 *
 */
public class UploadResponseFormatException extends Exception {
	private static final long serialVersionUID = -3696242406755642580L;

	public UploadResponseFormatException() {
	}
	
	public UploadResponseFormatException(String message) {
		super(message);
	}
	
	public UploadResponseFormatException(String message, Throwable caused) {
		super(message, caused);
	}

	public UploadResponseFormatException(Throwable caused) {
		super(caused);
	}
}
