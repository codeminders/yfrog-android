/**
 * 
 */
package com.codeminders.yfrog.android.util;

import java.net.URL;

import com.codeminders.yfrog.android.view.media.ImageViewActivity;

import android.content.*;
import android.graphics.*;
import android.text.*;
import android.text.style.*;
import android.view.View;

/**
 * @author idemydenko
 *
 */
public final class YFrogUtils {
	private static final String YFROG_HOST_NAME = "yfrog.com";
	private static final String YFROG_SMALL_SUFIX = ":small";
	private static final String YFROG_FULL_SUFIX = ":iphone";

	private YFrogUtils() {
		
	}
	
	public static boolean hasYFrogContent(String url) {
		return url != null && url.indexOf(YFROG_HOST_NAME) != -1;
	}

	private static String getSmallImageUrl(String url) {
		return new StringBuilder(url).append(YFROG_SMALL_SUFIX).toString();
	}
	
	private static String getFullImageUrl(String url) {
		return new StringBuilder(url).append(YFROG_FULL_SUFIX).toString();
	}

	public static void buildYFrogURL(final Context context, final Spannable spannable, final String url, final int start, final int end) {
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
			spannable.setSpan(new URLSpan(getFullImageUrl(url)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			e.printStackTrace();
		}

	}
}
