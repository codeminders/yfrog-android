/**
 * 
 */
package com.codeminders.yfrog.conn.response;

import java.io.IOException;
import java.io.InputStream;

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
