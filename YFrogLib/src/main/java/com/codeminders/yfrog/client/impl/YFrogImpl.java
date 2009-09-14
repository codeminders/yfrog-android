/**
 * 
 */
package com.codeminders.yfrog.client.impl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.codeminders.yfrog.client.YFrog;
import com.codeminders.yfrog.client.request.FileUploadRequest;
import com.codeminders.yfrog.client.request.InputStreamUploadRequest;
import com.codeminders.yfrog.client.request.UploadRequest;
import com.codeminders.yfrog.client.request.UrlUploadRequest;
import com.codeminders.yfrog.client.response.ResponseFactory;
import com.codeminders.yfrog.client.response.UploadResponse;
import com.codeminders.yfrog.client.response.UploadResponseFormatException;

/**
 * @author idemydenko
 *
 */
public class YFrogImpl extends YFrog {

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.client.conn.conn.YFrog#upload(com.codeminders.yfrog.client.conn.conn.request.UploadRequest)
	 */
	@Override
	public UploadResponse upload(UploadRequest request) throws IOException, UploadResponseFormatException {	
		return execute(BASE + ACTION_UPLOAD, request);
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.client.conn.conn.YFrog#uploadAndPost(com.codeminders.yfrog.client.conn.conn.request.UploadRequest)
	 */
	@Override
	public UploadResponse uploadAndPost(UploadRequest request) throws IOException, UploadResponseFormatException {
		return execute(BASE + ACTION_UPLOAD_AND_POST, request);
	}

	private UploadResponse execute(String action, UploadRequest request) throws IOException, UploadResponseFormatException {
		checkParams(request);
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(action);
		MultipartEntity entity = buildRequest(request);
		post.setEntity(entity);
		HttpResponse res = client.execute(post);
		
		return ResponseFactory.newResponseFactory().buildResponse(res.getEntity().getContent());
	}
	
	private MultipartEntity buildRequest(UploadRequest request) throws IOException {
		MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		multipart.addPart(UploadRequest.FIELD_USERNAME, new StringBody(request.getUsername()));
		multipart.addPart(UploadRequest.FIELD_PASSWORD, new StringBody(request.getPassword()));
		multipart.addPart(UploadRequest.FIELD_KEY, new StringBody(request.getKey()));
		multipart.addPart(UploadRequest.FIELD_TAGS, new StringBody(request.getTags()));
		multipart.addPart(UploadRequest.FIELD_PUBLIC, new StringBody(request.getPublicAsString()));
		
		if(request instanceof UrlUploadRequest) {
			multipart.addPart(UploadRequest.FIELD_URL, new StringBody(((UrlUploadRequest) request).getUrl()));
		} else if(request instanceof FileUploadRequest) {
			multipart.addPart(UploadRequest.FIELD_MEDIA, new FileBody(((FileUploadRequest) request).getFile()));
		} else {
			InputStreamUploadRequest isur = (InputStreamUploadRequest) request;
			multipart.addPart(UploadRequest.FIELD_MEDIA, new MeasurableInputStreamBody(isur.getInputStream(), isur.getFilename()));
		}
		
		return multipart;
	}
}
