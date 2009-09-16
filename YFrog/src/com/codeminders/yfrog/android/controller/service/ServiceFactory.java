/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.util.HashMap;


/**
 * @author idemydenko
 *
 */
public class ServiceFactory {
	private static final String LOGIN_SERVICE_NAME = "loginService";
	
	private static final HashMap<String, Object> cache = new HashMap<String, Object>();
	
	private ServiceFactory() {
	}
	
	public static AccountService getAccountService() {
		if (!cache.containsKey(LOGIN_SERVICE_NAME)) {
			Object service = new AccountService();
			cache.put(LOGIN_SERVICE_NAME, service);
			return (AccountService) service;
		} 
		return (AccountService) cache.get(LOGIN_SERVICE_NAME);
	}
}
