/**
 * 
 */
package com.codeminders.yfrog2.android.view.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.model.TwitterStatus;
import com.codeminders.yfrog2.android.util.StringUtils;
import com.codeminders.yfrog2.android.util.YFrogUtils;
import com.codeminders.yfrog2.android.util.image.cache.ImageCache;

/**
 * @author idemydenko
 *
 */
public class TwitterStatusAdapter<T extends TwitterStatus> extends ArrayAdapter<TwitterStatus> {
	private HashMap<Long, CharSequence> spannableCache = new HashMap<Long, CharSequence>();
	private LayoutInflater inflater = null;
	
	public TwitterStatusAdapter(Context context, List<TwitterStatus> objects) {
		super(context, 0, objects);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		TwitterStatus ts = getItem(position);
		view = inflater.inflate(R.layout.twitter_status, parent, false);
		
		TextView textView = (TextView) view.findViewById(R.id.ts_created_at);
		textView.setText(StringUtils.formatDate(view.getResources(), ts.getCreatedAt()));
		
		textView = (TextView) view.findViewById(R.id.ts_username);
		textView.setText(ts.getUser().getScreenUsername());
		
		setText(view, ts);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ts_user_icon);
		ImageCache.getInstance().putImage(ts.getUser().getProfileImageURL(), imageView);
		
		return view;
	}
	
	private void setText(final View view, final TwitterStatus status) {
		final TextView textView = (TextView) view.findViewById(R.id.ts_text);
		final String text = status.getText();
		
		if (YFrogUtils.hasYFrogContent(text)) {
			if (isCached(status.getId())) {
				textView.setText(get(status.getId()));
			} else {
				textView.setText(StringUtils.EMPTY_STRING);
				new Thread(new Runnable() {
					@Override
					public void run() {
						final CharSequence spannableText = StringUtils.parseURLs(text, view.getContext());
						
						textView.post(new Runnable() {
							public void run() {
								textView.setText(spannableText);
								put(status.getId(), spannableText);
							}
						});
					}
				}).start();

			}
		} else {
			textView.setText(StringUtils.parseURLs(status.getText(), view.getContext()));
		}
	}
	
	private boolean isCached(Long statusId) {
		synchronized (spannableCache) {
			return spannableCache.containsKey(statusId);
		}
	}
	
	private CharSequence get(Long statusId) {
		synchronized (spannableCache) {
			return spannableCache.get(statusId);
		}		
	}
	
	private void put(Long statusId, CharSequence spannable) {
		synchronized (spannableCache) {
			spannableCache.put(statusId, spannable);
		}		
	}
}
