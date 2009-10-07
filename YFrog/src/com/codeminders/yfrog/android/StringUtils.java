/**
 * 
 */
package com.codeminders.yfrog.android;

import java.util.regex.Pattern;

/**
 * @author idemydenko
 *
 */
public final class StringUtils {
	public static final String EMPTY_STRING = "";
	
	private static final String EMAIL_PATTERN = "[\\p{Alnum}\\.\\-_\\+]{1,}@[\\p{Alnum}\\.\\-_]{1,}\\.[\\p{Alpha}]{1,5}";
	
	private static Pattern emailPattern = null;
	
	static {
		emailPattern = Pattern.compile(EMAIL_PATTERN);
	}
	private StringUtils() {
	}
	
	public static final boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	public static final boolean isEmail(String email) {
		return emailPattern.matcher(email).matches();
	}
}
