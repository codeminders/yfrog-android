/**
 * 
 */
package com.codeminders.yfrog.android.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.model.UnsentMessage;

/**
 * @author idemydenko
 *
 */
public class UnsentMessageAdapter<T extends UnsentMessage> extends ArrayAdapter<UnsentMessage> {
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

		TextView textView = (TextView) view.findViewById(R.id.um_text);
		textView.setText(um.getText());

		if (um.getType() == UnsentMessage.TYPE_DIRECT_MESSAGE) {
			textView = (TextView) view.findViewById(R.id.um_receiver);
			textView.setText(um.getTo());
		} else {
			View to = (View) view.findViewById(R.id.um_to);
			to.setVisibility(View.GONE);
		}
		
		
		return view;
	}

}
