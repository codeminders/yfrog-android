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
		account.setOauthToken("key");
		
		long id = accountDAO.addAccount(account);
		
		assertTrue(id > 0);
		System.out.println(id);
	}
	
	public void testGetAccount() throws Exception {
		int id = 1;
		
		Account account = accountDAO.getAccount(id);
		
		assertNotNull(account);
	}
	
	public void testUpdateAccount() throws Exception {
		final int id = 1;
		final String nickname = "NewUser";
		final String password = "123456";
		final String email = "1@1.com";
		final String oauthKey = "yek";
		
		Account account = new Account();
		account.setId(id);
		account.setNickname(nickname);
		account.setPassword(password);
		account.setEmail(email);
		account.setOauthToken(oauthKey);
		
		accountDAO.updateAccount(account);
		
		Account account2 = accountDAO.getAccount(id);
		
		assertEquals(account2.getNickname(), nickname);
		assertEquals(account2.getPassword(), password);
		assertEquals(account2.getEmail(), email);
		assertEquals(account2.getOauthToken(), oauthKey);
	}
	
	public void testIsAccountUnique() throws Exception {
		Account account = new Account();
		account.setNickname("Nickname");
		account.setPassword("password");
		account.setEmail("mail");
		account.setOauthToken("key");
		
		boolean b = accountDAO.isAccountUnique(account);
		
		assertFalse(b);
		
		account.setId(23);
		b = accountDAO.isAccountUnique(account);
		
		assertTrue(b);
	}
}
