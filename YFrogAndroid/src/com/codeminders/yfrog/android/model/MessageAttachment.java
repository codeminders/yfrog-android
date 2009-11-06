/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.io.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;

import com.codeminders.yfrog.android.util.FileUtils;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.client.request.FileUploadRequest;
import com.codeminders.yfrog.client.request.InputStreamUploadRequest;
import com.codeminders.yfrog.client.request.UploadRequest;

/**
 * @author idemydenko
 *
 */
public class MessageAttachment {
	private Context context;
	private Bitmap bitmap;
	private Uri uri;
	
	public MessageAttachment(Context ctx, Bitmap attachment) {
		bitmap = attachment;
		context = ctx;
	}
	
	public MessageAttachment(Context ctx, Uri attachment) {
		uri = attachment;
		context = ctx;
	}

	public MessageAttachment(Context ctx, String attachment) {
		uri = Uri.parse(attachment);
		context = ctx;
	}

	private InputStream getInputStream() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, out);
		return new ByteArrayInputStream(out.toByteArray());
	}

	private File getFile() {
		return new File(uri.getPath());
	}

	public UploadRequest toUploadRequest() {
		UploadRequest request = null;
		if (uri == null) {
			request = createBitmapISUploadRequest();
		} else if (FileUtils.isFileUri(uri)){
			request = createFileUploadRequest();
		} else {
			request = createISUploadRequest();
		}
		request.setPublic(true);
		return request;
	}
	
	private UploadRequest createFileUploadRequest() {
		FileUploadRequest request = new FileUploadRequest();
		request.setFile(getFile());
		return request;
	}
	
	private UploadRequest createBitmapISUploadRequest() {
		InputStreamUploadRequest request = new InputStreamUploadRequest();
		request.setFilename(StringUtils.createFilename());
		try {
			request.setInputStream(getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	private UploadRequest createISUploadRequest() {
		InputStreamUploadRequest request = new InputStreamUploadRequest();
		request.setFilename(StringUtils.createFilename());
		try {
			request.setInputStream(context.getContentResolver().openInputStream(uri));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}
}
