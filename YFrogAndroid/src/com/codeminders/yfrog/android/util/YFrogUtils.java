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
	private static final String YFROG_IMAGE_HOST_NAME = "yfrog.com";
	private static final String YFROG_VIDEO_HOST_NAME = "yfrog.us";
	private static final String YFROG_THUMB_IMAGE_SUFIX = ".th.jpg";
	private static final String YFROG_FULL_IMAGE_SUFIX = ":iphone";

	private YFrogUtils() {
		
	}
	
	public static boolean hasYFrogImageContent(String url) {
		return url != null && url.indexOf(YFROG_IMAGE_HOST_NAME) != -1;
	}

	public static boolean hasYFrogVideoContent(String url) {
		return url != null && url.indexOf(YFROG_VIDEO_HOST_NAME) != -1;
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

	public static void buildYFrogImageURL(final Context context, final Spannable spannable, final String url, final int start, final int end) {
		try {
			Bitmap b = BitmapFactory.decodeStream(new URL(getThumbImageUrl(url)).openStream());
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
			spannable.setSpan(new URLSpan(getFullImageUrl(url)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			e.printStackTrace();
		}

	}
	
	public static void buildYFrogVideoURL(final Context context, final Spannable spannable, final String url, final int start, final int end) {
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
//							Uri uri = Uri.parse("android.resource://com.androidbook.samplevideo/" + R.raw.fnn);
							intent.setData(uri);
							intent.setType("video/mp4");
							context.startActivity(intent);
						}
					}
				, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			spannable.setSpan(new URLSpan(getOptVideoUrl(url)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			e.printStackTrace();
		}

	}
	
}
