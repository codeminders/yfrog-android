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
	
	private static final HashMap<String, Object> cache = new HashMap<String, Object>();
	
	private ServiceFactory() {
	}
	
	public static AccountService getAccountService() {
		if (!cache.containsKey(ACCOUNT_SERVICE_NAME)) {
			Object service = new AccountService();
			cache.put(ACCOUNT_SERVICE_NAME, service);
			return (AccountService) service;
		} 
		return (AccountService) cache.get(ACCOUNT_SERVICE_NAME);
	}
	
	public static TwitterService getTwitterService() {
		if (!cache.containsKey(TWITTER_SERVICE_NAME)) {
			Object service = new TwitterService();
			cache.put(TWITTER_SERVICE_NAME, service);
			return (TwitterService) service;
		} 
		return (TwitterService) cache.get(TWITTER_SERVICE_NAME);
	}
}
