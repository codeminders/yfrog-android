/**
 * 
 */
package com.codeminders.yfrog.android.view.adapter;

import java.util.*;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.widget.*;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.UnsentMessage;
import com.codeminders.yfrog.android.util.*;

/**
 * @author idemydenko
 *
 */
public class UnsentMessageAdapter<T extends UnsentMessage> extends ArrayAdapter<UnsentMessage> {
	private HashMap<Long, CharSequence> spannableCache = new HashMap<Long, CharSequence>();
	private LayoutInflater inflater = null;
	
	public UnsentMessageAdapter(Context context, List<UnsentMessage> objects) {
		super(context, 0, objects);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		UnsentMessage um = getItem(position);
		view = inflater.inflate(R.layout.unsent_message, parent, false);

		setText(view, um);
		if (um.getType() == UnsentMessage.TYPE_DIRECT_MESSAGE) {
			TextView textView = (TextView) view.findViewById(R.id.um_receiver);
			textView.setText(um.getTo());
		} else {
			View to = (View) view.findViewById(R.id.um_to);
			to.setVisibility(View.GONE);
		}
		
		
		return view;
	}

	private void setText(final View view, final UnsentMessage message) {
		final TextView textView = (TextView) view.findViewById(R.id.um_text);
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
