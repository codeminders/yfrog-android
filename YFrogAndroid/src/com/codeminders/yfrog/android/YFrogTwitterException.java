/**
 * 
 */
package com.codeminders.yfrog.android;

/**
 * @author idemydenko
 *
 */
public class YFrogTwitterException extends Exception {
	private static final long serialVersionUID = 7648098640662542647L;

	private int errCode;
	
	public YFrogTwitterException() {
		super();
	}

	public YFrogTwitterException(int errorCode) {
		super();
		errCode = errorCode; 
	}

	public YFrogTwitterException(String message) {
		super(message);
	}
	
	public YFrogTwitterException(Throwable throwable) {
		super(throwable);
	}
	
	public YFrogTwitterException(Throwable throwable, int errorCode) {
		super(throwable);
		errCode = errorCode;
	}
	
	public YFrogTwitterException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public int getErrorCode() {
		return errCode;
	}
}
