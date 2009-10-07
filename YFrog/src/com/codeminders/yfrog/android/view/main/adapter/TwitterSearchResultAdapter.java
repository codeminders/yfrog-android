/**
 * 
 */
package com.codeminders.yfrog.android.view.main.adapter;

import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterSearchResult;
import com.codeminders.yfrog.android.model.TwitterStatus;

/**
 * @author idemydenko
 *
 */
public class TwitterSearchResultAdapter<T extends TwitterSearchResult> extends ArrayAdapter<TwitterSearchResult> {
	private LayoutInflater inflater = null;
	
	public TwitterSearchResultAdapter(Context context, List<TwitterSearchResult> objects) {
		super(context, 0, objects);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		TwitterSearchResult ts = getItem(position);
		view = inflater.inflate(R.layout.twitter_search_result, parent, false);
		
		TextView textView = (TextView) view.findViewById(R.id.tsr_created_at);
		textView.setText(DateFormat.format("MMM dd, yyyy hh:mm", ts.getCreatedAt()));
		
		textView = (TextView) view.findViewById(R.id.tsr_username);
		textView.setText(ts.getFromUser());
		
		textView = (TextView) view.findViewById(R.id.tsr_text);
		textView.setText(ts.getText());
		
		return view;
	}
}
