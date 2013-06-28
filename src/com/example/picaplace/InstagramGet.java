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

	public static String getPictureStream() {

		Log.d("PicAInstagram", "Getting picture started");
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

	public static String getPictureStream(double lat, double lng) {

		Log.d("PicAInstagram", "Getting picture started");
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
				Log.d("PicAInstagram", builder.toString());
			} else {
				Log.e("TEST", "Failed to download file");
			}
		} catch (Exception a) {
			Log.d("PicAInstagram", a.toString());
		}

		return builder.toString();
	}

	// This method get all sub JSonObjects in a json string and returns the
	// value the most inner one.
	// String
	public static String parseJsonString(String json, String[] values) {
		String value = null;
		try {
			// Log.d("PicAJSON", json);
			JSONObject jsonObj = new JSONObject(json);
			// Log.d("PicAJSON", jsonObj.toString());
			for (int i = 0; i < values.length - 1; i++) {
				jsonObj = jsonObj.getJSONObject(values[i]);
				// Log.d("PicAJSON", jsonObj.toString());
			}
			value = jsonObj.getString(values[values.length - 1]);
			// Log.d("Val", value + "");
		} catch (Exception e) {
			e.printStackTrace();
			// Log.d("PicAJSON", "ParseCrashed");

		}
		return value;
	}

	public static int parseJsonInt(String json, String[] values) {
		int value = 0;
		try {
			Log.d("PicAJSON", json);
			JSONObject jsonObj = new JSONObject(json);
			Log.d("PicAJSON", jsonObj.toString());
			for (int i = 0; i < values.length - 1; i++) {
				jsonObj = jsonObj.getJSONObject(values[i]);
				Log.d("PicAJSON", jsonObj.toString());
			}
			value = jsonObj.getInt(values[values.length - 1]);
			Log.d("PicAJSON", value + "");

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("PicAJSON", "ParseCrashed");
		}
		return value;
	}

	public static double parseJsonDouble(String json, String[] values) {
		double value = 0;
		try {
			JSONObject jsonObj = new JSONObject(json);
			for (int i = 0; i < values.length - 1; i++) {
				jsonObj = jsonObj.getJSONObject(values[i]);
			}
			value = jsonObj.getDouble(values[values.length - 1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
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

	public static String getImageUrl(String json) {
		String value = "";
		Log.d("PicAJSON", "Likes started");
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject cur = jsonObject.getJSONArray("data").getJSONObject(0)
					.getJSONObject("images").getJSONObject("thumbnail");
			value = cur.getString("url");
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
