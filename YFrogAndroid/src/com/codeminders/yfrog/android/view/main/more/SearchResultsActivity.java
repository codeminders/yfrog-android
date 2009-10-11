/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterQueryResult;
import com.codeminders.yfrog.android.model.TwitterSavedSearch;
import com.codeminders.yfrog.android.model.TwitterSearchResult;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.view.main.adapter.TwitterSearchResultAdapter;
import com.codeminders.yfrog.android.view.message.StatusDetailsActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author idemydenko
 *
 */
public class SearchResultsActivity extends Activity implements OnClickListener {
	public static final String KEY_QUERY = "query";
	public static final String KEY_SAVED = "isSaved";
	public static final String KEY_QUERY_ID = "queryId";
	
	private TwitterService twitterService;
	
	private TwitterQueryResult queryResult;
	private String query;
	private boolean isSaved;
	private int queryId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterService = ServiceFactory.getTwitterService();
		
		Bundle extra = getIntent().getExtras();
		
		if (extra != null) {
			query = extra.getString(KEY_QUERY);
			isSaved = extra.getBoolean(KEY_SAVED);
			
			if (isSaved) {
				queryId = extra.getInt(KEY_QUERY_ID);
			}
		}
		
		setContentView(R.layout.twitter_search_results);
		Button button = (Button) findViewById(R.id.sr_save);
		button.setText(isSaved ? R.string.delete : R.string.save);
		button.setOnClickListener(this);
		
		createSearchResultList();
		
		
	}
	
	private void createSearchResultList() {
		
		try {
			queryResult = twitterService.search(query);
		} catch (YFrogTwitterException e) {
			// TODO: handle exception
		}
		
		ListView listView = (ListView) findViewById(R.id.sr_search_result_list);
		listView.setAdapter(new TwitterSearchResultAdapter<TwitterSearchResult>(this, queryResult.getResults(), query));
		listView.setOnItemClickListener(mOnClickListener);
	}
	
	@Override
	public void onClick(View v) {
		if (isSaved) {
			try {
				twitterService.deleteSavedSearch(queryId);
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			
		} else {
			try {
				twitterService.addSavedSearch(query);
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
		}
		finish();
	}
	
	private TwitterStatus getSelected(int position) {
		if (position > -1) {
			TwitterSearchResult sr = queryResult.getResults().get(position);
			TwitterStatus result = null;
			
			try {
				result = twitterService.getStatus(sr.getId());
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			
			return result;
		}
		return null;
	}
	
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			Intent intent = new Intent(SearchResultsActivity.this, StatusDetailsActivity.class);
			TwitterStatus status = getSelected(position);

			intent.putExtra(StatusDetailsActivity.KEY_STATUS, status);

			startActivity(intent);

		}
	};

}
