/**
 * 
 */
package com.codeminders.yfrog.client.response;

import java.io.*;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author idemydenko
 *
 */
public class ResponseFactoryTest extends TestCase {
	
	@Test
	public void testParseOk() throws Exception {
		String contentOk = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
						 "	<rsp stat=\"ok\"> " +
						 "		<mediaid>abc123</mediaid> " +
						 "		<mediaurl>http://yfrog.com/abc123</mediaurl> " +
						 "		<statusid>1111</statusid>" +
						 "		<userid>2345</userid>" +
						 "</rsp>";
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter dos = new OutputStreamWriter(baos);
		dos.write(contentOk);
		dos.flush();
		dos.close();
		
		UploadResponse response = ResponseFactory.newResponseFactory().buildResponse(new ByteArrayInputStream(baos.toByteArray()));
		
		baos.close();
		
		assertEquals(response.getStatus(), "ok");
		assertEquals(response.getMediaId(), "abc123");
		assertEquals(response.getMediaUrl(), "http://yfrog.com/abc123");
		assertEquals(response.getStatusId(), Long.valueOf(1111));
		assertEquals(response.getUserId(), Long.valueOf(2345));
	}

	
	@Test
	public void testParseFail() throws Exception {
		
		String contentFail = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
						 "	<rsp stat=\"fail\"> " +
						 "		<err code=\"1001\" msg=\"Invalid twitter username or password\" />" +
						 "</rsp>";
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter dos = new OutputStreamWriter(baos);
		dos.write(contentFail);
		dos.flush();
		dos.close();
		
		UploadResponse response = ResponseFactory.newResponseFactory().buildResponse(new ByteArrayInputStream(baos.toByteArray()));
		
		baos.close();
		
		assertEquals(response.getStatus(), "fail");
		assertEquals(response.getErrorCode(), Integer.valueOf(1001));
		assertEquals(response.getErrorMessage(), "Invalid twitter username or password");
	}

}
