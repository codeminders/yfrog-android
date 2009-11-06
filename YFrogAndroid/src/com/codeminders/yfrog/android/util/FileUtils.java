package com.codeminders.yfrog.android.util;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtils {
	private static final String FILE_URI_MIME = "file://";
	
	private FileUtils() {
		
	}
	
	public static boolean isFileUri(Uri uri) {
		if (uri == null) {
			return false;
		}
		return uri.toString().startsWith(FILE_URI_MIME);
	}

	public static String getExtension(String uri) {
		if (uri == null) {
			return null;
		}

		int dot = uri.lastIndexOf(".");
		if (dot >= 0) {
			return uri.substring(dot);
		} else {
			// No extension.
			return "";
		}
	}

	public static Uri getUri(File file) {
		if (file != null) {
			return Uri.fromFile(file);
		}
		return null;
	}

	public static Uri getMediaFileUri(Context context, Uri contentUri) {
		if (contentUri == null) {
			return null;
		}

		if (isFileUri(contentUri)) {
			return contentUri;
		}
		
		String[] projection = { MediaStore.MediaColumns.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
		String path = null;
		try {
			if (cursor != null && cursor.moveToFirst()) {
				path = cursor.getString(0);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return Uri.parse(FILE_URI_MIME + path);
	}

	public static File getFile(Uri uri) {
		if (uri != null) {
			String filepath = uri.getPath();
			if (filepath != null) {
				return new File(filepath);
			}
		}
		return null;
	}

	public static File getPathWithoutFilename(File file) {
		if (file != null) {
			if (file.isDirectory()) {
				// no file to be split off. Return everything
				return file;
			} else {
				String filename = file.getName();
				String filepath = file.getAbsolutePath();

				// Construct path without file name.
				String pathwithoutname = filepath.substring(0, filepath
						.length()
						- filename.length());
				if (pathwithoutname.endsWith("/")) {
					pathwithoutname = pathwithoutname.substring(0,
							pathwithoutname.length() - 1);
				}
				return new File(pathwithoutname);
			}
		}
		return null;
	}

	public static File getFile(String curdir, String file) {
		String separator = "/";
		if (curdir.endsWith("/")) {
			separator = "";
		}
		File clickedFile = new File(curdir + separator + file);
		return clickedFile;
	}
}