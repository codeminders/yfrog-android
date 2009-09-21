/**
 * 
 */
package com.codeminders.yfrog.android;

/**
 * @author idemydenko
 *
 */
public class YFrogTwitterException extends Exception {
	private static final long serialVersionUID = -6510678098767544259L;

	public YFrogTwitterException() {
		super();
	}
	
	public YFrogTwitterException(String message) {
		super(message);
	}
	
	public YFrogTwitterException(Throwable throwable) {
		super(throwable);
	}
	
	public YFrogTwitterException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
