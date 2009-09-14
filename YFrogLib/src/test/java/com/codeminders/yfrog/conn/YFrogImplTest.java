/**
 * 
 */
package com.codeminders.yfrog.conn;

import java.io.File;

import org.junit.Test;

import com.codeminders.yfrog.conn.YFrog;
import com.codeminders.yfrog.conn.impl.YFrogImpl;
import com.codeminders.yfrog.conn.request.FileUploadRequest;
import com.codeminders.yfrog.conn.request.InputStreamUploadRequest;
import com.codeminders.yfrog.conn.request.UploadRequest;
import com.codeminders.yfrog.conn.request.UrlUploadRequest;
import com.codeminders.yfrog.conn.response.UploadResponse;

import junit.framework.TestCase;

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
		
		YFrog frog = new YFrogImpl();
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
		
		YFrog frog = new YFrogImpl();
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

		YFrog frog = new YFrogImpl();
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

		YFrog frog = new YFrogImpl();
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
		
		YFrog frog = new YFrogImpl();
		
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

}