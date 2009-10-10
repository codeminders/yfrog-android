/**
 * 
 */
package com.codeminders.yfrog.android.util.async;

import java.net.URL;

import android.view.View;

/**
 * @author idemydenko
 *
 */
public abstract class ViewAsyncUpdatable<T extends View, V> {
	private URL url;
	private T view;

	final public void setView(T view) {
		this.view = view;
	}
	
	final public T getView() {
		return view;
	}
	
	final public URL getUrl() {
		return url;
	}

	final public void setUrl(URL url) {
		this.url = url;
	}

	void putData0(final V object) {
		if (view != null && object != null) {
			view.post(new Runnable() {
				public void run() {
					putData(object);
				}
			});
		}
	}
	
	public abstract void putData(V obj);
	
	public abstract V receiveData();
}
