/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.io.*;

import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore;

import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.client.request.*;

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
		if (uri != null) {
			return context.getContentResolver().openInputStream(uri);
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 100, out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
	
	public UploadRequest toUploadRequest() {
		InputStreamUploadRequest request = new InputStreamUploadRequest();
		request.setFilename(StringUtils.createFilename());
		request.setPublic(true);
		try {
			request.setInputStream(getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}
	
	public String toUrl() {
		if (uri != null) {
			return uri.toString();
		} else {
			return saveBitmapAndGetUrl();
		}
	}
	
	private String saveBitmapAndGetUrl() {	
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, StringUtils.createFilename());
		values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
		Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


		OutputStream out = null;
		try{
			out = context.getContentResolver().openOutputStream(uri);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch(IOException e){
			return StringUtils.EMPTY_STRING;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					
				}
			}
		}
		
		return uri.toString();
	}

}
