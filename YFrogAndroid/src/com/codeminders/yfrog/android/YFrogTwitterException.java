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

	private int errorCode;
	
	public YFrogTwitterException() {
		super();
	}
	
	public YFrogTwitterException(String message) {
		super(message);
	}
	
	public YFrogTwitterException(Throwable throwable) {
		super(throwable);
	}
	
	public YFrogTwitterException(Throwable throwable, int errorCode) {
		super(throwable);
	}
	
	public YFrogTwitterException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}