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
	private LocationListener locationListener;
	private static Location location;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 1 minute
	boolean canGetLocation = false;
	double lat; // latitude
	double lon; // longitude

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testinggps);
		manager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		location = getLocation();
		TextView view = (TextView) findViewById(R.id.Coords);
		// view.setText(location.getLongitude() + ", " +
		// location.getLatitude());
		view.setText(location.getLatitude() + ", " + location.getLongitude());
		Button b = (Button) findViewById(R.id.refresh);
		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getLocation();
			}
		});
	}

	// starts the process getting the location of the users
	public void startLocationUpdates() {
		location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				locationListener);
	}

	// start getting location from GPS providers to detect the desired location

	public void stopLocationUpdates() {
		manager.removeUpdates(locationListener);
	}

	public Location getLocation() {
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
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						manager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS", "GPS Enabled");
					}
					if (manager != null) {
						location = manager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						Log.d("GPS", "Got Position");
						if (location == null)
							Log.d("GPS", "location null");
						if (location != null) {
							Log.d("GPS", "Started change");
							lat = location.getLatitude();
							lon = location.getLongitude();
							Log.d("GPS", "Finished change");
							Log.d("GPS", "Changed GPS Coords");
						}
						Log.d("GPS", "Checking");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
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

	public static Location getLocation1() {
		return location;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		Log.d("GPS", "Changed GPS Location");
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}
