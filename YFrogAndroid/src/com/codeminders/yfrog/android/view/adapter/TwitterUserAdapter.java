/**
 * 
 */
package com.codeminders.yfrog.android.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;

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
		
		TextView textView = (TextView) view.findViewById(R.id.tu_username);
		textView.setText(u.getScreenUsername());
		
		textView = (TextView) view.findViewById(R.id.tu_fullname);
		textView.setText(u.getFullname());
		
		ImageView imageView = (ImageView) view.findViewById(R.id.tu_user_icon);
		ImageCache.getInstance().putImage(u.getProfileImageURL(), imageView);
		
		return view;
	}
}