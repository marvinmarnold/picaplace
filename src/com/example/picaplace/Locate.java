package com.example.picaplace;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class Locate extends Activity {
	private LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	private LocationListener locationListener;
	private static Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testinggps);
		// location =
		// manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		// TextView view = (TextView) findViewById(R.id.Coords);
		// view.setText(location.getLongitude() + ", " +
		// location.getLatitude());
	}

	// starts the process getting the location of the users
	public void startLocationUpdates() {
		location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locationListener = new LocationListener() {
			private static final int TWO_MINUTES = 1000 * 60 * 2;

			public void onLocationChanged(Location location) {
				makeUseOfNewLocation(location);
			}

			private void makeUseOfNewLocation(Location location) {
				if (isBetterLocation(location, Locate.location))
					Locate.setLocation(location);
				Log.d("Test", "Location set");
			}

			private boolean isBetterLocation(Location location,
					Location bestLocation) {
				// Check whether the new location fix is newer or older
				long timeDelta = location.getTime() - bestLocation.getTime();
				boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
				boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
				boolean isNewer = timeDelta > 0;
				// If it's been more than two minutes since the current
				// location, use the new location
				// because the user has likely moved
				if (isSignificantlyNewer) {
					return true;
					// If the new location is more than two minutes older, it
					// must be worse
				} else if (isSignificantlyOlder) {
					return false;
				}
				// Check if the old and new location are from the same provider
				boolean isFromSameProvider = isSameProvider(
						location.getProvider(), bestLocation.getProvider());
				// Check whether the new location fix is more or less accurate
				int accuracyDelta = (int) (location.getAccuracy() - bestLocation
						.getAccuracy());
				boolean isLessAccurate = accuracyDelta > 0;
				boolean isMoreAccurate = accuracyDelta < 0;
				boolean isSignificantlyLessAccurate = accuracyDelta > 200;
				// Determine location quality using a combination of timeliness
				// and accuracy
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

			private boolean isSameProvider(String provider1, String provider2) {
				if (provider1 == null) {
					return provider2 == null;
				}
				return provider1.equals(provider2);
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
		// when the location of the user changes it starts the process of
		// listening to detect the new location and to make use of it to provide
		// the requested data
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locationListener);
	}

	// start getting location from GPS providers to detect the desired location

	public void stopListening() {
		manager.removeUpdates(locationListener);
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

	public LocationListener getLocationListener() {
		return locationListener;
	}

	public void setLocationListener(LocationListener locationListener) {
		this.locationListener = locationListener;
	}

	public static Location getLocation() {
		return location;
	}

}
