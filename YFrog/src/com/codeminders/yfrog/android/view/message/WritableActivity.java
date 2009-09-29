/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * @author idemydenko
 *
 */
public abstract class WritableActivity extends Activity implements OnClickListener, TextWatcher, ViewFactory {
	private static final int MAX_COUNT = 140;
	private static final int OVERRIDED_TEXT_COLOR = 0xFF0000;
	
	protected TwitterService twitterService;
	
	private TextSwitcher switcher;
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitterService = ServiceFactory.getTwitterService();
		setContentView(R.layout.twitter_writable);

		EditText editText = (EditText) findViewById(R.id.wr_text);
		editText.addTextChangedListener(this);

		switcher = (TextSwitcher) findViewById(R.id.wr_counter);
		switcher.setFactory(this);

		count = editText.getText().length();
		
		Button button = (Button) findViewById(R.id.wr_send);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.wr_photo);
		button.setOnClickListener(this);
		
		updateCounter();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wr_send:
			if (isOverrideMaxCount()) {
				return;
			}
			
			EditText textInput = (EditText) findViewById(R.id.wr_text);
			String text = textInput.getText().toString();
			
			send(text);
			finish();
			break;
		case R.id.wr_photo:
			
			break;
		}
	}
	
	protected abstract void send(String text);
	
	@Override
	public View makeView() {
        TextView t = new TextView(this);
        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        
        if (isOverrideMaxCount()) {
        	t.setTextColor(OVERRIDED_TEXT_COLOR);
        }
        return t;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		count = s.length();
		updateCounter();
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	private void updateCounter() {
		switcher.setText(String.valueOf(count));
	}
	
	private boolean isOverrideMaxCount() {
		return count > MAX_COUNT;
	}
}