/**
 * 
 */
package com.codeminders.yfrog.android.view.main.adapter;

import java.util.List;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterStatus;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author idemydenko
 *
 */
public class TwitterStatusAdapter<T extends TwitterStatus> extends ArrayAdapter<TwitterStatus> {
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
		textView.setText(DateFormat.format("MMM dd, yyyy hh:mm", ts.getCreatedAt()));
		
		textView = (TextView) view.findViewById(R.id.ts_user_id);
		textView.setText(ts.getUser().getName());
		
		textView = (TextView) view.findViewById(R.id.ts_text);
		textView.setText(ts.getText());
		
		return view;
	}
}
