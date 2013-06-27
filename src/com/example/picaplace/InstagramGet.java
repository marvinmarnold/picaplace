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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

public class InstagramGet extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instagram);
	}

	public static String getPictureStream() {

		Log.d("Instagram", "Getting picture started");
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
				Log.d("Instagram", builder.toString());
			} else {
				Log.e("TEST", "Failed to download file");
			}
		} catch (Exception a) {
			Log.d("Instagram", a.toString());
		}

		return builder.toString();
	}

	public static String getPictureStream(double lat, double lng) {

		Log.d("Instagram", "Getting picture started");
		StringBuilder builder = new StringBuilder("");
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"https://api.instagram.com/v1/media/search?lat="
						+ lat
						+ "&lng="
						+ lng
						+ "&distance=1000&client_id=eaab0a729f20412b922747020fb4798a");

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
				Log.d("Instagram", builder.toString());
			} else {
				Log.e("TEST", "Failed to download file");
			}
		} catch (Exception a) {
			Log.d("Instagram", a.toString());
		}

		return builder.toString();
	}
	/*
	 * public static void transformStringToArray(String json, String[] values) {
	 * try { JSONObject jsonObj = new JSONObject(json); jsonObj =
	 * jsonObj.getJSONObject(""); } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * }
	 */
}
