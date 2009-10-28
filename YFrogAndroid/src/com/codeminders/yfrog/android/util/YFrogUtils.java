/**
 * 
 */
package com.codeminders.yfrog.android.util;

import java.net.URL;

import android.content.*;
import android.graphics.*;
import android.net.Uri;
import android.text.*;
import android.text.style.*;
import android.view.View;

import com.codeminders.yfrog.android.view.media.ImageViewActivity;

/**
 * @author idemydenko
 *
 */
public final class YFrogUtils {
	private static final String YFROG_IMAGE_HOST_NAME = "yfrog.";
	private static final String YFROG_THUMB_IMAGE_SUFIX = ".th.jpg";
	private static final String YFROG_FULL_IMAGE_SUFIX = ":iphone";
	private static final String YFROG_SMALL_IMAGE_SUFIX = ":small";
	
	// According to http://code.google.com/p/imageshackapi/wiki/YFROGurls and http://developer.android.com/guide/appendix/media-formats.html
	private static final String SUPPORTED_IMAGE_TYPE_SUFFIXES = "jpbg";
	private static final String SUPPORTED_VIDEO_TYPE_SUFFIXES = "z"; 

	private YFrogUtils() {
		
	}
	
	public static boolean hasYFrogContent(String url) {
		return url != null && url.indexOf(YFROG_IMAGE_HOST_NAME) != -1;
	}

	public static boolean hasImageContent(String url) {
		String suffix = getSuffix(url);
		return SUPPORTED_IMAGE_TYPE_SUFFIXES.indexOf(suffix) != -1;
	}

	public static boolean hasVideoContent(String url) {
		String suffix = getSuffix(url);
		return SUPPORTED_VIDEO_TYPE_SUFFIXES.indexOf(suffix) != -1;		
	}

	private static String getSuffix(String url) {
		String result = "";
		String temp = url.trim();
		result = temp.substring(temp.length());
		return result;
	}

	private static String getSmallImageUrl(String url) {
		return new StringBuilder(url).append(YFROG_SMALL_IMAGE_SUFIX).toString();
	}

	private static String getThumbImageUrl(String url) {
		return new StringBuilder(url).append(YFROG_THUMB_IMAGE_SUFIX).toString();
	}
	
	private static String getFullImageUrl(String url) {
		return new StringBuilder(url).append(YFROG_FULL_IMAGE_SUFIX).toString();
	}

	private static String getOptVideoUrl(String url) {
		return new StringBuilder(url).append(YFROG_FULL_IMAGE_SUFIX).toString();
	}

	public static void buildYFrogContentURL(final Context context, final Spannable spannable, final String url, final int start, final int end) {
		if (hasImageContent(url)) {
			buildYFrogImageURL(context, spannable, url, start, end);
		} else if (hasVideoContent(url)) {
			buildYFrogVideoURL(context, spannable, url, start, end);
		} else {
			buildYFrogOtherURL(context, spannable, url, start, end);
		}
	}
	
	private static void buildYFrogImageURL(final Context context, final Spannable spannable, final String url, final int start, final int end) {
		try {
			Bitmap b = BitmapFactory.decodeStream(new URL(getSmallImageUrl(url)).openStream());
			spannable.setSpan(new ImageSpan(b), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannable.setSpan(
					new URLSpan(getFullImageUrl(url)) {
						@Override
						public void onClick(View widget) {
							Intent intent = new Intent(context, ImageViewActivity.class);
							intent.putExtra(ImageViewActivity.KEY_IMAGE_URL, getFullImageUrl(url));
							context.startActivity(intent);
						}
					}
				, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			buildYFrogOtherURL(context, spannable, getFullImageUrl(url), start, end);
			e.printStackTrace();
		}

	}
	
	private static void buildYFrogVideoURL(final Context context, final Spannable spannable, final String url, final int start, final int end) {
		try {
			
			Bitmap b = BitmapFactory.decodeStream(new URL(getThumbImageUrl(url)).openStream());
			spannable.setSpan(new ImageSpan(b), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannable.setSpan(
					new URLSpan(getOptVideoUrl(url)) {
						@Override
						public void onClick(View widget) {
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							Uri uri = Uri.parse(getOptVideoUrl(url));
							intent.setData(uri);
							intent.setType("video/mp4");
							context.startActivity(intent);
						}
					}
				, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			buildYFrogOtherURL(context, spannable, getOptVideoUrl(url), start, end);
			e.printStackTrace();
		}

	}
	
	private static void buildYFrogOtherURL(final Context context, final Spannable spannable, final String url, final int start, final int end) {
		spannable.setSpan(new URLSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
}
