package com.example.picaplace;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Locate extends Activity implements LocationListener {
	private LocationManager manager;
	public static Location location;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 1 minute
	boolean canGetLocation = false;
	private TextView view;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testinggps);
		manager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		getLocation();
		view = (TextView) findViewById(R.id.Coords);
		Button b = (Button) findViewById(R.id.refresh);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getLocation();
			}
		});
	}

	// starts the process getting the location of the users
	public void startLocationUpdates() {
		location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	}

	// start getting location from GPS providers to detect the desired location

	public void stopLocationUpdates() {
		manager.removeUpdates(this);
	}

	public void getLocation() {
		try {
			manager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			boolean isGPSEnabled = manager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = manager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					manager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					manager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	// stops detecting location of the user after they finish navigating on the
	// application

	public static void setLocation(Location location) {
		Locate.location = location;
	}

	// changes the location of the user in the class

	public LocationManager getManager() {
		return manager;
	}

	public void setManager(LocationManager manager) {
		this.manager = manager;
	}

	public static Location getLocation1() {
		return location;
	}

	@Override
	public void onLocationChanged(Location loc) {
		if (isBetterLocation(loc, location))
			location = loc;
		if (location != null) {
			view.setText(location.getLongitude() + ", "
					+ location.getLatitude());
			Log.d("GPS", "Location is: " + location);
		}
		if (location == null)
			view.setText("Location is null");
		stopLocationUpdates();
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}
}