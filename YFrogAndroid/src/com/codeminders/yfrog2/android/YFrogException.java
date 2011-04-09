/**
 * 
 */
package com.codeminders.yfrog2.android;

/**
 * @author idemydenko
 *
 */
public class YFrogException extends Exception {
	private static final long serialVersionUID = -8595677756852314787L;

	private int errCode;
	
	public YFrogException() {
		super();
	}

	public YFrogException(int errorCode) {
		super();
		errCode = errorCode; 
	}

	public YFrogException(String message) {
		super(message);
	}
	
	public YFrogException(Throwable throwable) {
		super(throwable);
	}
	
	public YFrogException(Throwable throwable, int errorCode) {
		super(throwable);
		errCode = errorCode;
	}
	
	public YFrogException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public int getErrorCode() {
		return errCode;
	}

}
