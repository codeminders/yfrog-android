/**
 * 
 */
package com.codeminders.yfrog.android.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;

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
		textView.setText(StringUtils.formatDate(view.getResources(), ts.getCreatedAt()));
		
		textView = (TextView) view.findViewById(R.id.ts_username);
		textView.setText(ts.getUser().getScreenUsername());
		
		textView = (TextView) view.findViewById(R.id.ts_text);
		textView.setText(ts.getText());
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ts_user_icon);
		ImageCache.getInstance().putImage(ts.getUser().getProfileImageURL(), imageView);
		
		return view;
	}
}