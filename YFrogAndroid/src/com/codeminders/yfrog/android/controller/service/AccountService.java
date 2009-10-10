/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.ArrayList;

import android.net.Uri;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.controller.dao.AccountDAO;
import com.codeminders.yfrog.android.controller.dao.DAOFactory;
import com.codeminders.yfrog.android.model.Account;

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
		return accountDAO.getAllAccounts();
	}
	
	public Account getAccount(long id) {
		return accountDAO.getAccount(id);
	}
	
	public Account addAccount(Account account) {
		long id = accountDAO.addAccount(account);
		
		return accountDAO.getAccount(id);
	}
	
	public void deleteAccount(Account account) {
		accountDAO.deleteAccount(account.getId());
	}
	
	public void updateAccount(Account account) {
		accountDAO.updateAccount(account);
	}
	
	public boolean isAccountUnique(Account account) {
		return accountDAO.isAccountUnique(account);
	}
	
	public String getOAuthAuthorizationURL(Account account) throws YFrogTwitterException {
		String url = twitterService.getOAuthWebAuthorizationURL(account);
		account.setOauthStatus(Account.OAUTH_STATUS_WAIT_VERIFICATION);
		updateAccount(account);

		return url;
	}
	
	public void verifyOAuthAuthorization(Account account, String pin) throws YFrogTwitterException {
		twitterService.verifyOAuthAuthorization(account, pin);
		account.setOauthStatus(Account.OAUTH_STATUS_VERIFIED);
		updateAccount(account);
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
			twitterService.login(account.getNickname(), account.getPassword());
		} else {
			twitterService.loginOAuth(account.getOauthToken(), account.getOauthTokenSecret());
		}
		logged = account;
	}
	
	public Account getLogged() {
		return logged;
	}
	
	public void logout() {
		
	}
}