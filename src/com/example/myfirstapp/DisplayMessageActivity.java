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

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayMessageActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the message from the intent
		Intent intent = getIntent();
		String searchTerm = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		String jsonRequestURL = "http://fizzenglorp.appspot.com/genericquery?redirect=0&term=" + searchTerm;
		
		String jsonReturnString = getJsonString(jsonRequestURL);
		// Set the text view as the activity layout
		if(true){
			// Set the text view as the activity layout
			setContentView(R.layout.fragment_display_message);
			// get text view for json
			TextView jsonTextView = (TextView) findViewById(R.id.display_json);
			jsonTextView.setText(jsonReturnString);
			//Log.i(MainActivity.class.getName(), "jsonTextView: " + jsonTextView.toString());
			//Log.i(MainActivity.class.getName(), "json Text: " + jsonReturnString);
		}
		else{
			if (savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment()).commit();
			}
		}
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	/** Called when the user clicks the Search button */
	public void searchQuery(View view){
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.search_term);
		String message = editText.getText().toString();
		String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_message, menu);
		return true;
	}
*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_display_message,
					container, false);
			return rootView;
		}
	}
}
