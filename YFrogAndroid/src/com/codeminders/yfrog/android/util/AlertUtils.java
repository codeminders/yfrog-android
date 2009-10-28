/**
 * 
 */
package com.codeminders.yfrog.android.util;

import android.app.*;
import android.content.DialogInterface;

import com.codeminders.yfrog.android.*;

/**
 * @author idemydenko
 *
 */
public final class AlertUtils {
	public static final int ALERT_TWITTER_ERROR = 0x000fff;
	public static final int ALERT_TWITTER_UNKNOWN_ERROR = -1;
    private static final int OK = 200;// OK: Success!
    private static final int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    private static final int BAD_REQUEST = 400;// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    private static final int NOT_AUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    private static final int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    private static final int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    private static final int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Twitter team can investigate.
    private static final int BAD_GATEWAY = 502;// Bad Gateway: Twitter is down or being upgraded.
    private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.

	
	private AlertUtils() {
	}
	
	public static AlertDialog createTwitterErrorAlert(Activity activity, YFrogTwitterException e) {
		String message = activity.getResources().getString(R.string.twitter_error_msg) + e.getErrorCode();
		return new AlertDialog.Builder(activity)
		.setTitle(R.string.twitter_error_title)
		.setMessage(message)
		.setNeutralButton(R.string.twitter_error_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.create();
	}
}
