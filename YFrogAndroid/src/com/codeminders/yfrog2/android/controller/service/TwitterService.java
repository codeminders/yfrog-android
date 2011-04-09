/**
 * 
 */
package com.codeminders.yfrog2.android.controller.service;

import java.util.ArrayList;

import android.content.Context;

import com.codeminders.yfrog2.android.YFrogProperties;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.*;

/**
 * @author idemydenko
 * 
 */
public interface TwitterService {

	/*
	 * Desktop application tokens
	 * 
	 * private static final String CONSUMER_KEY = "Ai9SAL3FqA64k5uiY8ezA";
	 * private static final String CONSUMER_SECRET =
	 * "Piy2dJzdFVUMdUqrRLBUfkW2VcTnWnr2tnO6vHrZ2k";
	 */

	// static final String CONSUMER_KEY = "16F75LNJxjKTIUHidy5Sg";
	// static final String CONSUMER_SECRET =
	// "Sp3gGl1RvWtICmphby4MAomRCTj9sGvcE8b7XqUxxnQ";
	static final String CONSUMER_KEY = YFrogProperties.getProperies()
			.getConsumerKey();
	static final String CONSUMER_SECRET = YFrogProperties.getProperies()
			.getConsumerSecret();

	public static final String CALL_BACK_URL = "yfrog://android";
	public static final String PARAM_TOKEN = "oauth_token";
	public static final String PARAM_VERIFIER = "oauth_verifier";

	public static final String SOURCE = "YFrog";

	public void setLoggedAccount(Account acc);

	public abstract void login(String nickname, String password)
			throws YFrogTwitterException;

	public abstract String getOAuthWebAuthorizationURL(Account account)
			throws YFrogTwitterException;

	public abstract void verifyOAuthAuthorization(Account account, String pin)
			throws YFrogTwitterException;

	public abstract void loginOAuth(String oauthTolken, String oauthSecretTolken)
			throws YFrogTwitterException;

	public abstract void checkLogged() throws YFrogTwitterException;

	public abstract TwitterUser getCredentials(Account account)
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterStatus> getMentions(int page, int count)
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterStatus> getHomeStatuses(int page, int count)
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterDirectMessage> getDirectMessages()
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterUser> getFollowers()
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterUser> getUserFollowers(String username)
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterUser> getFollowings()
			throws YFrogTwitterException;

	public abstract boolean isFollower(long userId)
			throws YFrogTwitterException;

	public abstract boolean isFollowing(long userId)
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterStatus> getMyTweets(int page, int count)
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterStatus> getUserTweets(String username,
			int page, int count) throws YFrogTwitterException;

	public abstract TwitterUser getUser(String username)
			throws YFrogTwitterException;

	public abstract void sendUnsentMessage(UnsentMessage message)
			throws YFrogTwitterException;

	public abstract void sendAllUnsentMessages(Context context)
			throws YFrogTwitterException;

	public abstract void logout();

	public abstract TwitterStatus updateStatus(String text)
			throws YFrogTwitterException;

	public abstract void replay(String text, long replayToId)
			throws YFrogTwitterException;

	public abstract void publicReplay(String text) throws YFrogTwitterException;

	public abstract void sendDirectMessage(String username, String text)
			throws YFrogTwitterException;

	public abstract TwitterStatus favorite(long id)
			throws YFrogTwitterException;

	public abstract TwitterStatus unfavorite(long id)
			throws YFrogTwitterException;

	public abstract void follow(String username) throws YFrogTwitterException;

	public abstract void unfollow(String username) throws YFrogTwitterException;

	public abstract TwitterUser getLoggedUser();

	public abstract void deleteStatus(long id) throws YFrogTwitterException;

	public abstract void deleteDirectMessage(int id)
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterSavedSearch> getSavedSearches()
			throws YFrogTwitterException;

	public abstract TwitterSavedSearch addSavedSearch(String query)
			throws YFrogTwitterException;

	public abstract TwitterSavedSearch deleteSavedSearch(int id)
			throws YFrogTwitterException;

	public abstract TwitterQueryResult search(String query, int page, int count)
			throws YFrogTwitterException;

	public abstract TwitterStatus getStatus(long id)
			throws YFrogTwitterException;

	public abstract boolean isNotificationEnabled(String username)
			throws YFrogTwitterException;

	public abstract TwitterUser enableNotification(String username)
			throws YFrogTwitterException;

	public abstract TwitterUser disableNotification(String username)
			throws YFrogTwitterException;
}