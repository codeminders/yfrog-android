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
	private static final String ACCOUNT_SERVICE_NAME = "loginService";
	private static final String TWITTER_SERVICE_NAME = "twitterService";
	private static final String UNSENT_MESSAGE_SERVICE_NAME = "unsentMessageService";
	
	private static final HashMap<String, Object> cache = new HashMap<String, Object>();
	
	private ServiceFactory() {
	}
	
	public static AccountService getAccountService() {
		if (!contains(ACCOUNT_SERVICE_NAME)) {
			Object service = new AccountService();
			put(ACCOUNT_SERVICE_NAME, service);
			return (AccountService) service;
		} 
		return (AccountService) get(ACCOUNT_SERVICE_NAME);
	}
	
	public static TwitterService getTwitterService() {
		if (!contains(TWITTER_SERVICE_NAME)) {
			Object service = new Twitter4JService();
			put(TWITTER_SERVICE_NAME, service);
			return (TwitterService) service;
		} 
		return (TwitterService) get(TWITTER_SERVICE_NAME);
	}

	public static UnsentMessageService getUnsentMessageService() {
		if (!contains(UNSENT_MESSAGE_SERVICE_NAME)) {
			Object service = new UnsentMessageService();
			put(UNSENT_MESSAGE_SERVICE_NAME, service);
			return (UnsentMessageService) service;
		}
		return (UnsentMessageService) get(UNSENT_MESSAGE_SERVICE_NAME);
	}
	
	private static Object get(String name) {
		return cache.get(name);
	}
	
	private static void put(String name, Object service) {
		cache.put(name, service);
	}
	
	private static boolean contains(String name) {
		return cache.containsKey(name);
	}
}
