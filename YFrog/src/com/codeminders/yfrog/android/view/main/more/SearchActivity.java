/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import java.util.ArrayList;
import java.util.List;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterSavedSearch;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.view.main.adapter.TwitterStatusAdapter;

import android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

/**
 * @author idemydenko
 *
 */
public class SearchActivity extends ListActivity {
	
	private TwitterService twitterService;
	private ArrayList<TwitterSavedSearch> searches;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterService = ServiceFactory.getTwitterService();
		
		createSearchesList();
	}
	
	private void createSearchesList() {
		try {
			searches = twitterService.getSavedSearches();
		} catch (YFrogTwitterException e) {
			showDialog(AlertUtils.ALERT_TWITTER_ERROR);
		}

//		setListAdapter(new TwitterStatusAdapter<TwitterStatus>(this, statuses));
		setListAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item_1, getStrings()));
		getListView().setTextFilterEnabled(true);
		registerForContextMenu(getListView());

	}
	
	private List<String> getStrings() {
		List<String> list = new ArrayList<String>(searches.size());
		
		for (TwitterSavedSearch tss : searches) {
			list.add(tss.getName());
		}
		
		list.add("last");
		return list;
	}
}
