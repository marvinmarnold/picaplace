package com.example.picaplace;

import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instagram);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String json = InstagramGet.getJson(); // Add lat long here
		// Load image by url
		GridLayout rel = (GridLayout) findViewById(R.id.grid);
		Log.d("PicAGrid", "Grid Layout");
		LayoutParams p = new LayoutParams(230, 180);
		Log.d("PicAGrid", "Loading params");
		try {
			String[] pics = InstagramGet.getImageUrl(json);
			Log.d("PicAGrid", "Pics ");
			for (int i = 0; i < pics.length; i++) {
				Log.d("PicAGrid", "Loop " + i);
				ImageView mciv = new ImageView(this);
				Log.d("PicAGrid", "new Imageview");
				mciv.setId(i + 10);
				Log.d("PicAGrid", "set id");
				mciv.setLayoutParams(p);
				Log.d("PicAGrid", "Set Params");
				URL thumb_u = new URL(InstagramGet.getImageUrl(json)[i]);
				Log.d("PicAGrid", "New URL");
				Drawable thumb_d = Drawable.createFromStream(
						thumb_u.openStream(), "src");
				Log.d("PicAGrid", "Drawable Thumb");
				mciv.setImageDrawable(thumb_d);
				Log.d("PicAGrid", "Setting drawable");
				// rel.addView(mciv, 230, 200);
				rel.addView(mciv);
				Log.d("PicAGrid", "Adding to view");

			}
			Button b = new Button(this);
			b.setText("Refresh");
			b.setId(9999);
			rel.addView(b);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("PicAGrid", "Images crashed " + e.toString());
		}
		// Ended load by url
		// Log.d("Instagram", "Instagram Done");
		// String code = InstagramGet.getNumberOfLikes(json, 0, 15);
		// TextView myTextView = (TextView) findViewById(R.id.main_text);
		// myTextView.setText(code); // Set the window text
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Image setting

}
