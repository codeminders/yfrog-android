/**
 * 
 */
package com.codeminders.yfrog2.android.util.async;

import java.util.ArrayList;

import android.os.AsyncTask;



/**
 * @author idemydenko
 * 
 */
public final class ViewAsyncUpdateTask<K extends ViewAsyncUpdatable> extends AsyncTask<Void, Void, Void> {
	private ArrayList<K> toLoad = new ArrayList<K>();
	private UpdateListener<K> updateListener;
	
	public ViewAsyncUpdateTask() {
		
	}

	public ViewAsyncUpdateTask(UpdateListener<K> listener) {
		updateListener = listener;
	}
	
	public void setUpdateListener(UpdateListener<K> updateListener) {
		this.updateListener = updateListener;
	}
	
	public void addToLoad(K viewable) {
		synchronized (toLoad) {
			toLoad.add(viewable);
			toLoad.notify();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		Thread.currentThread().setName("ViewAsyncUpdateTask");

		System.out.println("Start Load task");

		while (true) {
			int size = 0;

			synchronized (toLoad) {
				size = toLoad.size();
			}

			if (size > 0) {
				K updatable;
				
				synchronized (toLoad) {
					updatable = toLoad.remove(0);
				}
				
				Object received = updatable.receiveData();
				updatable.putData0(received);

				if (updateListener != null) {
					updateListener.onUpdate(updatable, received);
				}
			} else {
				try {
					synchronized (toLoad) {
						toLoad.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static interface UpdateListener<K extends ViewAsyncUpdatable> {
		public void onUpdate(K updatable, Object received);
	}
}
