/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.io.IOException;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.*;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.client.YFrogClient;
import com.codeminders.yfrog.client.impl.YFrogClientImpl;
import com.codeminders.yfrog.client.oauth.OAuthHelper;
import com.codeminders.yfrog.client.request.UploadRequest;
import com.codeminders.yfrog.client.response.*;

/**
 * @author idemydenko
 *
 */
public class YFrogService {
	private AccountService accountService;
	
	YFrogService() {
		accountService = ServiceFactory.getAccountService();
	}
	
	public long send(String text, MessageAttachment attachment) throws YFrogTwitterException {
		YFrogClient client = new YFrogClientImpl();
		UploadRequest request = attachment.toUploadRequest();

		prepareAuthentication(request);
		
		if (!StringUtils.isEmpty(text)) {
			request.setMessage(text);
		}
		
		UploadResponse response = null;
		
		try {
			response = client.uploadAndPost(request);
		} catch (IOException e) {
			// TODO
		} catch (UploadResponseFormatException e) {
			
		}
		
		if (response == null || UploadResponse.STATUS_FAIL.equals(response.getStatus())) {
			throw new YFrogTwitterException();
		}
		
		return response.getStatusId();
	}
	
	public String upload(MessageAttachment attachment) throws YFrogTwitterException {
		YFrogClient client = new YFrogClientImpl();
		UploadRequest request = attachment.toUploadRequest();

		prepareAuthentication(request);
		
		UploadResponse response = null;
		
		try {
			response = client.upload(request);
		} catch (IOException e) {
			// TODO
		} catch (UploadResponseFormatException e) {
			
		}
		
		if (response == null || UploadResponse.STATUS_FAIL.equals(response.getStatus())) {
			throw new YFrogTwitterException();
		}
		
		return response.getMediaUrl();
	}

	private void prepareAuthentication(UploadRequest request) {
		Account logged = accountService.getLogged();
		
		request.setUsername(logged.getUsername());
		if (logged.getAuthMethod() == Account.METHOD_COMMON) {
			request.setPassword(accountService.getLogged().getPassword());
		} else {
			String signedUrl = OAuthHelper.getOAuthVerifyUrl(logged.getOauthToken(), logged.getOauthTokenSecret(), 
					TwitterService.CONSUMER_KEY, TwitterService.CONSUMER_SECRET);
			request.setVerifyUrl(signedUrl);
		}
	}
}
