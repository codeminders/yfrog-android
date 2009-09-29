/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.ArrayList;
import java.util.List;

import com.codeminders.yfrog.android.YFrogTwitterAuthException;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.TwitterDirectMessage;
import com.codeminders.yfrog.android.model.TwitterStatus;
import com.codeminders.yfrog.android.model.TwitterUser;

import twitter4j.DirectMessage;
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
	TwitterUser loggedUser = null;
	
	TwitterService() {
	}
	
	public void login(String nickname, String password) throws YFrogTwitterException {
		twitter = new Twitter(nickname, password);
		
		if (!isLogged()) {
			throw new YFrogTwitterAuthException();
		}
	}
	
	public boolean isLogged() {
		
		try {
			loggedUser = Twitter4jHelper.getUser(twitter.verifyCredentials());
		} catch (TwitterException e) {
			return false;
		} catch (NullPointerException e) {
			// TODO: bug in Twitter4J (Response<init>, initialize is from error stream)
			return false;
		}
		return loggedUser != null;
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
	
	public ArrayList<TwitterDirectMessage> getDirectMessages() throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getDirectMessages(twitter.getDirectMessages());
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}
	}
 	
	public ArrayList<TwitterUser> getFollowers() throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getUsers(twitter.getFollowersStatuses());
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e);
		}
	}

	public ArrayList<TwitterUser> getFollowings() throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getUsers(twitter.getFriendsStatuses());
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e);
		}
	}

	public ArrayList<TwitterStatus> getMyTweets() throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatuses(twitter.getUserTimeline());
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e);
		}		
	}

	public ArrayList<TwitterStatus> getUserTweets(String username) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatuses(twitter.getUserTimeline(username));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e);
		}		
	}

	public void logout() {
		loggedUser = null;
		twitter = null;
	}
	
	public void updateStatus(String text) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.updateStatus(text);
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}		
	}

	public void replay(String text, long replayToId) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.updateStatus(text, replayToId);
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}		
	}

	public void favorite(long id) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.createFavorite(id);
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}
	}

	public void unfavorite(long id) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.destroyFavorite(id);
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}
	}

	public TwitterUser getLoggedUser() {
		return loggedUser;
	}
	
	public void deleteStatus(long id) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.destroyStatus(id);
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}
	}
	
	public void deleteDirectMessage(int id) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.destroyDirectMessage(id);
		} catch (TwitterException e) {
			throw new YFrogTwitterException();
		}		
	}
	
	private void checkCreated() {
		if (twitter == null || loggedUser == null) {
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
		st.setId(status.getId());
		st.setFavorited(status.isFavorited());
		
		return st;
	}
	
	static ArrayList<TwitterUser> getUsers(List<User> users) {
		int size = users.size();
		ArrayList<TwitterUser> result = new ArrayList<TwitterUser>();
		
		for (int i = 0; i < size; i++) {
			result.add(Twitter4jHelper.getUser(users.get(i)));
		}
		
		return result;
	}
	
	static TwitterUser getUser(User user) {
		TwitterUser u = new TwitterUser();
		
		u.setId(user.getId());
		u.setFullname(user.getName());
		u.setProfileImageURL(user.getProfileImageURL());
		u.setUsername(user.getScreenName());
		u.setLocation(user.getLocation());
		u.setDescription(user.getDescription());
		
		return u;
	}
	
	static ArrayList<TwitterDirectMessage> getDirectMessages(List<DirectMessage> dms) {
		int size = dms.size();
		ArrayList<TwitterDirectMessage> result = new ArrayList<TwitterDirectMessage>();
		
		for (int i = 0; i < size; i++) {
			result.add(getDirectMessage(dms.get(i)));
		}
		return result;
	}
	
	static TwitterDirectMessage getDirectMessage(DirectMessage dm) {
		TwitterDirectMessage m = new TwitterDirectMessage();
		
		m.setSender(getUser(dm.getSender()));
		m.setText(dm.getText());
		m.setCreatedAt(dm.getCreatedAt());
		m.setId(dm.getId());
		
		return m;
	}
}