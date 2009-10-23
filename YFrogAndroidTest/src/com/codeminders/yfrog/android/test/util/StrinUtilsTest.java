/**
 * 
 */
package com.codeminders.yfrog.android.test.util;

import com.codeminders.yfrog.android.util.StringUtils;

import junit.framework.TestCase;

/**
 * @author idemydenko
 *
 */
public class StrinUtilsTest extends TestCase {
	
	public void testHighlight() throws Exception {
		String str = "hello bla yes bla bla  no good bla bad bla";
		
		StringUtils.highlightText(str, "bla");
	}
	
	public void testParseYFrog() throws Exception {
		String str = "AnneHeijkoop Gutentag!! http://yfrog.com/0c6xbj";
		StringUtils.parseURLs(str, null);
		
		str = "AnneHeijkoop Gutentag!! http://yfrog.com/cccc  http://yfrog.com/bbbb AnneHeijkoop Gutentag!! http://yfrog.com/aaaa";
		StringUtils.parseURLs(str, null);

	}
}
