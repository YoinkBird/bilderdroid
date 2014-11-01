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

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		String searchTerm = "test";
		String jsonRequestURL = "http://fizzenglorp.appspot.com/genericquery?redirect=0&term=" + searchTerm;

		String jsonReturnString = getJsonString(jsonRequestURL);
		if(true){
			// Set the text view as the activity layout
			setContentView(R.layout.fragment_main);
			// get text view for json
			TextView jsonTextView = (TextView) findViewById(R.id.display_json);
			jsonTextView.setText(jsonReturnString);
			//Log.i(MainActivity.class.getName(), "jsonTextView: " + jsonTextView.toString());
			//Log.i(MainActivity.class.getName(), "json Text: " + jsonReturnString);
		}
		else{
			setContentView(R.layout.activity_main);
			if (savedInstanceState == null) {
				getFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment()).commit();
			}
		}

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
			Log.i(MainActivity.class.getName(), jsonObject.toString());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_search:
				//openSearch();
				return true;
			case R.id.action_settings:
				//openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	/** Called when the user clicks the Send button */
	public void sendMessage(View view){
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	/** Called when the user clicks the 'Stop App' button */
	/** see also http://stackoverflow.com/a/4153842
	 * */
	public void stopApplication(View view){
		//POC: show that this function is being called by updating the text input field
		if(false){
			EditText editText = (EditText) findViewById(R.id.edit_message);
			editText.setText("defaultvalue");
		}
		// end program lifecycle 
		else{
			finish();
		}
	}
}
