/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import com.codeminders.yfrog.android.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author idemydenko
 *
 */
public class WriteActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.twitter_writable);
	}
}
