/**
 * 
 */
package com.codeminders.yfrog.android.view.main.adapter;

import java.util.List;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.TwitterDirectMessage;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.util.image.cache.ImageCache;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
		textView.setText(StringUtils.formatDate(view.getResources(), ts.getCreatedAt()));
		
		textView = (TextView) view.findViewById(R.id.tdm_username);
		textView.setText(ts.getSender().getScreenUsername());
		
		textView = (TextView) view.findViewById(R.id.tdm_text);
		textView.setText(ts.getText());
		
		ImageView imageView = (ImageView) view.findViewById(R.id.tdm_user_icon);
		ImageCache.getInstance().putImage(ts.getSender().getProfileImageURL(), imageView);
		
		return view;
	}
}
