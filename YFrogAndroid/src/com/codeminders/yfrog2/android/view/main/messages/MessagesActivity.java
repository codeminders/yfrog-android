/**
 * 
 */
package com.codeminders.yfrog2.android.view.main.messages;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogException;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.controller.service.ServiceFactory;
import com.codeminders.yfrog2.android.controller.service.TwitterService;
import com.codeminders.yfrog2.android.model.TwitterDirectMessage;
import com.codeminders.yfrog2.android.model.TwitterUser;
import com.codeminders.yfrog2.android.util.AlertUtils;
import com.codeminders.yfrog2.android.util.async.AsyncYFrogUpdater;
import com.codeminders.yfrog2.android.view.adapter.TwitterDirectMessageAdapter;
import com.codeminders.yfrog2.android.view.message.WriteDirectMessageActivity;
import com.codeminders.yfrog2.android.view.message.WriteStatusActivity;
import com.codeminders.yfrog2.android.view.user.UserDetailsActivity;

/**
 * @author idemydenko
 * 
 */
public class MessagesActivity extends ListActivity {
	private static final String SAVED_MESSAGES = "smessages";
	private static final String SAVED_ATTEMPTS = "sattempts";
	
	public static final String TAG = "messages";

	private static final int ATTEMPTS_TO_RELOAD = 5;
	private int attempts = 0;

	private static final int MENU_DELETE = 0;
	private static final int MENU_USERINFO = 1;
	private static final int MENU_REPLY = 2;

	private static final int DIALOG_ALERT_REPLY_ERROR = 5000;
	
	private TwitterService twitterService;
	private ArrayList<TwitterDirectMessage> messages = new ArrayList<TwitterDirectMessage>(0);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		twitterService = ServiceFactory.getTwitterService();
		boolean restored = restoreState(savedInstanceState);
		
		createList(!restored);
	}

	private boolean restoreState(Bundle savedState) {
		if (savedState == null) {
			return false;
		}
		
		
		Serializable values = savedState.getSerializable(SAVED_MESSAGES);
		if (values == null) {
			return false;
		}
		messages = (ArrayList<TwitterDirectMessage>) values;		
		
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
		
		savedState.putSerializable(SAVED_MESSAGES, messages);
		savedState.putInt(SAVED_ATTEMPTS, attempts);
	}

	
	@Override
	protected void onRestart() {
		super.onRestart();
		createList(true);
	}
	
	private void createList(boolean twitterUpdate) {
		if (twitterUpdate) {
			attempts = 1;
		}
		
		boolean needReload = twitterUpdate || isNeedReload();
		
		if (needReload) {
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					messages = twitterService.getDirectMessages();					
				}
				
				protected void doAfterUpdate() {
					show();
				}
			}.update();
		} else {
			show();
		}		
	}

	private void show() {
		setContentView(R.layout.twitter_dir_messages_list);
		setListAdapter(new TwitterDirectMessageAdapter<TwitterDirectMessage>(this, messages));
		registerForContextMenu(getListView());
	}
	
	private boolean isNeedReload() {
		return (++attempts % ATTEMPTS_TO_RELOAD == 0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.messages_tab, menu);
		getMenuInflater().inflate(R.menu.common_refresh_list, menu);
		getMenuInflater().inflate(R.menu.common_add_tweet, menu);
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
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(0, MENU_REPLY, 0, R.string.dm_reply);
		menu.add(0, MENU_USERINFO, 0, R.string.userinfo);
		menu.add(0, MENU_DELETE, 0, R.string.delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		switch (item.getItemId()) {
		case MENU_DELETE:
			final TwitterDirectMessage toDelete = getSelected(info.position);
			
			new AsyncYFrogUpdater(this) {
				protected void doUpdate() throws YFrogTwitterException {
					twitterService.deleteDirectMessage(toDelete.getId());
					messages.remove(info.position);
				}
				
				protected void doAfterUpdate() {
					createList(false); // View Adapter don't support add/remove operations from list
				}
			}.update();

			return true;
		
		case MENU_USERINFO:
			userInfo(info.position);
			return true;
		
		case MENU_REPLY:
			final TwitterUser toReply = getSelected(info.position).getSender();
			
			new AsyncYFrogUpdater(this) {
				boolean follower = false;
				protected void doUpdate() throws YFrogException {
					follower = twitterService.isFollower(toReply.getId()); 
				}
				
				@Override
				protected void doAfterUpdate() {
					if (follower) {
						Intent intent = new Intent(MessagesActivity.this, WriteDirectMessageActivity.class);
						intent.putExtra(WriteDirectMessageActivity.KEY_WRITER_USERNAME, toReply.getUsername());
						startActivity(intent);		
					} else {
						showDialog(DIALOG_ALERT_REPLY_ERROR);
					}
				}
			}.update();
		}
		return false;
	}
	
	private void userInfo(final int position) {
		new AsyncYFrogUpdater(this) {
			boolean following = false;
			boolean follower = false;
			TwitterUser u;
			
			@Override
			protected void doUpdate() throws YFrogTwitterException {
				u = getSelected(position).getSender();
				following = twitterService.isFollowing(u.getId());
				follower = twitterService.isFollower(u.getId());
				u.setFollower(follower);
				u.setFollowing(following);
			}
			
			@Override
			protected void doAfterUpdate() {
				Intent intent = new Intent(MessagesActivity.this, UserDetailsActivity.class);
				
				ArrayList<TwitterUser> users = new ArrayList<TwitterUser>(1);
				users.add(u);
				
				intent.putExtra(UserDetailsActivity.KEY_USERS, (Serializable) users);
				intent.putExtra(UserDetailsActivity.KEY_USER_POS, 0);
				
				startActivityForResult(intent, 0);
			}
		}.update();		
	}

	private TwitterDirectMessage getSelected(int position) {
		if (position > -1) {
			return messages.get(position);
		}
		return null;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_ALERT_REPLY_ERROR) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.dm_error_reply_title);
			builder.setMessage(R.string.dm_error_reply_msg);
			builder.setNegativeButton(R.string.error_alert_btn, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface d, int which) {
					d.dismiss();
				}
			});
			
			return builder.create();
		}
		
		if (id == AlertUtils.LOGOUT) {
			return AlertUtils.createLogoutAlert(this);
		}
		return AlertUtils.createErrorAlert(this, id);
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		saveState(outState);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog(AlertUtils.LOGOUT);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
