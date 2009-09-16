/**
 * 
 */
package com.codeminders.yfrog.android.test.controller.dao;

import java.util.List;

import com.codeminders.yfrog.android.controller.dao.AbstractDAO;
import com.codeminders.yfrog.android.controller.dao.AccountDAO;
import com.codeminders.yfrog.android.controller.dao.DAOFactory;
import com.codeminders.yfrog.android.controller.dao.db.DatabaseHelper;
import com.codeminders.yfrog.android.model.Account;

import android.test.AndroidTestCase;

/**
 * @author idemydenko
 *
 */
public class AccountDAOTest extends AndroidTestCase {
	
	AccountDAO accountDAO;

	public AccountDAOTest() {
		accountDAO = DAOFactory.getAccountDAO();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DatabaseHelper.init(getContext());
	}
	
	public void testGetAllAccounts() throws Exception {
		List<Account> list = accountDAO.getAllAccounts();
		assertTrue(list.size() > 0);
	}
	
	public void testAddAccount() throws Exception {
		Account account = new Account();
		account.setNickname("Nickname");
		account.setPassword("password");
		account.setEmail("mail");
		account.setOauthKey("key");
		
		long id = accountDAO.addAccount(account);
		
		assertTrue(id > 0);
		System.out.println(id);
	}
	
	public void testGetAccount() throws Exception {
		int id = 1;
		
		Account account = accountDAO.getAccount(id);
		
		assertNotNull(account);
	}
}
