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

public class SearchStreamsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the message from the intent
		Intent intent = getIntent();
		String searchTerm = intent.getStringExtra(MainActivity.SEARCH_QUERY);
		String jsonRequestURL = "http://fizzenglorp.appspot.com/genericquery?redirect=0&term=" + searchTerm;
		
		CustomJson customJsonObj = new CustomJson();
		String jsonReturnString = customJsonObj.getJsonString(jsonRequestURL);
		CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
		String[] mTestThumbUrls = customJsonBilderappObj.loadImagesByStreamType("search", jsonRequestURL);
		Log.i(this.getClass().getSimpleName(), "mTestThumbUrls: " + mTestThumbUrls.toString());
		// Set the text view as the activity layout
		if(true){
			// set up layout
			setContentView(R.layout.fragment_search_streams);
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
	/** Called when the user clicks the Search button */
	public void searchQuery(View view){
		Intent intent = new Intent(this, SearchStreamsActivity.class);
		EditText editText = (EditText) findViewById(R.id.search_term);
		String message = editText.getText().toString();
		intent.putExtra(MainActivity.SEARCH_QUERY, message);
		startActivity(intent);
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_streams, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_search_streams,
					container, false);
			return rootView;
		}
	}
}
