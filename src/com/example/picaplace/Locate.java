package com.example.picaplace;

import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

public class Locate extends Activity implements LocationListener {
	private LocationManager manager;
	public static Location location;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	boolean canGetLocation = false;

	// private TextView view;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instagram);
		manager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		getLocation();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String json;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		if (location != null) {
			json = InstagramGet.getJson(location.getLatitude(),
					location.getLongitude()); // Add lat + long here
		} else {
			Log.d("PicABug", "Loc Is Null");
			json = InstagramGet.getJson();
		}
		Log.d("PicAJson", json);
		InstagramGet.testJson(json);
		// Load image by url
		GridLayout rel = (GridLayout) findViewById(R.id.grid);
		Log.d("PicAGrid", "Grid Layout");
		try {
			String[] pics = InstagramGet.getImageUrl(json);
			Log.d("PicAGrid", "Pics ");
			for (int i = 0; i < pics.length; i++) {
				Log.d("PicAGrid", "Loop " + i);
				ImageView mciv = new ImageView(this);
				// Log.d("PicAGrid", "new Imageview");
				mciv.setId(i + 10);
				// Log.d("PicAGrid", "set id");
				LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				// Log.d("PicAGrid", "Loading params");
				mciv.setLayoutParams(p);
				// Log.d("PicAGrid", "Set Params");
				URL thumb_u = new URL(pics[i]);
				// Log.d("PicAGrid", "New URL");
				Drawable thumb_d = Drawable.createFromStream(
						thumb_u.openStream(), "src");
				// Log.d("PicAGrid", "Drawable Thumb");
				mciv.setImageDrawable(thumb_d);
				// Log.d("PicAGrid", "Setting drawable");
				rel.addView(mciv, 230 / 4 * 3, 250);
				// Log.d("PicAGrid", "Adding to view");
			}
			rel.addView(new ImageView(this));
			Button ref = new Button(this);
			// Log.d("PicAGrid", "new Button");
			ref.setText("Refresh");
			// Log.d("PicAGrid", "Refreshing");
			ref.setId(9999);
			// Log.d("PicAGrid", "setting id");
			ref.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					getLocation();
				}
			});
			// Log.d("PicAGrid", "Listener");
			rel.addView(ref);
			// Log.d("PicAGrid", "Adding view");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("PicAGrid", "Images crashed " + e.toString());
		}

		Log.i("PicAMain", "Program ended");
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
			//
			// getting GPS status
			boolean isGPSEnabled = manager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = manager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				Log.e("PicALoc", "Location or network are off");
			} else {
				if (isNetworkEnabled) {
					Log.d("PicABug", "Network Enabled");
					manager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					Log.d("PicABug", "GPS Enabled");
					manager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				}
			}
			onLocationChanged(manager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		} catch (Exception e) {
			Log.d("PicALoc", "Get Location crashed");
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
		Log.d("PicABug", "changed location");
		// if (location != null) {
		// view.setText(location.getLongitude() + ", "
		// + location.getLatitude());
		// Log.d("GPS", "Location is: " + location);
		// }
		// if (location == null)
		// view.setText("Location is null");
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