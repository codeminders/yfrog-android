/**
 * 
 */
package com.codeminders.yfrog2.android;

/**
 * @author idemydenko
 *
 */
public class YFrogTwitterException extends YFrogException {
	private static final long serialVersionUID = 2699095704037915524L;

	public YFrogTwitterException() {
		super();
	}

	public YFrogTwitterException(int errorCode) {
		super(errorCode);
	}

	public YFrogTwitterException(String message) {
		super(message);
	}
	
	public YFrogTwitterException(Throwable throwable) {
		super(throwable);
	}
	
	public YFrogTwitterException(Throwable throwable, int errorCode) {
		super(throwable, errorCode);
	}
	
	public YFrogTwitterException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
