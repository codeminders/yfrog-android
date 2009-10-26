/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.ArrayList;

import android.content.Context;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.*;

/**
 * @author idemydenko
 *
 */
public interface TwitterService {

	public static final String CALL_BACK_URL = "yfrog://android";
	public static final String PARAM_TOKEN = "oauth_token";
	public static final String PARAM_VERIFIER = "oauth_verifier";

	public void setLoggedAccount(Account acc);
	
	public abstract void login(String nickname, String password)
			throws YFrogTwitterException;

	public abstract String getOAuthWebAuthorizationURL(Account account)
			throws YFrogTwitterException;

	public abstract void verifyOAuthAuthorization(Account account, String pin)
			throws YFrogTwitterException;

	public abstract void loginOAuth(String oauthTolken, String oauthSecretTolken)
			throws YFrogTwitterException;

	// TODO Can we move logged user to other method
	public abstract boolean isLogged();

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

	public abstract ArrayList<TwitterStatus> getMyTweets(int page, int count)
			throws YFrogTwitterException;

	public abstract ArrayList<TwitterStatus> getUserTweets(String username,
			int page, int count) throws YFrogTwitterException;

	public abstract void sendUnsentMessage(UnsentMessage message)
			throws YFrogTwitterException;

	public void sendUnsentMessage(UnsentMessage message, MessageAttachment attachment) 
			throws YFrogTwitterException;
	
	public abstract void sendAllUnsentMessages(Context context) throws YFrogTwitterException;

	public abstract void logout();

	public abstract TwitterStatus updateStatus(String text)
			throws YFrogTwitterException;

	public abstract void replay(String text, long replayToId)
			throws YFrogTwitterException;

	public abstract void publicReplay(String text) throws YFrogTwitterException;

	public abstract void sendDirectMessage(String username, String text)
			throws YFrogTwitterException;

	public abstract void favorite(long id) throws YFrogTwitterException;

	public abstract void unfavorite(long id) throws YFrogTwitterException;

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

}