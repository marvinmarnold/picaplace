package com.example.picaplace;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instagram);
		Log.d("MainActivity", "Activity is created");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Log.d("MainActivity", "Changed policy");
		String json = InstagramGet.getPictureStream();

		try {
			ImageView myImageView = (ImageView) findViewById(R.id.image);
			URL thumb_u = new URL(InstagramGet.getImageUrl(json));
			Drawable thumb_d = Drawable.createFromStream(thumb_u.openStream(),
					"src");
			myImageView.setImageDrawable(thumb_d);
		} catch (Exception e) {
			// handle it
		}

		// String[] valuesArr = { "data", "images", "thumbnail", "url" };
		Log.d("Instagram", "Instagram Done");
		String code = InstagramGet.getNumberOfLikes(json, 0, 15);
		TextView myTextView = (TextView) findViewById(R.id.main_text);
		myTextView.setText(code); // Set the window text
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Image setting

	private Drawable LoadImageFromWebOperations(String url) {

		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}

	}
}
