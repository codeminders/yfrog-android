/**
 * 
 */
package com.codeminders.yfrog.client;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Test;

import com.codeminders.yfrog.client.impl.YFrogClientImpl;
import com.codeminders.yfrog.client.oauth.OAuthHelper;
import com.codeminders.yfrog.client.request.*;
import com.codeminders.yfrog.client.response.UploadResponse;

/**
 * @author idemydenko
 *
 */
public class YFrogImplTest extends TestCase {

	@Test
	public void testUploadUrl() throws Exception {
		UrlUploadRequest request = new UrlUploadRequest();
		request.setUsername("Dem_off");
		request.setPassword("dem123");
		request.setPublic(true);
		request.setUrl("http://blogs.chron.com/soccer/archives/zidane.JPG");
		
		YFrogClient frog = new YFrogClientImpl();
		UploadResponse response = frog.upload(request);
		
		assertEquals(response.getStatus(), "ok");
	}

	@Test
	public void testUploadAndPostUrl() throws Exception {
		UrlUploadRequest request = new UrlUploadRequest();
		request.setUsername("Dem_off");
		request.setPassword("dem123");
		request.setPublic(true);
		request.setUrl("http://blogs.chron.com/soccer/archives/zidane.JPG");
		
		YFrogClient frog = new YFrogClientImpl();
		UploadResponse response = frog.uploadAndPost(request);
		
		assertEquals(response.getStatus(), "ok");
	}

	
	@Test
	public void testUploadFile() throws Exception {
		FileUploadRequest request = new FileUploadRequest();
		request.setUsername("Dem_off");
		request.setPassword("dem123");
		request.setPublic(true);
		request.setFile(new File("src/test/resources/ulitko.jpg"));

		YFrogClient frog = new YFrogClientImpl();
		UploadResponse response = frog.upload(request);
		
		assertEquals(response.getStatus(), "ok");
	}

	@Test
	public void testUploadInputStream() throws Exception {
		InputStreamUploadRequest request = new InputStreamUploadRequest();
		request.setUsername("Dem_off");
		request.setPassword("dem123");
		request.setPublic(true);
		request.setInputStream(this.getClass().getResourceAsStream("/krivetko.jpg"));
		request.setFilename("krivetko.jpg");

		YFrogClient frog = new YFrogClientImpl();
		UploadResponse response = frog.upload(request);
		
		assertEquals(response.getStatus(), "ok");
	}

	@Test
	public void testCheckParams() throws Exception {
		InputStreamUploadRequest request = new InputStreamUploadRequest();
		request.setUsername("Dem_off");
		request.setPassword("");
		request.setPublic(true);
		request.setInputStream(this.getClass().getResourceAsStream("/krivetko.jpg"));
		request.setFilename("krivetko.jpg");
		
		YFrogClient frog = new YFrogClientImpl();
		
		try {
			frog.upload(request);
			fail();
		} catch (IllegalArgumentException e) {
		}
		
		request = new InputStreamUploadRequest();
		request.setPassword("dem123");
		request.setPublic(true);
		request.setInputStream(this.getClass().getResourceAsStream("/krivetko.jpg"));
		request.setFilename("krivetko.jpg");
		
		try {
			frog.upload(request);
			fail();
		} catch (IllegalArgumentException e) {			
		}
		
		request = new InputStreamUploadRequest();
		request.setUsername("Dem_off");
		request.setPassword("dem123");
		request.setPublic(true);
		request.setInputStream(this.getClass().getResourceAsStream("/fkrivetko.jpg"));
		request.setFilename("krivetko.jpg");
		
		try {
			frog.upload(request);
			fail();
		} catch (IllegalArgumentException e) {			
		}
		
		UrlUploadRequest request1 = new UrlUploadRequest();
		request1.setUsername("Dem_off");
		request1.setPassword("dem123");
		request1.setPublic(true);
		
		try {
			frog.upload(request1);
			fail();
		} catch (IllegalArgumentException e) {			
		}
		
		FileUploadRequest request2 = new FileUploadRequest();
		request2.setUsername("Dem_off");
		request2.setPassword("dem123");
		request2.setPublic(true);

		try {
			frog.upload(request2);
			fail();
		} catch (IllegalArgumentException e) {			
		}
	}

	private static final String TOKEN = "75972934-ruQrtHRnOUk8ay94FHqMpjrKy01hArU9DNFCkd8KQ";
	private static final String TOKEN_SECRET = "d5785u0tf9UUXXYjZ0e5qR0ScjjHfjBqoGaBfEo";
	private static final String CONSUMER = "16F75LNJxjKTIUHidy5Sg";
	private static final String CONSUMER_SECRET = "Sp3gGl1RvWtICmphby4MAomRCTj9sGvcE8b7XqUxxnQ";

	@Test
	public void testOAuthUploadFile() throws Exception {
		FileUploadRequest request = new FileUploadRequest();
		request.setUsername("YFROG_ANDROID");
//		request.setPassword("dem123");
		String u = OAuthHelper.getOAuthVerifyUrl(TOKEN, TOKEN_SECRET, CONSUMER, CONSUMER_SECRET);
		request.setVerifyUrl(u);
		request.setPublic(true);
		request.setFile(new File("src/test/resources/ulitko.jpg"));

		YFrogClient frog = new YFrogClientImpl();
		UploadResponse response = frog.upload(request);
		
		System.out.println(response);
		assertEquals(response.getStatus(), "ok");
		
	}

}
