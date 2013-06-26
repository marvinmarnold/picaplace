package com.example.picaplace;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.util.Log;

public class InstagramGet extends Activity {

	public String getPictureStream() {

		StringBuilder builder = new StringBuilder("");
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"https://api.instagram.com/v1/media/search");
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
			System.err.println("ERROR Instagram Get");
		}
		System.out.println(builder.toString());
		return builder.toString();
	}
}
