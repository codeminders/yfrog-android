/**
 * 
 */
package com.codeminders.yfrog.android.view.main.adapter;

import java.util.List;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterUser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author idemydenko
 *
 */
public class TwitterUserAdapter<T extends TwitterUser> extends ArrayAdapter<TwitterUser> {
	private LayoutInflater inflater = null;
	
	public TwitterUserAdapter(Context context, List<TwitterUser> objects) {
		super(context, 0, objects);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		TwitterUser u = getItem(position);
		view = inflater.inflate(R.layout.twitter_user, parent, false);
		
		TextView textView = (TextView) view.findViewById(R.id.tu_user_id);
		textView.setText(u.getUsername());
		
		textView = (TextView) view.findViewById(R.id.tu_name);
		textView.setText(u.getName());
		
		return view;
	}
}
