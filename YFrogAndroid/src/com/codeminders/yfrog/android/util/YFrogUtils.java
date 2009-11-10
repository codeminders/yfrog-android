/**
 * 
 */
package com.codeminders.yfrog.android.util;

import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;

import com.codeminders.yfrog.android.R;
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
	private static final String YFROG_VIDEO_SUFIX = ".android";
	
	// According to http://code.google.com/p/imageshackapi/wiki/YFROGurls and http://developer.android.com/guide/appendix/media-formats.html
	private static final String SUPPORTED_IMAGE_TYPE_SUFFIXES = "jpbg";
	private static final String SUPPORTED_VIDEO_TYPE_SUFFIXES = "z";
	private static final String NOT_THUMBNAILABLE_TYPE_SUFFIXES = "sdx";

	private static Bitmap default_yfrog_thumbnail = null;

	private YFrogUtils() {
		
	}
	
	private static Bitmap getDefaultThumbnail(Context context) {
		if (default_yfrog_thumbnail == null) {
			default_yfrog_thumbnail = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_yfrog_thumbnail);
		}
		return default_yfrog_thumbnail;
	}

	private static Bitmap getBitmap(Context context, String url) throws IOException {
		return BitmapFactory.decodeStream(new URL(url).openStream());
	}
	
	private static Bitmap getThumbnail(Context context, String url) throws IOException {
		return getBitmap(context, getThumbImageUrl(url));
	}

	private static Bitmap getSmallImage(Context context, String url) throws IOException {
		return getBitmap(context, getSmallImageUrl(url));
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

	public static boolean hasNoThumbenailableContent(String url) {
		String suffix = getSuffix(url);
		return NOT_THUMBNAILABLE_TYPE_SUFFIXES.indexOf(suffix) != -1;		
	}
	
	
	private static String getSuffix(String url) {
		String result = "";
		String temp = url.trim();
		result = temp.substring(temp.length() - 1);
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
		return new StringBuilder(url).append(YFROG_VIDEO_SUFIX).toString();
	}

	public static void bindYFrogContent(final Context context, final Spannable spannable, final String url, final int start, final int end) {
		if (hasImageContent(url)) {
			bindImage(context, spannable, url, start, end);
		} else if (hasVideoContent(url)) {
			bindVideo(context, spannable, url, start, end);
		} else if (hasNoThumbenailableContent(url)) {
			bindDefaultThumbnail(context, spannable, url, start, end);
		} else {
			bindThumbnail(context, spannable, url, start, end);
		}
	}
	
	private static void bindImage(final Context context, final Spannable spannable, final String url, final int start, final int end) {
		try {
			Bitmap b = getSmallImage(context, url);
			spannable.setSpan(new ImageSpan(b), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannable.setSpan(createImageURLSpan(context, url),
					start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			bindDefaultThumbnail(context, spannable, url, start, end);
			e.printStackTrace();
		}

	}
	
	private static void bindVideo(final Context context,
			final Spannable spannable, final String url, final int start,
			final int end) {
		try {

			Bitmap b = getThumbnail(context, url);
			spannable.setSpan(new ImageSpan(b), start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannable.setSpan(createVideoURLSapn(context, url),
					start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (Exception e) {
			bindDefaultThumbnail(context, spannable, url, start, end);
			e.printStackTrace();
		}

	}

	private static void bindDefaultThumbnail(final Context context,
			final Spannable spannable, final String url, final int start,
			final int end) {
		Bitmap bitmap = getDefaultThumbnail(context);
		spannable.setSpan(new ImageSpan(bitmap), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new URLSpan(url), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	private static void bindThumbnail(final Context context,
			final Spannable spannable, final String url, final int start,
			final int end) {
		try {
			Bitmap bitmap = getThumbnail(context, url);
		spannable.setSpan(new ImageSpan(bitmap),
				start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(new URLSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} catch (IOException e) {
			bindDefaultThumbnail(context, spannable, url, start, end);
			e.printStackTrace();
		}
	}

	private static URLSpan createImageURLSpan(final Context context, final String url) {
		final String imageUrl = getFullImageUrl(url);
		return new URLSpan(imageUrl) {
			@Override
			public void onClick(View widget) {
				Intent intent = new Intent(context, ImageViewActivity.class);
				intent.putExtra(ImageViewActivity.KEY_IMAGE_URL, getURL());
				context.startActivity(intent);
			}
		};
	}
	
	private static URLSpan createVideoURLSapn(final Context context, final String url) {
		final String videoUrl = getOptVideoUrl(url);
		return new URLSpan(videoUrl) {
			@Override
			public void onClick(View widget) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse(getURL()), "video/*");
				context.startActivity(intent);
			}
		};
	}
	
	
}
