/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterSavedSearch;
import com.codeminders.yfrog.android.util.AlertUtils;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.android.view.message.WriteStatusActivity;

/**
 * @author idemydenko
 * 
 */
public class SearchActivity extends Activity implements OnClickListener {
	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;

	private static final int MENU_DELETE = 0;

	private TwitterService twitterService;
	private ArrayList<TwitterSavedSearch> searches;
	private List<String> searchesQueries;
	private Button saveButton;
	private AutoCompleteTextView input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		twitterService = ServiceFactory.getTwitterService();

		createList(true);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		createList(true);
	}

	private void createList(boolean twitterUpdate) {
		if (twitterUpdate) {
			attempts = 1;
		}

		boolean needReload = twitterUpdate || isNeedReload();

		if (needReload) {
			try {
				searches = twitterService.getSavedSearches();
			} catch (YFrogTwitterException e) {
				showDialog(AlertUtils.ALERT_TWITTER_ERROR);
			}
		}

		searchesQueries = getStrings();
		setContentView(R.layout.twitter_saved_searches);

		ListView listView = (ListView) findViewById(R.id.s_searches_list);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getStrings()));
		listView.setOnItemClickListener(mOnClickListener);
		registerForContextMenu(listView);
		
		Button button = (Button) findViewById(R.id.s_search_button);
		button.setOnClickListener(this);

		saveButton = (Button) findViewById(R.id.s_save_button);
		saveButton.setOnClickListener(this);

		input = (AutoCompleteTextView) findViewById(R.id.s_search_input);
		input.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, searchesQueries));
		input.addTextChangedListener(inputTextWatcher);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(input, InputMethodManager.SHOW_FORCED);
		// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
		// InputMethodManager.RESULT_UNCHANGED_SHOWN); TODO Forced show
	}

	private boolean isNeedReload() {
		return (++attempts % ATTEMPTS_TO_RELOAD == 0);
	}

	private List<String> getStrings() {
		List<String> list = new ArrayList<String>(searches.size());

		for (TwitterSavedSearch tss : searches) {
			list.add(tss.getName());
		}

		return list;
	}

	private boolean isQuerySaved(String query) {
		return searchesQueries.contains(query.trim());
	}
	
	private void showSearchResults(String query, int savedId) {
		Intent intent = new Intent(this, SearchResultsActivity.class);
		intent.putExtra(SearchResultsActivity.KEY_QUERY, query);
		intent.putExtra(SearchResultsActivity.KEY_SEARCHES, (Serializable) searches);
		
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		String query = input.getText().toString().trim();

		if (StringUtils.isEmpty(query)) {
			return;
		}

		switch (v.getId()) {
		case R.id.s_search_button:
			int resultId = 0;
			
			if (isQuerySaved(query)) {
				resultId = findSearchByQuery(query).getId();
			} 
			
			showSearchResults(query, resultId);
			break;
		case R.id.s_save_button:
			input.setText(StringUtils.EMPTY_STRING);

			TwitterSavedSearch search = null;
			try {
				if (isQuerySaved(query)) {
					search = twitterService.deleteSavedSearch(findSearchByQuery(query).getId());
					searches.remove(search);
				} else {
					search = twitterService.addSavedSearch(query);
					searches.add(search);
				}
			} catch (YFrogTwitterException e) {
				e.printStackTrace();
			}
			createList(false);
			break;

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common_refresh_list, menu);
		inflater.inflate(R.menu.common_add_tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.reload_list:
			createList(true);
			return true;
		case R.id.add_tweet:
			Intent intent = new Intent(this, WriteStatusActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, MENU_DELETE, 0, R.string.delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();

			TwitterSavedSearch toDelete = searches.get(info.position);

			try {
				twitterService.deleteSavedSearch(toDelete.getId());
				searches.remove(info.position);
			} catch (YFrogTwitterException e) {
				// TODO
			}
			createList(false);

			return true;
		}
		return false;
	}

	YFrogTwitterException toHandle;

	private TwitterSavedSearch findSearchByQuery(String query) {
		if (!isQuerySaved(query)) {
			return null;
		}

		return searches.get(searchesQueries.indexOf(query.trim()));
	}

	private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			TwitterSavedSearch savedSearch = searches.get(position);
			showSearchResults(savedSearch.getQuery(), savedSearch.getId());
		}
	};

	private TextWatcher inputTextWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			saveButton.setText(isQuerySaved(s.toString()) ? R.string.delete : R.string.save);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}
	};
}
