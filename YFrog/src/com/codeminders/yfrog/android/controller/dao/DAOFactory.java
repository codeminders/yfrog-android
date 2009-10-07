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
	private static final String ACCOUNT_DAO_NAME = "accountDao";
	private static final String UNSENT_MESSAGE_DAO_NAME = "unsentMessageDao";
	
	private static final HashMap<String, Object> cache = new HashMap<String, Object>();
	
	private DAOFactory() {
	}
	
	public static AccountDAO getAccountDAO() {
		if (!cache.containsKey(ACCOUNT_DAO_NAME)) {
			Object dao = new AccountDAO();
			cache.put(ACCOUNT_DAO_NAME, dao);
			return (AccountDAO) dao;
		} 
		return (AccountDAO) cache.get(ACCOUNT_DAO_NAME);
	}

	public static UnsentMessageDAO getUnsentMessageDAO() {
		if (!cache.containsKey(UNSENT_MESSAGE_DAO_NAME)) {
			Object dao = new UnsentMessageDAO();
			cache.put(UNSENT_MESSAGE_DAO_NAME, dao);
			return (UnsentMessageDAO) dao;
		} 
		return (UnsentMessageDAO) cache.get(UNSENT_MESSAGE_DAO_NAME);
	}

}
