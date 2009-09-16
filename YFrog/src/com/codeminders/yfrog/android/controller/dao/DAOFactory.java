/**
 * 
 */
package com.codeminders.yfrog.android.controller.dao;

import java.util.HashMap;

/**
 * @author idemydenko
 *
 */
public class DAOFactory {
	private static final String LOGIN_DAO_NAME = "accountDao";
	
	private static final HashMap<String, Object> cache = new HashMap<String, Object>();
	
	private DAOFactory() {
	}
	
	public static AccountDAO getAccountDAO() {
		if (!cache.containsKey(LOGIN_DAO_NAME)) {
			Object dao = new AccountDAO();
			cache.put(LOGIN_DAO_NAME, dao);
			return (AccountDAO) dao;
		} 
		return (AccountDAO) cache.get(LOGIN_DAO_NAME);
	}

}
