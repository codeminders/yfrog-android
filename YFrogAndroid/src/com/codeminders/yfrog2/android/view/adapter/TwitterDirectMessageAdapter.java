/**
 * 
 */
package com.codeminders.yfrog2.android.view.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.model.TwitterDirectMessage;
import com.codeminders.yfrog2.android.util.StringUtils;
import com.codeminders.yfrog2.android.util.YFrogUtils;
import com.codeminders.yfrog2.android.util.image.cache.ImageCache;

/**
 * @author idemydenko
 *
 */
public class TwitterDirectMessageAdapter<T extends TwitterDirectMessage> extends ArrayAdapter<TwitterDirectMessage>{
	private HashMap<Integer, CharSequence> spannableCache = new HashMap<Integer, CharSequence>();
	private LayoutInflater inflater = null;
	
	public TwitterDirectMessageAdapter(Context context, List<TwitterDirectMessage> objects) {
		super(context, 0, objects);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		TwitterDirectMessage ts = getItem(position);
		view = inflater.inflate(R.layout.twitter_dir_message, parent, false);
		
		TextView textView = (TextView) view.findViewById(R.id.tdm_created_at);
		textView.setText(StringUtils.formatDate(view.getResources(), ts.getCreatedAt()));
		
		textView = (TextView) view.findViewById(R.id.tdm_username);
		textView.setText(ts.getSender().getScreenUsername());
		
		setText(view, ts);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.tdm_user_icon);
		ImageCache.getInstance().putImage(ts.getSender().getProfileImageURL(), imageView);
		
		return view;
	}
	
	private void setText(final View view, final TwitterDirectMessage message) {
		final TextView textView = (TextView) view.findViewById(R.id.tdm_text);
		final String text = message.getText();
		
		if (YFrogUtils.hasYFrogContent(text)) {
			if (isCached(message.getId())) {
				textView.setText(get(message.getId()));
			} else {
				textView.setText(StringUtils.EMPTY_STRING);
				new Thread(new Runnable() {
					@Override
					public void run() {
						final CharSequence spannableText = StringUtils.parseURLs(text, view.getContext());
						
						textView.post(new Runnable() {
							public void run() {
								textView.setText(spannableText);
								put(message.getId(), spannableText);
							}
						});
					}
				}).start();

			}
		} else {
			textView.setText(StringUtils.parseURLs(message.getText(), view.getContext()));
		}
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	private boolean isCached(Integer messageId) {
		synchronized (spannableCache) {
			return spannableCache.containsKey(messageId);
		}
	}
	
	private CharSequence get(Integer messageId) {
		synchronized (spannableCache) {
			return spannableCache.get(messageId);
		}		
	}
	
	private void put(Integer messageId, CharSequence spannable) {
		synchronized (spannableCache) {
			spannableCache.put(messageId, spannable);
		}		
	}

}
