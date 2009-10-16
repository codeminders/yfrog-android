/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.ArrayList;

import android.net.Uri;

import com.codeminders.yfrog.android.YFrogTwitterAuthException;
import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.dao.AccountDAO;
import com.codeminders.yfrog.android.controller.dao.DAOFactory;
import com.codeminders.yfrog.android.model.Account;
import com.codeminders.yfrog.android.model.TwitterUser;
import com.codeminders.yfrog.android.util.StringUtils;

/**
 * @author idemydenko
 *
 */
public final class AccountService {
	private Account logged;
	
	private AccountDAO accountDAO;
	
	private TwitterService twitterService;
	
	AccountService() {
		accountDAO = DAOFactory.getAccountDAO();
		twitterService = ServiceFactory.getTwitterService();
	}
	
	public ArrayList<Account> getAllAccounts() {
		accountDAO.deleteEmptyAccounts();
		return accountDAO.getAllAccounts();
	}
	
	public Account getAccount(long id) {
		return accountDAO.getAccount(id);
	}
	
	public Account addAccount(Account account) throws YFrogTwitterException {
		if (account.getAuthMethod() == Account.METHOD_COMMON) {
			verifiyAccount(account);
		}
		
		long id = accountDAO.addAccount(account);
		
		return accountDAO.getAccount(id);
	}
	
	public void deleteAccount(Account account) {
		accountDAO.deleteAccount(account.getId());
	}
	
	public void updateAccount(Account account)  throws YFrogTwitterException {
		if (account.getAuthMethod() == Account.METHOD_COMMON) {
			verifiyAccount(account);
		}
		accountDAO.updateAccount(account);
	}
	
	private void verifiyAccount(Account account) throws YFrogTwitterException {
		twitterService.getCredentials(account);
	}
	
	public boolean isAccountUnique(Account account) {
		return accountDAO.isAccountUnique(account);
	}
	
	public String getOAuthAuthorizationURL(Account account) throws YFrogTwitterException {
		accountDAO.resetWatingForOAuthVerificationAccounts();
		String url = twitterService.getOAuthWebAuthorizationURL(account);
		account.setOauthStatus(Account.OAUTH_STATUS_WAIT_VERIFICATION);
		accountDAO.updateAccount(account);

		return url;
	}
	
	public void verifyOAuthAuthorization(Account account, String pin) throws YFrogTwitterException {
		twitterService.verifyOAuthAuthorization(account, pin);
		account.setOauthStatus(Account.OAUTH_STATUS_VERIFIED);
		TwitterUser user = twitterService.getCredentials(account);
		
		String currentUsername = account.getUsername();
		String receivedUsername = user.getUsername();
		
		if (StringUtils.isEmpty(currentUsername)) {
			account.setUsername(receivedUsername);
			
			if (!isAccountUnique(account)) {
				deleteAccount(account);
				throw new YFrogTwitterAuthException();
			}
		} else if (!currentUsername.equalsIgnoreCase(receivedUsername)) { // TODO Twitter usernames aren't case sensitivity
			resetOAuth(account);
			throw new YFrogTwitterAuthException();
		}
		
		updateAccount(account);
	}
	
	private void resetOAuth(Account account) {
		account.setOauthToken(StringUtils.EMPTY_STRING);
		account.setOauthTokenSecret(StringUtils.EMPTY_STRING);
		account.setOauthStatus(Account.OAUTH_STATUS_NOT_AUTHORIZED);
		account.setAuthMethod(Account.METHOD_COMMON);
		accountDAO.updateAccount(account);
	}
	
	public Account getWatingForOAuthVerificationAccount() {
		return accountDAO.getWatingForOAuthVerificationAccount();
	}
	
	public boolean isOAuthCallback(Uri uri) {
		if (uri == null) {
			return false;
		}
		
		return uri.toString().startsWith(TwitterService.CALL_BACK_URL);
	}
	
	public String getToken(Uri uri) {
		return uri.getQueryParameter(TwitterService.PARAM_TOKEN);
	}

	public String getVerifier(Uri uri) {
		return uri.getQueryParameter(TwitterService.PARAM_VERIFIER);
	}

	public void login(Account account) throws YFrogTwitterException {
		if (account == null) {
			throw new IllegalArgumentException("Account can't be null");
		}
		
		if (account.getAuthMethod() == Account.METHOD_COMMON) {
			twitterService.login(account.getUsername(), account.getPassword());
		} else {
			twitterService.loginOAuth(account.getOauthToken(), account.getOauthTokenSecret());
		}
		logged = account;
	}
	
	public Account getLogged() {
		return logged;
	}
}