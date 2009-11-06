package com.codeminders.yfrog.android.view.media.pick;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class DirectoryScanner extends Thread {

	private File currentDirectory;
	boolean cancel;

	private Context context;
	private Handler handler;
	private long operationStartTime;
	private FilenameFilter filter;

	// Update progress bar every n files
	static final private int PROGRESS_STEPS = 50;

	DirectoryScanner(File directory, FilenameFilter filenameFilter, Context context, Handler handler) {
		super("Directory Scanner");
		currentDirectory = directory;
		this.context = context;
		this.handler = handler;
		this.filter = filenameFilter;
	}

	private void clearData() {
		context = null;
		handler = null;
	}

	public void run() {
		
		File[] files = null;
		
		if (filter != null) {
			files = currentDirectory.listFiles(filter);
		} else {
			files = currentDirectory.listFiles();
		}

		int totalCount = 0;

		if (cancel) {
			clearData();
			return;
		}

		operationStartTime = SystemClock.uptimeMillis();

		totalCount = files.length;

		int progress = 0;

		List<String> listDir = new ArrayList<String>(totalCount);
		List<String> listFile = new ArrayList<String>(totalCount);

		for (File currentFile : files) {
			if (cancel) {
				clearData();
				return;
			}

			progress++;
			updateProgress(progress, totalCount);

			if (currentFile.isDirectory()) {
				listDir.add("> " + currentFile.getName());

			} else {
				listFile.add(currentFile.getName());
			}
		}

		Collections.sort(listDir);
		Collections.sort(listFile);

		if (!cancel) {
//			DirectoryContents contents = new DirectoryContents();
//
//			contents.listDir = listDir;
//			contents.listFile = listFile;

			ArrayList<String> content = new ArrayList<String>(listDir.size() + listFile.size());
			content.addAll(listDir);
			content.addAll(listFile);
			
			Message msg = handler
					.obtainMessage(VideoPickActivity.MESSAGE_SHOW_DIRECTORY_CONTENTS);
			msg.obj = content;
			msg.sendToTarget();
		}

		clearData();
	}

	private void updateProgress(int progress, int maxProgress) {
		if ((progress % PROGRESS_STEPS) == 0) {
			long curTime = SystemClock.uptimeMillis();

			if (curTime - operationStartTime < 1000L) {
				return;
			}

			Message msg = handler
					.obtainMessage(VideoPickActivity.MESSAGE_SET_PROGRESS);
			msg.arg1 = progress;
			msg.arg2 = maxProgress;
			msg.sendToTarget();
		}
	}
}
