/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import android.app.ListActivity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.codeminders.yfrog.android.R;

/**
 * @author idemydenko
 *
 */
public class MoreActivity extends ListActivity {
	public static final String TAG = "more";
	
	private static final int ITEM_MY_TWEETS = 0;
	private static final int ITEM_FOLLOWERS = 1;
	private static final int ITEM_FOLLOWING = 2;
	private static final int ITEM_SEARCH =3;
	private static final int ITEM_SETTINGS =4;
	private static final int ITEM_ABOUT =5;
	
	private static final int[] ICONS = new int[] {
		R.drawable.my_tweets,
		R.drawable.followers,
		R.drawable.following,
		R.drawable.search,
		R.drawable.settings,
		R.drawable.about
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setListAdapter(new EfficientAdapter(this, getResources().getStringArray(R.array.tab_more_items),
				ICONS));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = null;
		switch (position) {
		case ITEM_MY_TWEETS:
			intent = new Intent(this, MyTweetsActivity.class);
			startActivity(intent);
			break;

		case ITEM_FOLLOWERS:
			intent = new Intent(this, FollowersActivity.class);
			startActivity(intent);
			break;

		case ITEM_FOLLOWING:
			intent = new Intent(this, FollowingActivity.class);
			startActivity(intent);			
			break;
		
		case ITEM_SEARCH:
			intent = new Intent(this, SearchActivity.class);
			startActivity(intent);			
			break;
		case ITEM_SETTINGS:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case ITEM_ABOUT:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		}
	}
	
    private static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private String[] items;
        private int[] itemIcons;

        public EfficientAdapter(Context context, String[] itms, int[] itmIcons) {
            mInflater = LayoutInflater.from(context);
            items = itms;
            itemIcons = itmIcons;
        }

        public int getCount() {
            return items.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.more_item, null);

                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.mi_text);
                holder.icon = (ImageView) convertView.findViewById(R.id.mi_image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(items[position]);
            holder.icon.setImageResource(itemIcons[position]);

            return convertView;
        }

        static class ViewHolder {
            TextView text;
            ImageView icon;
        }
    }

}
