/**
 *
 */
package com.codeminders.yfrog2.android.controller.service;

import java.io.IOException;

import android.location.Location;

import com.codeminders.yfrog.client.YFrogClient;
import com.codeminders.yfrog.client.impl.YFrogClientImpl;
import com.codeminders.yfrog.client.oauth.OAuthHelper;
import com.codeminders.yfrog.client.request.UploadRequest;
import com.codeminders.yfrog.client.response.UploadResponse;
import com.codeminders.yfrog.client.response.UploadResponseFormatException;
import com.codeminders.yfrog2.android.YFrogProperties;
import com.codeminders.yfrog2.android.YFrogTwitterException;
import com.codeminders.yfrog2.android.model.Account;
import com.codeminders.yfrog2.android.model.MessageAttachment;
import com.codeminders.yfrog2.android.util.StringUtils;

/**
 * @author idemydenko
 *
 */
public class YFrogService {
	private AccountService accountService;
	private GeoLocationService geoLocationService;
	private YFrogProperties properties;

	YFrogService() {
		accountService = ServiceFactory.getAccountService();
		geoLocationService = ServiceFactory.getGeoLocationService();
		properties = YFrogProperties.getProperies();
	}

	public long send(String text, MessageAttachment attachment) throws YFrogTwitterException {
        YFrogClient client;
        try{
            client = new YFrogClientImpl();
        } catch (Exception e) {
             throw new YFrogTwitterException("YFrogClientImpl initializer exception");
        }
		UploadRequest request = attachment.toUploadRequest();

		prepareAuthentication(request);
		request.setAzimuth(attachment.getAzimuth());
		request.setPitch(attachment.getPitch());
		request.setRoll(attachment.getRoll());

		if (!StringUtils.isEmpty(text)) {
			request.setMessage(text);
		}

		UploadResponse response = null;

		try {
			response = client.uploadAndPost(request);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UploadResponseFormatException e) {
			e.printStackTrace();
		}

		checkResponse(response);

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
			e.printStackTrace();
		} catch (UploadResponseFormatException e) {
			e.printStackTrace();
		}

		checkResponse(response);

		return response.getMediaUrl();
	}

	private void prepareAuthentication(UploadRequest request) {
		Account logged = accountService.getLogged();

		request.setUsername(logged.getUsername());
		String signedUrl = OAuthHelper.getOAuthVerifyUrl(logged.getOauthToken(), logged.getOauthTokenSecret(),
				TwitterService.CONSUMER_KEY, TwitterService.CONSUMER_SECRET);
		request.setVerifyUrl(signedUrl);

		if (logged.isPostLocation()) {
			Location location = geoLocationService.getLocation();

			if (location != null) {
				request.setTags(StringUtils.creatGeoTags(location.getLatitude(), location.getLongitude()));
			}
		}

		request.setKey(properties.getDeveloperKey());
//		System.out.println("Developer Key - " + request.getKey());
	}

	private void checkResponse(UploadResponse response) throws YFrogTwitterException {
        try {
		    if (UploadResponse.STATUS_FAIL.equals(response.getStatus())) {
			    throw new YFrogTwitterException(response.getErrorCode());
		    }
        } catch (NullPointerException e) {
            throw new YFrogTwitterException(-1);
        }
	}
}
