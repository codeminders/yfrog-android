/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterQueryResult;
import com.codeminders.yfrog.android.model.TwitterSavedSearch;
import com.codeminders.yfrog.android.model.TwitterSearchResult;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.view.adapter.TwitterSearchResultAdapter;
import com.codeminders.yfrog.android.view.message.StatusDetailsActivity;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;

/**
 * @author idemydenko
 * 
 */
public class SearchResultsActivity extends Activity implements OnClickListener {
	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_COUNT = 1500;

	public static final String KEY_QUERY = "query";
	public static final String KEY_SEARCHES = "searches";

	private TwitterService twitterService;

	private TwitterQueryResult queryResult;
	private String query;
	private boolean isSaved;
	private int page = 1;
	private Button saveButton;
	private AutoCompleteTextView input;
	private ArrayList<TwitterSavedSearch> searches;
	private List<String> searchesQueries;

	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		twitterService = ServiceFactory.getTwitterService();

		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			query = extra.getString(KEY_QUERY);
			searches = (ArrayList<TwitterSavedSearch>) extra
					.getSerializable(KEY_SEARCHES);
			searchesQueries = getStrings();
			isSaved = isQuerySaved(query);

		}

		createList(true);

	}

	private void createList(boolean twitterUpdate) {

		if (twitterUpdate) {
			page = 1;
			try {
				queryResult = twitterService.search(query, page,
						DEFAULT_PAGE_SIZE);
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
		}

		setContentView(R.layout.twitter_search_results);

		ListView listView = (ListView) findViewById(R.id.sr_search_result_list);

		int selected = listView.getSelectedItemPosition();
		listView
				.setAdapter(new TwitterSearchResultAdapter<TwitterSearchResult>(
						this, queryResult.getResults(), query));

		if (selected > -1) {
			listView.setSelection(selected);
		}

		listView.setOnItemClickListener(mOnClickListener);

		saveButton = (Button) findViewById(R.id.sr_save_button);
		saveButton.setText(isSaved ? R.string.delete : R.string.save);
		saveButton.setOnClickListener(this);

		Button button = (Button) findViewById(R.id.sr_search_button);
		button.setOnClickListener(this);

		input = (AutoCompleteTextView) findViewById(R.id.sr_search_input);
		input.setText(query);
		input.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, searchesQueries));
		input.addTextChangedListener(inputTextWatcher);
	}

	private List<String> getStrings() {
		List<String> list = new ArrayList<String>(searches.size());

		for (TwitterSavedSearch tss : searches) {
			list.add(tss.getName());
		}

		return list;
	}

	private TwitterSavedSearch findSearchByQuery() {
		if (!isSaved) {
			return null;
		}

		return searches.get(searchesQueries.indexOf(query.trim()));
	}

	private void appendList() {
		try {
			TwitterQueryResult appended = twitterService.search(query, page,
					DEFAULT_PAGE_SIZE);
			queryResult.getResults().addAll(appended.getResults());
		} catch (YFrogTwitterException e) {
			e.printStackTrace();
		}

		createList(false);
	}

	private String getQueryFromInput() {
		return input.getText().toString().trim();
	}

	private boolean isQuerySaved(String query) {
		return searchesQueries.contains(query.trim());
	}

	@Override
	public void onClick(View v) {
		String q = getQueryFromInput();

		if (StringUtils.isEmpty(q)) {
			return;
		}

		query = q;

		switch (v.getId()) {
		case R.id.sr_search_button:

			createList(true);
			break;

		case R.id.sr_save_button:

			try {
				if (isSaved) {
					TwitterSavedSearch savedSearch = findSearchByQuery();
					twitterService.deleteSavedSearch(savedSearch.getId());
				} else {
					twitterService.addSavedSearch(query);
				}
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			finish();

			break;
		}
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

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_results, menu);
		getMenuInflater().inflate(R.menu.common_add_tweet, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
			return true;
		case R.id.more_serarch_results:
			if (!isNoMoreItems()) {
				page++;
				appendList();
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.more_serarch_results);
		item.setEnabled(!isNoMoreItems());
		return super.onPrepareOptionsMenu(menu);
	}

	private boolean isNoMoreItems() {
		return queryResult.getResults().size() < page * DEFAULT_PAGE_SIZE
				&& page * DEFAULT_PAGE_SIZE > MAX_COUNT;
	}

	@SuppressWarnings("unchecked")
	private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			Intent intent = new Intent(SearchResultsActivity.this,
					StatusDetailsActivity.class);
			TwitterStatus status = getSelected(position);
			intent.putExtra(StatusDetailsActivity.KEY_STATUSES,
					(Serializable) Arrays.asList(status));
			intent.putExtra(StatusDetailsActivity.KEY_STATUS_POS, 0);

			startActivity(intent);

		}
	};

	private TextWatcher inputTextWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			isSaved = isQuerySaved(s.toString());
			saveButton.setText(isSaved ? R.string.delete : R.string.save);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}
	};
}
