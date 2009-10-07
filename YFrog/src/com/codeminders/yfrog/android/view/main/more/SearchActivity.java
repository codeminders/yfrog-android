/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import java.util.ArrayList;
import java.util.List;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.StringUtils;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterSavedSearch;
import com.codeminders.yfrog.android.util.AlertUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * @author idemydenko
 *
 */
public class SearchActivity extends Activity implements OnClickListener {
	private static final int MENU_DELETE = 0;
	
	private static final int DIALOG_ADD = 0;
	
	private TwitterService twitterService;
	private ArrayList<TwitterSavedSearch> searches;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterService = ServiceFactory.getTwitterService();
		
		createSearchesList();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		createSearchesList();
	}
	
	private void createSearchesList() {
		try {
			searches = twitterService.getSavedSearches();
		} catch (YFrogTwitterException e) {
			showDialog(AlertUtils.ALERT_TWITTER_ERROR);
		}
		
		setContentView(R.layout.twitter_saved_searches);
		
		ListView listView = (ListView) findViewById(R.id.s_searches_list);
		
		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getStrings()));
		listView.setOnItemClickListener(mOnClickListener);
		registerForContextMenu(listView);
		
		Button button = (Button) findViewById(R.id.s_search_button);
		button.setOnClickListener(this);

	}
	
	private List<String> getStrings() {
		List<String> list = new ArrayList<String>(searches.size());
		
		for (TwitterSavedSearch tss : searches) {
			list.add(tss.getName());
		}
		
		return list;
	}
	private void showSearchResults(String query, boolean isSaved, int savedId) {
		Intent intent = new Intent(this, SearchResultsActivity.class);
		intent.putExtra(SearchResultsActivity.KEY_QUERY, query);
		intent.putExtra(SearchResultsActivity.KEY_SAVED, isSaved);
		if (isSaved) {
			intent.putExtra(SearchResultsActivity.KEY_QUERY_ID, savedId);
		}
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.s_search_button:
			EditText text = (EditText) findViewById(R.id.s_search_input);
			String query = text.getText().toString();
			if (!StringUtils.isEmpty(query)) {
				showSearchResults(query, false, 0);
			}
			break;
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.s_add_search:
			showDialog(DIALOG_ADD);
			return true;
		}
		return false;
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
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			
			TwitterSavedSearch toDelete = searches.get(info.position);
			
			try {
				twitterService.deleteSavedSearch(toDelete.getId());
			} catch (YFrogTwitterException e) {
				// TODO
			}
			createSearchesList();
			return true;
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ADD:
			LayoutInflater inflater = LayoutInflater.from(this);
			final View view = inflater.inflate(R.layout.add_search_dialog, null);
			
			final AlertDialog d = new AlertDialog.Builder(this)
				.setTitle(R.string.s_add_search)
				.setView(view)
				.create();

			d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getText(R.string.save),
					new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText text = (EditText) d.findViewById(R.id.s_add_search_input);
						String query = text.getText().toString();
						text.setText(StringUtils.EMPTY_STRING);
						
						if (!StringUtils.isEmpty(query)) {
							try {
								twitterService.addSavedSearch(query);
							} catch (YFrogTwitterException e) {
								// TODO: handle exception
							}
							
							createSearchesList();
						}
					}
			});
			
			d.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getText(R.string.cancel),
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			return d;
		case AlertUtils.ALERT_TWITTER_ERROR:
			Dialog errorDialiog = AlertUtils.createTwitterErrorAlert(this, toHandle);
			toHandle = null;
			return errorDialiog;
		}
		return super.onCreateDialog(id);
	}
	
	YFrogTwitterException toHandle;
	
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			TwitterSavedSearch savedSearch = searches.get(position);
			showSearchResults(savedSearch.getQuery(), true, savedSearch.getId());
		}
	};

}
