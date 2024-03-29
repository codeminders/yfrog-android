/**
 * 
 */
package com.codeminders.yfrog2.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.codeminders.yfrog2.android.R;
import com.codeminders.yfrog2.android.YFrogProperties;

/**
 * @author idemydenko
 *
 */
public final class AlertUtils {
	// Logout Alert
	public static final int LOGOUT = -100;
	
	// GPS Error code
	public static final int GPS_RETRIEVE_LOCATION_ERROR = -5;
	
	// Database error codes
	public static final int DB_ACCOUNT_INSERT_ERROR = -4;
	
	// App error codes
	public static final int ACCOUNT_VERIFICATION_ERROR = -3;
	public static final int IO_ERROR = -2;
	public static final int TWITTER_CONNECTION_ERROR = -1;
	
	// http://apiwiki.twitter.com/HTTP-Response-Codes-and-Errors
    public static final int NOT_MODIFIED = 304;// Not Modified: There was no new data to return.
    public static final int BAD_REQUEST = 400;// Bad Request: The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.
    public static final int NOT_AUTHORIZED = 401;// Not Authorized: Authentication credentials were missing or incorrect.
    public static final int FORBIDDEN = 403;// Forbidden: The request is understood, but it has been refused.  An accompanying error message will explain why.
    public static final int NOT_FOUND = 404;// Not Found: The URI requested is invalid or the resource requested, such as a user, does not exists.
    public static final int NOT_ACCEPTABLE = 406;// Not Acceptable: Returned by the Search API when an invalid format is specified in the request.
    public static final int INTERNAL_SERVER_ERROR = 500;// Internal Server Error: Something is broken.  Please post to the group so the Twitter team can investigate.
    public static final int BAD_GATEWAY = 502;// Bad Gateway: Twitter is down or being upgraded.
    public static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable: The Twitter servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.

    // http://code.google.com/p/imageshackapi/wiki/YFROGuploadAndPost
    public static final int YFROG_ERROR_1001 = 1001;// Invalid twitter username or password
    public static final int YFROG_ERROR_1002 = 1002;// Image/video not found
    public static final int YFROG_ERROR_1003 = 1003;// Unsupported image/video type
    public static final int YFROG_ERROR_1004 = 1004;// Image/video is too big
    public static final int YFROG_ERROR_2001 = 2001;// Invalid action
    public static final int YFROG_ERROR_2002 = 2002;// Failed to upload
    public static final int YFROG_ERROR_2003 = 2003;// Failed to update twitter status
    public static final int YFROG_ERROR_2004 = 2004;// Invalid developer key
    public static final int YFROG_ERROR_2005 = 2005;// Image/video is too big


	private AlertUtils() {
	}

	public static AlertDialog createLogoutAlert(final Activity activity) {
		return new AlertDialog.Builder(activity)
		.setTitle(R.string.lo_dialog_title)
		.setMessage(R.string.lo_dialog_msg)
		.setPositiveButton(R.string.lo_dialog_btn_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
                YFrogProperties.getProperies().delete("lastLogged");
				activity.finish();
			}
		})
		.setNegativeButton(R.string.lo_dialog_btn_no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.create();
	}

	public static AlertDialog createErrorAlert(Context context, int errorCode) {
		String message = getErrorMessage(context, errorCode);
		String title = getErrorTitle(context, errorCode);
		
		return new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setNeutralButton(R.string.error_alert_btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.create();
	}
	
	private static String getErrorMessage(Context context, int errorCode) {
		String msg = "";
		
		switch (errorCode) {
		case GPS_RETRIEVE_LOCATION_ERROR:
			msg = context.getResources().getString(R.string.gps_retrieve_location_error);
			break;
		case DB_ACCOUNT_INSERT_ERROR:
			msg = context.getResources().getString(R.string.account_db_insert_error);
			break;

		case ACCOUNT_VERIFICATION_ERROR:
			msg = context.getResources().getString(R.string.account_verification_error);
			break;

		case IO_ERROR:
			msg = context.getResources().getString(R.string.io_error);
			break;

		case TWITTER_CONNECTION_ERROR:
			msg = context.getResources().getString(R.string.twitter_error);
			break;

		case NOT_MODIFIED:
			msg = context.getResources().getString(R.string.error_304);
			break;

		case BAD_REQUEST:
			msg = context.getResources().getString(R.string.error_400);
			break;
		case NOT_AUTHORIZED:
			msg = context.getResources().getString(R.string.error_401);
			break;
		case FORBIDDEN:
			msg = context.getResources().getString(R.string.error_403);
			break;
		case NOT_FOUND:
			msg = context.getResources().getString(R.string.error_404);
			break;
		case NOT_ACCEPTABLE:
			msg = context.getResources().getString(R.string.error_406);
			break;
		case INTERNAL_SERVER_ERROR:
			msg = context.getResources().getString(R.string.error_500);
			break;
		case BAD_GATEWAY:
			msg = context.getResources().getString(R.string.error_502);
			break;
		case SERVICE_UNAVAILABLE:
			msg = context.getResources().getString(R.string.error_503);
			break;
		case YFROG_ERROR_1001:
			msg = context.getResources().getString(R.string.error_1001);
			break;
		case YFROG_ERROR_1002:
			msg = context.getResources().getString(R.string.error_1002);
			break;
		case YFROG_ERROR_1003:
			msg = context.getResources().getString(R.string.error_1003);
			break;
		case YFROG_ERROR_1004:
			msg = context.getResources().getString(R.string.error_1004);
			break;
		case YFROG_ERROR_2001:
			msg = context.getResources().getString(R.string.error_2001);
			break;
		case YFROG_ERROR_2002:
			msg = context.getResources().getString(R.string.error_2002);
			break;
		case YFROG_ERROR_2003:
			msg = context.getResources().getString(R.string.error_2003);
			break;
		case YFROG_ERROR_2004:
			msg = context.getResources().getString(R.string.error_2004);
			break;
		case YFROG_ERROR_2005:
			msg = context.getResources().getString(R.string.error_2005);
			break;

		}
		return msg;
	}
	
	private static String getErrorTitle(Context context, int errorCode) {
		if (errorCode > SERVICE_UNAVAILABLE) {
			return context.getResources().getString(R.string.yfrog_upload_error_title) + " (" + errorCode + ")";
		} else if (errorCode == IO_ERROR) {
			return context.getResources().getString(R.string.io_error_title);
		} else if (errorCode == ACCOUNT_VERIFICATION_ERROR) {
			return context.getResources().getString(R.string.account_verification_error_title);
		} else if (errorCode == DB_ACCOUNT_INSERT_ERROR) {
			return context.getResources().getString(R.string.database_error_title);
		} else if (errorCode == GPS_RETRIEVE_LOCATION_ERROR) {
			return context.getResources().getString(R.string.gps_error_title);
		} else {
			return context.getResources().getString(R.string.twitter_error_title) + " (" + errorCode + ")";
		}
	}
}
