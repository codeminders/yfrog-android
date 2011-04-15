/**
 * 
 */
package com.codeminders.yfrog2.android.controller.service.twitter4j;

import twitter4j.TwitterException;
import twitter4j.auth.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author idemydenko
 *
 */
public class Twitter extends twitter4j.Twitter {
	
	public Twitter() {
		super();
	}
	
	public Twitter(String username, String password) {
		super(username, password);
	}
	
	public boolean isNotificationEnabled(String username) throws TwitterException {
		Response response = get(getBaseURL() + "friendships/show.json", "target_screen_name", username, true);
		
		JSONObject json = response.asJSONObject();
		try {
			if (json.isNull("relationship")) {
				throw new TwitterException("Invalid response format");
			}
			
			JSONObject relationship = json.getJSONObject("relationship");
			
			if (relationship.isNull("source")) {
				throw new TwitterException("Invalid response format");
			}
			
			return relationship.getJSONObject("source").getBoolean("notifications_enabled");
		} catch (JSONException jsone) {
			throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
		} catch (TwitterException e) {
			throw new TwitterException("Invalid response format", -1);
		}
	}

}
