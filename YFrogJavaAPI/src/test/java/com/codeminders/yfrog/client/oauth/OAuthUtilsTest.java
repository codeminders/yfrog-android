/**
 * 
 */
package com.codeminders.yfrog.client.oauth;

import junit.framework.TestCase;

/**
 * @author idemydenko
 *
 */
public class OAuthUtilsTest extends TestCase {
	private static final String TWITTER_VERIFY_URL1 = "https://twitter.com/account/verify_credentials.xml?" +
	"oauth_version=1.0&" +
	"oauth_nonce=ec16aaa5c2c366429175c359f6b902dd&" +
	"oauth_timestamp=1256574748&" +
	"oauth_consumer_key=16F75LNJxjKTIUHidy5Sg&" +
	"oauth_token=75972934-ruQrtHRnOUk8ay94FHqMpjrKy01hArU9DNFCkd8KQ&" +
	"oauth_signature_method=HMAC-SHA1&" +
	"oauth_signature=H%2BwLlbLa6ru5fd%2Bp1BY3DMVpwT4%3D";

	
	private static final String TOKEN = "75972934-ruQrtHRnOUk8ay94FHqMpjrKy01hArU9DNFCkd8KQ";
	private static final String TOKEN_SECRET = "d5785u0tf9UUXXYjZ0e5qR0ScjjHfjBqoGaBfEo";
	private static final String CONSUMER = "16F75LNJxjKTIUHidy5Sg";
	private static final String CONSUMER_SECRET = "Sp3gGl1RvWtICmphby4MAomRCTj9sGvcE8b7XqUxxnQ";
	
	public void testGetOAuthVerifyUrl() throws Exception {
		String url = OAuthHelper.getOAuthVerifyUrl(TOKEN, TOKEN_SECRET, CONSUMER, CONSUMER_SECRET);
		
		System.out.println(url);
		
		System.out.println(TWITTER_VERIFY_URL1);
	}
}
