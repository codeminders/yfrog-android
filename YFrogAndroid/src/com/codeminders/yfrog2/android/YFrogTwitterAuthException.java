/**
 * 
 */
package com.codeminders.yfrog2.android;

/**
 * @author idemydenko
 *
 */
public class YFrogTwitterAuthException extends YFrogTwitterException {
	private static final long serialVersionUID = -7382450005238410141L;

	public YFrogTwitterAuthException() {
		super();
	}
	
	public YFrogTwitterAuthException(String message) {
		super(message);
	}
	
	public YFrogTwitterAuthException(Throwable throwable) {
		super(throwable);
	}
	
	public YFrogTwitterAuthException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
