/**
 * 
 */
package com.codeminders.yfrog2.android.view.media.pick;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.controller.service.AccountService;
import com.codeminders.yfrog2.android.controller.service.ServiceFactory;
import com.codeminders.yfrog2.android.util.FileUtils;

/**
 * @author idemydenko
 * 
 */
public class VideoPickActivity extends ListActivity {
	static final int MESSAGE_SHOW_DIRECTORY_CONTENTS = 500; 
	static final int MESSAGE_SET_PROGRESS = 501; 

	private static final String BUNDLE_CURRENT_DIRECTORY = "current_directory";
	private static final String BUNDLE_DIR_VALUE = "dir_value";

	private static final int MENU_VIEW = 0;
	
	private ArrayList<String> directoryEntries = new ArrayList<String>();

	private File currentDirectory = new File("");

	private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";

	private int comparativeDirValue;

	private EditText editFilename;
	private Button buttonPick;

	private TextView emptyText;
	private ProgressBar progressBar;

	private DirectoryScanner directoryScanner;
	private File previousDirectory;

	private Handler currentHandler;

	private FilenameFilter filenameFilter = new VideoFileFilter();
	
	private AccountService accountService;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		accountService = ServiceFactory.getAccountService();

		currentHandler = new Handler() {
			public void handleMessage(Message msg) {
				VideoPickActivity.this.handleMessage(msg);
			}
		};

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.pick_video);

		emptyText = (TextView) findViewById(R.id.vp_empty_text);
		progressBar = (ProgressBar) findViewById(R.id.vp_scan_progress);

		getListView().setOnCreateContextMenuListener(this);
		getListView().setEmptyView(findViewById(R.id.empty));
		getListView().setTextFilterEnabled(true);
		getListView().requestFocus();
		getListView().requestFocusFromTouch();

		setTitle(createTitle());
		
		editFilename = (EditText) findViewById(R.id.vp_filename);

		buttonPick = (Button) findViewById(R.id.vp_button_pick);
		buttonPick.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				pick();
			}
		});

		File browseto = new File(path);
		comparativeDirValue = 0;

		if (icicle != null) {
			browseto = new File(icicle.getString(BUNDLE_CURRENT_DIRECTORY));
			comparativeDirValue = icicle.getInt(BUNDLE_DIR_VALUE);
		}

		browseTo(browseto);
	}

	public void onDestroy() {
		super.onDestroy();
		if (directoryScanner != null) {
			directoryScanner.cancel = true;
		}

		directoryScanner = null;
	}

	private void handleMessage(Message message) {
		switch (message.what) {
		case MESSAGE_SHOW_DIRECTORY_CONTENTS:
			showDirectoryContents((ArrayList<String>) message.obj);
			break;

		case MESSAGE_SET_PROGRESS:
			setProgress(message.arg1, message.arg2);
			break;
		}

	}

	private void setProgress(int progress, int maxProgress) {
		progressBar.setMax(maxProgress);
		progressBar.setProgress(progress);
		progressBar.setVisibility(View.VISIBLE);
	}

	private void showDirectoryContents(ArrayList<String> contents) {
		directoryScanner = null;

		directoryEntries.ensureCapacity(contents.size());
		directoryEntries.addAll(contents);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, directoryEntries);
		
		setListAdapter(adapter);
		getListView().setTextFilterEnabled(true);

		selectInList(previousDirectory);
		setProgressBarIndeterminateVisibility(false);

		progressBar.setVisibility(View.GONE);
		emptyText.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(BUNDLE_CURRENT_DIRECTORY, currentDirectory
				.getAbsolutePath());
		outState.putInt(BUNDLE_DIR_VALUE, comparativeDirValue);
	}

	private void pick() {
		String filename = editFilename.getText().toString();
		File file = FileUtils.getFile(currentDirectory.getAbsolutePath(), filename);

		Intent intent = getIntent();
		intent.setData(FileUtils.getUri(file));
		setResult(RESULT_OK, intent);
		finish();
	}

	private void upOneLevel() {
		if (comparativeDirValue > 0) {
			comparativeDirValue--;
		}
		if (currentDirectory.getParent() != null)
			browseTo(currentDirectory.getParentFile());
	}

	private void browseTo(final File directory) {
		if (directory.isDirectory()) {
				previousDirectory = currentDirectory;
				currentDirectory = directory;
				refreshList();
		} else {
			editFilename.setText(directory.getName());
		}
	}

	private void refreshList() {
		DirectoryScanner scanner = directoryScanner;

		if (scanner != null) {
			scanner.cancel = true;
		}

		directoryEntries.clear();

		setProgressBarIndeterminateVisibility(true);

		emptyText.setVisibility(View.GONE);

		progressBar.setVisibility(View.GONE);
		setListAdapter(null);

		directoryScanner = new DirectoryScanner(currentDirectory, filenameFilter, this,
				currentHandler);
		directoryScanner.start();
	}

	private void selectInList(File selectFile) {
		String filename = selectFile.getName();
		ArrayAdapter<String> la = (ArrayAdapter<String>) getListAdapter();
		int count = la.getCount();
		for (int i = 0; i < count; i++) {
			String it = la.getItem(i);
			if (it.equals(filename)) {
				getListView().setSelection(i);
				break;
			}
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();

		String file = adapter.getItem(position);
		String curdir = currentDirectory.getAbsolutePath();
		File clickedFile = FileUtils.getFile(curdir, file);
		if (clickedFile != null) {
			if (clickedFile.isDirectory()) {
				comparativeDirValue++;
			}
			browseTo(clickedFile);
			
			if (!clickedFile.isDirectory()) {
				pick();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (comparativeDirValue > 0) {
				upOneLevel();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, MENU_VIEW, 0, R.string.vp_view);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		String filename = (String) getListAdapter().getItem(info.position);
		String filepath = currentDirectory.getAbsolutePath() + "/" + filename;
		switch (item.getItemId()) {
		case MENU_VIEW:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(filepath), "video/*");
			startActivity(intent);
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	private String createTitle() {
		return accountService.getLogged().getUsername() + "> " + getResources().getString(R.string.vp_title);
	}

}


class VideoFileFilter implements FilenameFilter {
	public static final String FORMAT_3GP = ".3gp";
	public static final String FORMAT_MP4 = ".mp4";
	
	private static final HashSet<String> EXTENTIONS = new HashSet<String>();
	
	static {
		EXTENTIONS.add(FORMAT_3GP);
		EXTENTIONS.add(FORMAT_MP4);
	}
	
	public boolean accept(File dir, String filename) {
		if (filename == null) {
			return false;
		}
		return EXTENTIONS.contains(FileUtils.getExtension(filename));
	}
	
}