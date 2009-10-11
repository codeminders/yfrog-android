/**
 * 
 */
package com.codeminders.yfrog.android.view.adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterSearchResult;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;

/**
 * @author idemydenko
 *
 */
public class TwitterSearchResultAdapter<T extends TwitterSearchResult> extends ArrayAdapter<TwitterSearchResult> {
	private LayoutInflater inflater = null;
	private String toHighlight;
	
	public TwitterSearchResultAdapter(Context context, List<TwitterSearchResult> objects, String highlighted) {
		super(context, 0, objects);
		
		toHighlight = highlighted; 
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		TwitterSearchResult ts = getItem(position);
		view = inflater.inflate(R.layout.twitter_search_result, parent, false);
		
		TextView textView = (TextView) view.findViewById(R.id.tsr_created_at);
		textView.setText(StringUtils.formatDate(view.getResources(), ts.getCreatedAt()));
		
		textView = (TextView) view.findViewById(R.id.tsr_username);
		textView.setText(ts.getFromUser());
		
		textView = (TextView) view.findViewById(R.id.tsr_text);
		textView.setText(StringUtils.highlightText(ts.getText(), toHighlight));
		
		ImageView imageView = (ImageView) view.findViewById(R.id.tsr_user_icon);
		ImageCache.getInstance().putImage(ts.getProfileImageUrl(), imageView);

		return view;
	}
}
