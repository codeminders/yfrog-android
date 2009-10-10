/**
 * 
 */
package com.codeminders.yfrog.android.view.message;

import com.codeminders.yfrog.android.R;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.service.ServiceFactory;
import com.codeminders.yfrog.android.controller.service.TwitterService;
import com.codeminders.yfrog.android.model.TwitterStatus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author idemydenko
 *
 */

// TODO may be need StatusChangeListener
public class StatusDetailsActivity extends Activity implements OnClickListener {
	public static final String KEY_STATUS = "status";
	
	private TwitterService twitterService;
	private TwitterStatus status;
	private boolean favorited;
	private boolean my;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitterService = ServiceFactory.getTwitterService();
		setContentView(R.layout.twitter_status_details);
		
		Bundle extras = getIntent().getExtras();
		status = (TwitterStatus) extras.getSerializable(KEY_STATUS);
		favorited = status.isFavorited();
		my = twitterService.getLoggedUser().equals(status.getUser());
		
		TextView view = (TextView) findViewById(R.id.tu_username);
		view.setText(status.getUser().getUsername());
		
		view = (TextView) findViewById(R.id.tu_fullname);
		view.setText(status.getUser().getFullname());
		
		view = (TextView) findViewById(R.id.tm_text);
		view.setText(status.getText());
		
		Button button = (Button) findViewById(R.id.tm_replay);
		if (my) {
			button.setVisibility(View.INVISIBLE);
		}
		button.setOnClickListener(this);

		
		button = (Button) findViewById(R.id.tm_favorite);
		if (favorited) {
			button.setText(R.string.tm_btn_unfavorite);
		}
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tm_forward);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.tm_delete);
		if (!my) {
			button.setVisibility(View.INVISIBLE);
		}
		button.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tm_replay:
			Intent intent = new Intent(this, WriteReplayActivity.class);
			intent.putExtra(WriteReplayActivity.KEY_MESSAGE_ID, status.getId());
			intent.putExtra(WritableActivity.KEY_WRITER_USERNAME, status.getUser().getUsername());
			startActivity(intent);
			break;
		case R.id.tm_favorite:
			favorite();
			break;
		case R.id.tm_forward:
			break;
		case R.id.tm_delete:
			try {
				twitterService.deleteStatus(status.getId());
			} catch (YFrogTwitterException e) {
				// TODO: handle exception
			}
			finish();
			break;
		}
	}
	
	private void favorite() {
		try {
			if (!favorited) {
				twitterService.favorite(status.getId());
			} else {
				twitterService.unfavorite(status.getId());
			}
			favorited = !favorited;
			
			Button button = (Button) findViewById(R.id.tm_favorite);
			button.setText(favorited ? R.string.tm_btn_unfavorite : R.string.tm_btn_favorite);
		} catch (YFrogTwitterException e) {
			// TODO: handle exception
		}		
	}
	
}
