/**
 * 
 */
package com.codeminders.yfrog.client;

import java.io.IOException;
import java.util.Properties;

import com.codeminders.yfrog.client.request.*;
import com.codeminders.yfrog.client.response.*;

/**
 * @author idemydenko
 *
 */
public abstract class YFrogClient {
	public static final String BASE;
	public static final String ACTION_UPLOAD;
	public static final String ACTION_UPLOAD_AND_POST;
	
	private static final String PROPS_FILE_NAME = "yfrog.properties";

	private static final String DEFAULT_BASE = "http://yfrog.com/api/";
	private static final String DEFAULT_ACTION_UPLOAD = "upload";
	private static final String DEFAULT_ACTION_UPLOAD_AND_POST = "uploadAndPost";

	static {
		Properties props = new Properties();
		try {
			props.load(YFrogClient.class.getResourceAsStream(PROPS_FILE_NAME));
		} catch (IOException e) {
		}

		BASE = props.getProperty("base.url", DEFAULT_BASE);
		ACTION_UPLOAD = props.getProperty("action.upload", DEFAULT_ACTION_UPLOAD);
		ACTION_UPLOAD_AND_POST = props.getProperty("action.uploadAndPost", DEFAULT_ACTION_UPLOAD_AND_POST);
	} 
	
	
	public abstract UploadResponse upload(UploadRequest request) throws IOException, UploadResponseFormatException;
	
	public abstract UploadResponse uploadAndPost(UploadRequest request) throws IOException, UploadResponseFormatException;
	
	protected void checkParams(UploadRequest request) {
		if (isEmpty(request.getUsername())) {
			throw new IllegalArgumentException("Usernme can't be empty");
		}
		if (isEmpty(request.getPassword())) {
			throw new IllegalArgumentException("Password can't be empty");
		}
		
		if (request instanceof UrlUploadRequest) {
			UrlUploadRequest req = (UrlUploadRequest) request;
			if (isEmpty(req.getUrl())) {
				throw new IllegalArgumentException("Url can't be empty");
			}
		} else if (request instanceof FileUploadRequest) {
			FileUploadRequest req = (FileUploadRequest) request;
			if (req.getFile() == null || !req.getFile().exists()) {
				throw new IllegalArgumentException("File not found");
			}
		} else if (request instanceof InputStreamUploadRequest) {
			InputStreamUploadRequest req = (InputStreamUploadRequest) request;
			if (req.getInputStream() == null) {
				throw new IllegalArgumentException("InputStream may not be null");
			}
			if (isEmpty(req.getFilename())) {
				throw new IllegalArgumentException("Filename can't be empty");
			}
		}
	}
	
	protected boolean isEmpty(String param) {
		return param == null || param.trim().length() == 0;
	}
}
