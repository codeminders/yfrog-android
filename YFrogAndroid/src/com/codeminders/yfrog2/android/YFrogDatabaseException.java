/**
 * 
 */
package com.codeminders.yfrog2.android;

/**
 * @author idemydenko
 *
 */
public class YFrogDatabaseException extends YFrogException {
	public YFrogDatabaseException() {
		super();
	}

	public YFrogDatabaseException(int errorCode) {
		super(errorCode);
	}

	public YFrogDatabaseException(String message) {
		super(message);
	}
}
