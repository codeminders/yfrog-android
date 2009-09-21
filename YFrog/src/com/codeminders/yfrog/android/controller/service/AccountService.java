/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.ArrayList;

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
	
	public Account getAccount(Integer id) {
		return accountDAO.getAccount(id);
	}
	
	public void addAccount(Account account) {
		accountDAO.addAccount(account);
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
	
	public void login(Account account) throws YFrogTwitterException {
		if (account == null) {
			throw new IllegalArgumentException("Account can't be null");
		}
		
		twitterService.login(account.getNickname(), account.getPassword());
		logged = account;
	}
	
	public Account getLogged() {
		return logged;
	}
	
	public void logout() {
		
	}
}