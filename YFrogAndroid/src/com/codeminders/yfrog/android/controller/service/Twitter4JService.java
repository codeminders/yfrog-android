/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.*;

import twitter4j.*;
import twitter4j.http.*;

import com.codeminders.yfrog.android.*;
import com.codeminders.yfrog.android.model.*;

/**
 * @author idemydenko
 *
 */
public class Twitter4JService implements TwitterService {
	/*
	 * Desktop application tokens
	 * 
	 *	private static final String CONSUMER_KEY = "Ai9SAL3FqA64k5uiY8ezA";
	 *	private static final String CONSUMER_SECRET = "Piy2dJzdFVUMdUqrRLBUfkW2VcTnWnr2tnO6vHrZ2k"; 
	 */
	
	private static final String CONSUMER_KEY = "16F75LNJxjKTIUHidy5Sg";
	private static final String CONSUMER_SECRET = "Sp3gGl1RvWtICmphby4MAomRCTj9sGvcE8b7XqUxxnQ";
	
	private Twitter twitter = null;
	private TwitterUser loggedUser = null;
	private UnsentMessageService unsentMessageService;
	
	Twitter4JService() {
		unsentMessageService = ServiceFactory.getUnsentMessageService();
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#login(java.lang.String, java.lang.String)
	 */
	public void login(String nickname, String password) throws YFrogTwitterException {
		twitter = new Twitter(nickname, password);
		
		if (!isLogged()) {
			throw new YFrogTwitterAuthException();
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getOAuthWebAuthorizationURL(com.codeminders.yfrog.android.model.Account)
	 */
	public String getOAuthWebAuthorizationURL(Account account) throws YFrogTwitterException {
		Twitter twitter = new Twitter();
	    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	    RequestToken requestToken;
	    
	    try {
	    	requestToken = twitter.getOAuthRequestToken(CALL_BACK_URL);
	    } catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	    
	    account.setOauthToken(requestToken.getToken());
	    account.setOauthTokenSecret(requestToken.getTokenSecret());
	    
	    return requestToken.getAuthorizationURL();
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#verifyOAuthAuthorization(com.codeminders.yfrog.android.model.Account, java.lang.String)
	 */
	public void verifyOAuthAuthorization(Account account, String pin) throws YFrogTwitterException {
		Twitter twitter = new Twitter();
	    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	    
	    try {
	    	AccessToken accessToken = twitter.getOAuthAccessToken(account.getOauthToken(), account.getOauthTokenSecret(), pin);
	    	account.setOauthToken(accessToken.getToken());
	    	account.setOauthTokenSecret(accessToken.getTokenSecret());
	    } catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#loginOAuth(java.lang.String, java.lang.String)
	 */
	public void loginOAuth(String oauthTolken, String oauthSecretTolken) throws YFrogTwitterException {
		twitter = new Twitter();
		
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	    twitter.setOAuthAccessToken(oauthTolken, oauthSecretTolken);
		
		if (!isLogged()) {
			throw new YFrogTwitterAuthException();
		}
	}

	// TODO Can we move logged user to other method
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#isLogged()
	 */
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
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getCredentials(com.codeminders.yfrog.android.model.Account)
	 */
	public TwitterUser getCredentials(Account account) throws YFrogTwitterException {
		Twitter twitter = new Twitter();
		
		if (account.getAuthMethod() == Account.METHOD_COMMON) {
			twitter.setUserId(account.getUsername());
			twitter.setPassword(account.getPassword());
		} else {
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			twitter.setOAuthAccessToken(account.getOauthToken(), account.getOauthTokenSecret());
		}
		try {
			return Twitter4jHelper.getUser(twitter.verifyCredentials());
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
 	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getMentions(int, int)
	 */
	public ArrayList<TwitterStatus> getMentions(int page, int count) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatuses(twitter.getMentions(Twitter4jHelper.createPaging(page, count)));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getHomeStatuses(int, int)
	 */
	public ArrayList<TwitterStatus> getHomeStatuses(int page, int count) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatuses(twitter.getFriendsTimeline(Twitter4jHelper.createPaging(page, count)));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getDirectMessages()
	 */
	public ArrayList<TwitterDirectMessage> getDirectMessages() throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getDirectMessages(twitter.getDirectMessages());
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}
 	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getFollowers()
	 */
	public ArrayList<TwitterUser> getFollowers() throws YFrogTwitterException {
		checkCreated();
		try {
			IDs followings = twitter.getFriendsIDs();
			return Twitter4jHelper.getUsers(twitter.getFollowersStatuses(), null, followings);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getUserFollowers(java.lang.String)
	 */
	public ArrayList<TwitterUser> getUserFollowers(String username) throws YFrogTwitterException {
		checkCreated();
		try {
			IDs followers = twitter.getFollowersIDs();
			IDs followings = twitter.getFriendsIDs();
			return Twitter4jHelper.getUsers(twitter.getFollowersStatuses(username), followers, followings);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getFollowings()
	 */
	public ArrayList<TwitterUser> getFollowings() throws YFrogTwitterException {
		checkCreated();
		try {
			IDs followers = twitter.getFollowersIDs();

			return Twitter4jHelper.getUsers(twitter.getFriendsStatuses(), followers, null);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getMyTweets(int, int)
	 */
	public ArrayList<TwitterStatus> getMyTweets(int page, int count) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatuses(twitter.getUserTimeline(Twitter4jHelper.createPaging(page, count)));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}		
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getUserTweets(java.lang.String, int, int)
	 */
	public ArrayList<TwitterStatus> getUserTweets(String username, int page, int count) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatuses(twitter.getUserTimeline(username, Twitter4jHelper.createPaging(page, count)));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#sendUnsentMessage(com.codeminders.yfrog.android.model.UnsentMessage)
	 */
	public void sendUnsentMessage(UnsentMessage message) throws YFrogTwitterException {
		String text = message.getText();
		switch (message.getType()) {
		case UnsentMessage.TYPE_DIRECT_MESSAGE:
			sendDirectMessage(message.getTo(), text);
			break;
		case UnsentMessage.TYPE_PUBLIC_REPLAY:
			publicReplay(text);
			break;
		case UnsentMessage.TYPE_REPLAY:
			replay(text, Long.valueOf(message.getTo()));
			break;
		case UnsentMessage.TYPE_STATUS:
			updateStatus(text);
			break;
		}
		unsentMessageService.deleteUnsentMessage(message.getId());
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#sendAllUnsentMessages()
	 */
	public void sendAllUnsentMessages() throws YFrogTwitterException {
		Account logged = ServiceFactory.getAccountService().getLogged();
		ArrayList<UnsentMessage> toSent = unsentMessageService.getUnsentMessagesForAccount(logged.getId());
		
		int size = toSent.size();
		for (int i = 0; i < size; i++) {
			sendUnsentMessage(toSent.get(i));
		}
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#logout()
	 */
	public void logout() {
		loggedUser = null;
		twitter = null;
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#updateStatus(java.lang.String)
	 */
	public TwitterStatus updateStatus(String text) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatus(twitter.updateStatus(text));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}		
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#replay(java.lang.String, long)
	 */
	public void replay(String text, long replayToId) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.updateStatus(text, replayToId);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}		
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#publicReplay(java.lang.String)
	 */
	public void publicReplay(String text) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.updateStatus(text);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}		
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#sendDirectMessage(java.lang.String, java.lang.String)
	 */
	public void sendDirectMessage(String username, String text) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.sendDirectMessage(username, text);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}		
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#favorite(long)
	 */
	public void favorite(long id) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.createFavorite(id);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#unfavorite(long)
	 */
	public void unfavorite(long id) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.destroyFavorite(id);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#follow(java.lang.String)
	 */
	public void follow(String username) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.createFriendship(username, true);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#unfollow(java.lang.String)
	 */
	public void unfollow(String username) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.destroyFriendship(username);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}

	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getLoggedUser()
	 */
	public TwitterUser getLoggedUser() {
		return loggedUser;
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#deleteStatus(long)
	 */
	public void deleteStatus(long id) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.destroyStatus(id);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#deleteDirectMessage(int)
	 */
	public void deleteDirectMessage(int id) throws YFrogTwitterException {
		checkCreated();
		try {
			twitter.destroyDirectMessage(id);
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getSavedSearches()
	 */
	public ArrayList<TwitterSavedSearch> getSavedSearches() throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getSavedSearches(twitter.getSavedSearches());
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}				
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#addSavedSearch(java.lang.String)
	 */
	public TwitterSavedSearch addSavedSearch(String query) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getSavedSearch(twitter.createSavedSearch(query));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}				
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#deleteSavedSearch(int)
	 */
	public TwitterSavedSearch deleteSavedSearch(int id) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getSavedSearch(twitter.destroySavedSearch(id));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}				
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#search(java.lang.String, int, int)
	 */
	public TwitterQueryResult search(String query, int page, int count) throws YFrogTwitterException {
		checkCreated();
		try {
			Query q = new Query(query);
			q.setPage(page);
			q.setRpp(count);
			return Twitter4jHelper.getQueryResult(twitter.search(q));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
		}				
	}
	
	/* (non-Javadoc)
	 * @see com.codeminders.yfrog.android.controller.service.TwitterService#getStatus(long)
	 */
	public TwitterStatus getStatus(long id) throws YFrogTwitterException {
		checkCreated();
		try {
			return Twitter4jHelper.getStatus(twitter.showStatus(id));
		} catch (TwitterException e) {
			throw new YFrogTwitterException(e, e.getStatusCode());
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
	
	static Paging createPaging(int page, int count) {
		return new Paging(page, count);
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
	

	// TODO May be we need use Arrays.sort() and Arrays.binarySearch()
	static ArrayList<TwitterUser> getUsers(List<User> users, IDs followers, IDs followings) {
		HashSet<Integer> followersIDs = toSet(followers);
		HashSet<Integer> followingsIDs = toSet(followings);
		boolean follower = false;
		boolean following = false;
		
		int size = users.size();
		ArrayList<TwitterUser> result = new ArrayList<TwitterUser>();
		
		for (int i = 0; i < size; i++) {
			User user = users.get(i);
			follower = followersIDs == null ? true : followersIDs.contains(user.getId());
			following = followingsIDs == null ? true : followingsIDs.contains(user.getId()); 
			result.add(Twitter4jHelper.getUser(user, follower, following));
		}
		
		return result;
	}

	@Deprecated
	static TwitterUser getUser(User user) {
		return getUser(user, false, false);
	}

	static TwitterUser getUser(User user, boolean follower, boolean following) {
		TwitterUser u = new TwitterUser();
		
		u.setId(user.getId());
		u.setFullname(user.getName());
		u.setProfileImageURL(user.getProfileImageURL());
		u.setUsername(user.getScreenName());
		u.setLocation(user.getLocation());
		u.setDescription(user.getDescription());
		u.setFollower(follower);
		u.setFollowing(following);
		
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

	static ArrayList<TwitterSavedSearch> getSavedSearches(List<SavedSearch> s) {
		ArrayList<TwitterSavedSearch> result = new ArrayList<TwitterSavedSearch>();
		
		int size = s.size();
		
		for (int i = 0; i < size; i++) {
			result.add(getSavedSearch(s.get(i)));
		}
		
		return result;
	}

	static TwitterSavedSearch getSavedSearch(SavedSearch ss) {
		TwitterSavedSearch result = new TwitterSavedSearch();
		
		result.setId(ss.getId());
		result.setName(ss.getName());
		result.setQuery(ss.getQuery());
		return result;
	}
	
	static TwitterQueryResult getQueryResult(QueryResult qr) {
		TwitterQueryResult result = new TwitterQueryResult();
		result.setQuery(qr.getQuery());
		result.setResults(getSearchResults(qr.getTweets()));
		
		return result;
	}
	
	static ArrayList<TwitterSearchResult> getSearchResults(List<Tweet> tweets) {
		ArrayList<TwitterSearchResult> result = new ArrayList<TwitterSearchResult>();
		
		int size = tweets.size();
		
		for (int i = 0; i < size; i++) {
			result.add(getSearchResult(tweets.get(i)));
		}
		
		return result;
	}
	
	static TwitterSearchResult getSearchResult(Tweet tweet) {
		TwitterSearchResult result = new TwitterSearchResult();
		result.setId(tweet.getId());
		result.setFromUser(tweet.getFromUser());
		result.setProfileImageUrl(tweet.getProfileImageUrl());
		result.setText(tweet.getText());
		result.setCreatedAt(tweet.getCreatedAt());
		
		return result;
	}
	
	// TODO May be we need use Arrays.sort() and Arrays.binarySearch() 
	private static HashSet<Integer> toSet(IDs ids) {
		if (ids == null) {
			return null;
		}
		
		int[] temp = ids.getIDs();
		
		HashSet<Integer> result = new HashSet<Integer>(temp.length);
		
		for (int i = 0; i < temp.length; i++) {
			result.add(temp[i]);
		}
		
		return result;
	}
}