/**
 * 
 */
package com.codeminders.yfrog.android.view.main.more;

import com.codeminders.yfrog.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author idemydenko
 *
 */
public class SettingsActivity extends Activity implements OnCheckedChangeListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.settings);
		
		CheckBox checkBox = (CheckBox) findViewById(R.id.st_locations);
		checkBox.setOnCheckedChangeListener(this);
		
		checkBox = (CheckBox) findViewById(R.id.st_scale);
		checkBox.setOnCheckedChangeListener(this);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.st_locations:
			
			break;

		case R.id.st_scale:
			
			break;
		}
		
	}
}
