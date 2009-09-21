/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.ArrayList;
import java.util.List;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.model.TwitterUser;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author idemydenko
 *
 */
public class TwitterService {
	private Twitter twitter = null;
	
	TwitterService() {
	}
	
	public void login(String nickname, String password) throws YFrogTwitterException {
		twitter = new Twitter(nickname, password);
		
		if (!isLogged()) {
			throw new YFrogTwitterException();
		}
	}
	
	public boolean isLogged() {
		
		boolean result = false;
		
		try {
			result = twitter.test();
		} catch (TwitterException e) {
			result = false;
		}
		return result;
	}
	
	public ArrayList<TwitterStatus> getMentions() throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatuses(twitter.getMentions());
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}
	}
	
	public ArrayList<TwitterStatus> getFriendsTimeline() throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatuses(twitter.getFriendsTimeline());
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}
	}
	
	public void logout() {
		twitter = null;
	}
	
	private void checkCreated() {
		if (twitter == null) {
			throw new IllegalStateException();
		}
	}
}

final class Twitter4jHelper {
	private Twitter4jHelper() {
		
	}
	
	static ArrayList<TwitterStatus> getStatuses(List<Status> statuses) {
		int size = statuses.size();
		ArrayList<TwitterStatus> result = new ArrayList<TwitterStatus>();
		
		for (int i = 0; i < size; i++) {
			result.add(getStatus(statuses.get(i)));
		}
		
		return result;
	}
	
	static TwitterStatus getStatus(Status status) {
		TwitterStatus st = new TwitterStatus();
		st.setUser(getUser(status.getUser()));
		st.setText(status.getText());
		st.setCreatedAt(status.getCreatedAt());
		
		return st;
	}
	
	static TwitterUser getUser(User user) {
		TwitterUser u = new TwitterUser();
		
		u.setId(user.getId());
		u.setName(user.getName());
		u.setProfileImageURL(user.getProfileImageURL());
		
		return u;
	}
}