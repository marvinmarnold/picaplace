package com.example.picaplace;

import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
	private int start;
	private int end;
	private String json;
	public static Context context;
	public static GridLayout rel;

	// private TextView view;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instagram);
		// I dunno what it means but i have to put it here #lmao #Yolo #swag
		context = this;
		rel = (GridLayout) findViewById(R.id.grid);
		manager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		start = 0;
		end = 4;
		Button ref1 = (Button) findViewById(R.id.refButton);
		// Log.d("PicAGrid", "new Button");
		ref1.setText("Reset");
		// Log.d("PicAGrid", "Refreshing");
		// Log.d("PicAGrid", "setting id");
		ref1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getLocation();
				layoutUpdate();
			}
		});
		Button next = (Button) findViewById(R.id.nextButton);
		// Log.d("PicAGrid", "new Button");
		next.setText("next");
		// Log.d("PicAGrid", "Refreshing");
		// Log.d("PicAGrid", "setting id");
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getLocation();
				layoutUpdate();
			}
		});
		// Log.d("PicAGrid", "Listener");
		// Log.d("PicAGrid", "Here");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		layoutUpdate();
		Log.i("PicAMain", "Program ended");

	}

	public void layoutUpdate() {
		// while (location == null)
		getLocation();
		new MyAsyncTask().execute();
	}

	private class MyAsyncTask extends AsyncTask<String, Void, ArrayList<View>>
			implements OnClickListener {
		protected ArrayList<View> doInBackground(String... arg0) {
			Log.d("PicAMain", "starting thread");
			if (location != null) {
				Log.d("PicAMain", "getting JSon");
				json = InstagramGet.getJson(location.getLatitude(),
						location.getLongitude()); // Add lat + long here
				Log.d("PicAMain", "got JSon");
			} else {
				Log.d("PicAMain", "Location Is Null");
				// json = InstagramGet.getJson();
				return null;
			}
			// Load image by url
			GridLayout rel = Locate.rel;
			ArrayList<View> views = new ArrayList<View>();
			Log.d("PicAMain", "starting to add pictures from JSon");
			if (json != null) {
				try {
					Log.d("PicAMain", "getting the images");
					String[] pics = InstagramGet.getImageUrl(json);
					Log.d("PicAMain", "got the images");
					if (start >= pics.length) {
						start = 0;
						end = 4;
					} else {
						end += 4;
					}
					for (; start < pics.length && start < end; start++) {
						Log.d("PicAGrid", "Loop " + start);
						ImageView mciv = new ImageView(Locate.context);
						// Log.d("PicAGrid", "new Imageview");
						mciv.setId(start + 10);
						// Log.d("PicAGrid", "set id");
						LayoutParams p = new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						// Log.d("PicAGrid", "Loading params");
						mciv.setLayoutParams(p);
						// Log.d("PicAGrid", "Set Params");
						URL thumb_u = new URL(pics[start]);
						// Log.d("PicAGrid", "New URL");
						Drawable thumb_d = Drawable.createFromStream(
								thumb_u.openStream(), "src");
						// Log.d("PicAGrid", "Drawable Thumb");
						mciv.setImageDrawable(thumb_d);
						views.add(mciv);
						Log.d("PicAGrid", "Setting drawable");
						// rel.addView(mciv, 230 / 2 * 3, 400);
						// onPostExecute(mciv);
						// Log.d("PicAGrid", "Adding to view");
					}
					// int diff = end - start - 1;
					// for (; diff > 0; diff--) {
					// rel.addView(new View(this));
					// }
					// Log.d("PicAGrid", "Adding view");
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("PicAGrid", "Images crashed " + e.toString());
				}

			}
			onPostExecute(views);
			return views;
		}

		protected void onPostExecute(View... result) {
			for (View v : result) {
				if (v != null) {
					(Locate.rel).addView(v, 230 / 2 * 3, 400);
				}
			}
		}

		@Override
		public void onClick(View arg0) {

		}

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

	/*
	 * public static int binarySearchPosition(ArrayList<String> urls,
	 * ArrayList<Integer> locations, int dist) { int start = 0; int end =
	 * urls.size() - 1; while (start != end) {
	 * 
	 * }
	 * 
	 * return start;
	 * 
	 * }
	 */

	public void getLocation() {
		try {
			Log.d("PicABug", "Here");
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
					// Log.d("PicAMain", "Network Enabled");
					manager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					// Log.d("PicAMain", "GPS Enabled");
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
		if (loc == null) {
			return;
		}
		if (isBetterLocation(loc, location)) {
			location = loc;
			Log.d("PicAMain", "changed location " + loc.getLatitude() + ", "
					+ loc.getLongitude());
			Log.d("PicAMain", "location lat long" + location.getLatitude()
					+ ", " + location.getLongitude());

		} else {
			Log.d("PicAMain", "Not better location " + loc.getLatitude() + ", "
					+ loc.getLongitude());
		}
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

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Get the layout inflater
		LayoutInflater inflater = getLayoutInflater();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.full_screen_image, null))
		// Add action buttons
				.setPositiveButton(R.string.dialogButton,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		return builder.create();
	}
}