package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.os.StrictMode;
import android.util.Log;

public class CustomJson {

	public CustomJson() {
		// TODO Auto-generated constructor stub
	}


	public String getJsonString(String httpUrl) {
		// Just for testing, allow network access in the main thread
		// NEVER use this is productive code
		StrictMode.ThreadPolicy policy = new StrictMode.
				ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 


		//setContentView(R.layout.main);
		String jsonReturnString = sendHttpRequest(httpUrl);
		try {
			//JSONObject jsonObject = new JSONObject(jsonReturnString);
			JSONArray jsonObject = new JSONArray(jsonReturnString);
			jsonReturnString = jsonObject.toString();
			Log.i(this.getClass().getSimpleName(), jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonReturnString;
	}
	public String sendHttpRequest(String httpUrl) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		//String httpUrl = "https://bugzilla.mozilla.org/rest/bug?assigned_to=lhenry@mozilla.com";
		//httpUrl = "http://fizzenglorp.appspot.com/genericquery?redirect=0&term=test";
		HttpGet httpGet = new HttpGet(httpUrl);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				//Log.e(ParseJSON.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}