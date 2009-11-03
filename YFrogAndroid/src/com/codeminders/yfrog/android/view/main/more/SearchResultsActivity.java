/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import java.io.Serializable;
import java.util.*;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.text.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.controller.service.*;
import com.codeminders.yfrog.android.model.*;
import com.codeminders.yfrog.android.util.*;
import com.codeminders.yfrog.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog.android.view.adapter.TwitterSearchResultAdapter;
import com.codeminders.yfrog.android.view.message.*;

/**
 * @author idemydenko
 * 
 */
public class SearchResultsActivity extends Activity implements OnClickListener {
	private static final String SAVED_STATUSES = "sstatuses";
	private static final String SAVED_PAGE = "spage";
	private static final String SAVED_SELECTED = "sselected";

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
	
	private int selected = -1;

	@Override
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		twitterService = ServiceFactory.getTwitterService();

		boolean restored = restoreState(savedInstanceState);
		
		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			query = extra.getString(KEY_QUERY);
			searches = (ArrayList<TwitterSavedSearch>) extra
					.getSerializable(KEY_SEARCHES);
			searchesQueries = getStrings();
			isSaved = isQuerySaved(query);

		}
		setTitle(StringUtils.formatTitle(twitterService.getLoggedUser().getUsername(), 
				getResources().getString(R.string.sr_title)  + " " + query));
		
		createList(!restored, false);

	}

	protected void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}
	
	private boolean restoreState(Bundle savedState) {
		if (savedState == null) {
			return false;
		}
		
		
		Serializable values = savedState.getSerializable(SAVED_STATUSES);
		if (values == null) {
			return false;
		}
		queryResult = (TwitterQueryResult) values;		
		
		int value = savedState.getInt(SAVED_PAGE);
		if (value < 1) {
			return false;
		}
		page = value;
		
		value = savedState.getInt(SAVED_SELECTED);
		if (value > -1 && value < queryResult.getResults().size()) {
			selected = value;
		}

		return true;
	}
	
	private void saveState(Bundle savedState) {
		if (savedState == null) {
			return;
		}
		
		savedState.putSerializable(SAVED_STATUSES, queryResult);
		savedState.putInt(SAVED_PAGE, page);
		ListView listView = (ListView) findViewById(R.id.sr_search_result_list);
		savedState.putInt(SAVED_SELECTED, listView.getSelectedItemPosition());
	}

	private void createList(boolean twitterUpdate, final boolean append) {

		if (twitterUpdate) {
			if (!append) {
				page = 1;
			}
			
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					if (append) {
						queryResult.getResults().addAll(twitterService.search(query, page,
								DEFAULT_PAGE_SIZE).getResults());
					} else {
						queryResult = twitterService.search(query, page,
								DEFAULT_PAGE_SIZE);
					}					
				}
				
				protected void doAfterUpdate() {
					show();
				}
				
				protected void doAfterError() {
					queryResult.setResults(new ArrayList<TwitterSearchResult>(0));
					show();
				}
			}.update();
		} else {
			show();
		}

	}

	private void show() {
		setContentView(R.layout.twitter_search_results);

		
		ListView listView = (ListView) findViewById(R.id.sr_search_result_list);

		if (selected < 0) {
			selected = listView.getSelectedItemPosition();
		}
		
		if (queryResult.getResults().size() == 0) {
			View v = findViewById(R.id.sr_searches_empty);
			v.setVisibility(View.VISIBLE);
		} else {
			listView.setAdapter(new TwitterSearchResultAdapter<TwitterSearchResult>(
							this, queryResult.getResults(), query));
		}

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

			createList(true, false);
			break;

		case R.id.sr_save_button:
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					if (isSaved) {
						TwitterSavedSearch savedSearch = findSearchByQuery();
						twitterService.deleteSavedSearch(savedSearch.getId());
					} else {
						twitterService.addSavedSearch(query);
					}					
				}
				
				protected void doAfterUpdate() {
					finish();
				}
			}.update();
			break;
		}
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
				createList(true, true);
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

	private void showDetails(int position) {
		if (position < 0) {
			return;
		}

		final TwitterSearchResult sr = queryResult.getResults().get(position);

		new AsyncYFrogUpdater(this) {
			private TwitterStatus status;
			protected void doUpdate() throws YFrogTwitterException {
				status = twitterService.getStatus(sr.getId());
			}
			
			protected void doAfterUpdate() {
				if (status == null) {
					return;
				}
				Intent intent = new Intent(SearchResultsActivity.this,
						StatusDetailsActivity.class);
				intent.putExtra(StatusDetailsActivity.KEY_STATUSES,
						(Serializable) Arrays.asList(status));
				intent.putExtra(StatusDetailsActivity.KEY_STATUS_POS, 0);

				startActivity(intent);						
			}
		}.update();


	}
	
	@SuppressWarnings("unchecked")
	private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			showDetails(position);
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
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return AlertUtils.createErrorAlert(this, id);
	}
}
