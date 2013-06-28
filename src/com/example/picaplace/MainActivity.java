package com.example.picaplace;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
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

		Drawable image = ImageOperations(this, path, "ic_launcher-web.png");
		Log.d("PicAMain", "Image.jpg is ok");
		imageView = new ImageView(this);
		imageView = (ImageView) findViewById(R.id.image);
		imageView.setImageDrawable(image);

		Log.d("MainActivity", "Activity is created");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Log.d("MainActivity", "Changed policy");
		String json = InstagramGet.getPictureStream();
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

	private String path = "http://peytonhamil.com/suns.jpg";
	private ImageView imageView;

	public Object fetch(String address) throws MalformedURLException,
			IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	private Drawable ImageOperations(Context ctx, String url,
			String saveFilename) {
		try {
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

}
