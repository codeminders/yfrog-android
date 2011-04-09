/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.more;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
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

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.controller.service.ServiceFactory;
import com.codeminders.yfrog2.android.controller.service.TwitterService;
import com.codeminders.yfrog2.android.model.TwitterSavedSearch;
import com.codeminders.yfrog2.android.util.AlertUtils;
import com.codeminders.yfrog2.android.util.StringUtils;
import com.codeminders.yfrog2.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog2.android.view.message.WriteStatusActivity;

/**
 * @author idemydenko
 * 
 */
public class SearchActivity extends Activity implements OnClickListener {
	private static final String SAVED_SEARCHES = "searches";
	private static final String SAVED_ATTEMPTS = "sattempts";

	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;

	private static final int MENU_DELETE = 0;

	private TwitterService twitterService;
	private ArrayList<TwitterSavedSearch> searches = new ArrayList<TwitterSavedSearch>(0);
	private List<String> searchesQueries;
//	private Button saveButton;
	private ImageButton saveButton;
	private AutoCompleteTextView input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		twitterService = ServiceFactory.getTwitterService();

		setTitle(StringUtils.formatTitle(twitterService.getLoggedUser().getUsername(), 
				getResources().getString(R.string.s_title)));

		boolean restored = restoreState(savedInstanceState);
		
		createList(!restored);
	}

	protected void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}
	
	private boolean restoreState(Bundle savedState) {
		if (savedState == null) {
			return false;
		}
		
		Serializable values = savedState.getSerializable(SAVED_SEARCHES);
		if (values == null) {
			return false;
		}
		searches = (ArrayList<TwitterSavedSearch>) values;		
		
		int value = savedState.getInt(SAVED_ATTEMPTS);
		if (value < 0) {
			return false;
		}
		attempts = value;
		
		return true;
	}
	
	private void saveState(Bundle savedState) {
		if (savedState == null) {
			return;
		}
		
		savedState.putSerializable(SAVED_SEARCHES, searches);
		savedState.putInt(SAVED_ATTEMPTS, attempts);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		createList(false);
	}

	private void createList(boolean twitterUpdate) {
		if (twitterUpdate) {
			attempts = 1;
		}

		boolean needReload = twitterUpdate || isNeedReload();

		if (needReload) {
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					searches = twitterService.getSavedSearches();
				}

				protected void doAfterUpdate() {
					show();
				}
				
				@Override
				protected void doAfterError() {
					searches = new ArrayList<TwitterSavedSearch>(0);
					show();
				}
			}.update();
		} else {
			show();
		}
	}

	private void show() {
		searchesQueries = getStrings();
		setContentView(R.layout.twitter_saved_searches);

		if (searches.size() == 0) {
			View v = findViewById(R.id.s_searches_empty);
			v.setVisibility(View.VISIBLE);
		} else {
			ListView listView = (ListView) findViewById(R.id.s_searches_list);
			listView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, getStrings()));
			listView.setOnItemClickListener(mOnClickListener);
			registerForContextMenu(listView);
		}
//		Button button = (Button) findViewById(R.id.s_search_button);
		ImageButton button = (ImageButton) findViewById(R.id.s_search_button);
		button.setOnClickListener(this);

//		saveButton = (Button) findViewById(R.id.s_save_button);
		saveButton = (ImageButton) findViewById(R.id.s_save_button);
		saveButton.setOnClickListener(this);

		input = (AutoCompleteTextView) findViewById(R.id.s_search_input);
		input.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, searchesQueries));
		input.addTextChangedListener(inputTextWatcher);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(input, InputMethodManager.SHOW_FORCED);
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
		
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Serializable serializable = data.getSerializableExtra(SearchResultsActivity.KEY_SEARCHES);
			
			if (serializable != null) {
				searches = (ArrayList<TwitterSavedSearch>) serializable;
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		final String query = input.getText().toString().trim();

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

			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					TwitterSavedSearch search = null;
					
					if (isQuerySaved(query)) {
						search = twitterService.deleteSavedSearch(findSearchByQuery(query).getId());
						searches.remove(search);
					} else {
						search = twitterService.addSavedSearch(query);
						searches.add(search);
					}					
				}
				
				protected void doAfterUpdate() {
					createList(false);
				}
			}.update();
			
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
			final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			final TwitterSavedSearch toDelete = searches.get(info.position);
			
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					twitterService.deleteSavedSearch(toDelete.getId());
					searches.remove(info.position);
				}
				
				protected void doAfterUpdate() {
					createList(false);
				}
			}.update();
			return true;
		}
		return false;
	}

	private TwitterSavedSearch findSearchByQuery(String query) {
		if (!isQuerySaved(query)) {
			return null;
		}

		return searches.get(searchesQueries.indexOf(query.trim()));
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return AlertUtils.createErrorAlert(this, id);
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
//			Drawable d = isQuerySaved(s.toString()) ? getResources().getDrawable(R.drawable.delete_account) 
//					: getResources().getDrawable(R.drawable.save);
//			saveButton.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
			saveButton.setImageResource(isQuerySaved(s.toString()) ? R.drawable.delete_account : R.drawable.save);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}
	};
}
