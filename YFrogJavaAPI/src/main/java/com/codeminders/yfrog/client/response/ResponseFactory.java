/**
 * 
 */
package com.codeminders.yfrog.client.response;

import java.io.*;

/**
 * @author idemydenko
 *
 */
public abstract class ResponseFactory {
	
	public static ResponseFactory newResponseFactory() {
		return new XMLResponseFactory();
	}
	
	public abstract UploadResponse buildResponse(InputStream is)  throws IOException, UploadResponseFormatException;

}
