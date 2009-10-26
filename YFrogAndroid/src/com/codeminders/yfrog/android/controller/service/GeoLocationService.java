/**
 * 
 */
package com.codeminders.yfrog.android.controller.service;

import java.io.IOException;
import java.util.*;

import com.codeminders.yfrog.android.util.StringUtils;

import android.content.Context;
import android.location.*;
import android.os.Bundle;

/**
 * @author idemydenko
 *
 */
public class GeoLocationService implements LocationListener {
	private Context context;
	private LocationManager locationManager;
	private Location loc = null;
	
	GeoLocationService(Context ctx) {
		context = ctx;
		locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, 1000f, this);
	}
	
	public Location getLocation() {
		return loc;
	}

	public String getLocationAddress() {
		if (loc == null) {
			return StringUtils.EMPTY_STRING;
		}
		Geocoder geocoder = new Geocoder(context);
		List<Address> addresses = null;
		
		try {
			addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
		} catch (IOException e) {
			return StringUtils.formatLocation(loc);
		}
		
		if (addresses == null || addresses.size() == 0) {
			return StringUtils.formatLocation(loc);
		}
		return addresses.get(0).toString();
	}

	@Override
	public void onLocationChanged(Location location) {
		loc = location;
	}
	
	@Override
	public void onProviderDisabled(String provider) {
	}
	
	@Override
	public void onProviderEnabled(String provider) {
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
