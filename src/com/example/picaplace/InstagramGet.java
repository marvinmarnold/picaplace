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
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class InstagramGet extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instagram);
	}

	public static String getJson() {
		Log.d("PicAInstagram", "Json Without params");
		StringBuilder builder = new StringBuilder("");
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"https://api.instagram.com/v1/media/popular?client_id=eaab0a729f20412b922747020fb4798a");

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
				Log.d("PicAInstagram", builder.toString());
			} else {
				Log.e("TEST", "Failed to download file");
			}
		} catch (Exception a) {
			Log.d("PicAInstagram", a.toString());
		}

		return builder.toString();
	}

	public static String getJson(double lat, double lng) {
		Log.d("PicAInstagram", "Json With params");
		StringBuilder builder = new StringBuilder("");
		HttpClient client = new DefaultHttpClient();
		String http = "https://api.instagram.com/v1/media/search?lat=\"" + lat
				+ "\"&lng=\"" + lng
				+ "\"&distance=1000&client_id=eaab0a729f20412b922747020fb4798a";
		Log.d("PicABUG", http);
		HttpGet httpGet = new HttpGet(http);
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
				Log.d("PicAInstagram", builder.toString());
			} else {
				Log.e("TEST", "Failed to download file");
			}
		} catch (Exception a) {
			Log.d("PicAInstagram", a.toString());
		}

		return builder.toString();
	}

	// Range between 0 to 15 inclusive.
	public static String getNumberOfLikes(String json, int start, int end) {
		String value = "";
		Log.d("PicAJSON", "Likes started");
		try {
			JSONObject jsonObject = new JSONObject(json);
			for (int i = start; i <= (end % 16); i++) {
				Log.d("PicAJSON", "Likes: " + i);
				JSONObject cur = jsonObject.getJSONArray("data")
						.getJSONObject(i).getJSONObject("likes");
				value += "Likes " + cur.getInt("count") + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("PicAJSON", "Likes Catched");
		}
		return value;
	}

	public static String[] getImageUrl(String json) {
		String[] value = new String[15];
		Log.d("PicAJSON", "Likes started");
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject cur;
			for (int i = 0; i < 15; i++) {
				cur = jsonObject.getJSONArray("data").getJSONObject(i)
						.getJSONObject("images").getJSONObject("thumbnail");
				value[i] = cur.getString("url");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("PicAJSON", "Likes Catched");
		}
		return value;
	}

	public static int getMetaCode(String json) {
		int value = 0;
		Log.d("PicAJSON", "Meta code started");
		try {
			JSONObject jsonObject = new JSONObject(json).getJSONObject("meta");
			return jsonObject.getInt("code");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("PicAJSON", "Meta Code failed");
		}
		Log.d("PicAJSON", "Meta code started");
		return value;
	}

}
