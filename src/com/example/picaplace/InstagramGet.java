package com.example.picaplace;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.util.Log;

public class InstagramGet extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instagram);
	}

	public static String getPictureStream() {

		StringBuilder builder = new StringBuilder("");
		AndroidHttpClient client = AndroidHttpClient.newInstance(
				"PicAPlace");
		HttpGet httpGet = new HttpGet(
				"https://api.instagram.com/v1/media/search?client_id='f83232561f2842a5badc2da920518b9c'");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.d("TEST", "Failed to download file");
			}
		} catch (Exception a) {
			Log.d("Instagram error", a.toString());
		}
		Log.d("Instagram", builder.toString());

		return builder.toString();
	}
}
