/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.io.IOException;

import com.codeminders.yfrog.android.YFrogTwitterException;
import com.codeminders.yfrog.android.model.MessageAttachment;
import com.codeminders.yfrog.android.util.StringUtils;
import com.codeminders.yfrog.client.YFrogClient;
import com.codeminders.yfrog.client.impl.YFrogClientImpl;
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

		request.setUsername(accountService.getLogged().getUsername());
		request.setPassword(accountService.getLogged().getPassword());
		
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

		request.setUsername(accountService.getLogged().getUsername());
		request.setPassword(accountService.getLogged().getPassword());
		
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

}
