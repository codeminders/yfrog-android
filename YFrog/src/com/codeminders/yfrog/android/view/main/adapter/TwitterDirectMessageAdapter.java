/**
 * 
 */
package com.codeminders.yfrog.android.view.main.adapter;

import java.util.List;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterDirectMessage;
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
public class TwitterDirectMessageAdapter<T extends TwitterDirectMessage> extends ArrayAdapter<TwitterDirectMessage>{
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
		textView.setText(DateFormat.format("MMM dd, yyyy hh:mm", ts.getCreatedAt()));
		
		textView = (TextView) view.findViewById(R.id.tdm_user_id);
		textView.setText(ts.getSender().getUsername());
		
		textView = (TextView) view.findViewById(R.id.tdm_text);
		textView.setText(ts.getText());
		
		return view;
	}
}
