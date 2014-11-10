package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchStreamsActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: move after the layout setup
		// Get the message from the intent
		Intent intent = getIntent();
		String searchTerm = intent.getStringExtra(MainActivity.SEARCH_QUERY);
		String jsonRequestURL = "http://fizzenglorp.appspot.com/genericquery?redirect=0&term=" + searchTerm;
		
		// TODO: move after the layout setup
		CustomJson customJsonObj = new CustomJson();
		String jsonReturnString = customJsonObj.getJsonString(jsonRequestURL);
		CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
		String[] mTestThumbUrls = customJsonBilderappObj.loadImagesByStreamType("search", jsonRequestURL);
		Log.i(this.getClass().getSimpleName(), "mTestThumbUrls: " + mTestThumbUrls.toString());
		// Set the text view as the activity layout
		if(true){
			// set up layout
			setContentView(R.layout.fragment_search_streams);
			/*
			 * no longer needed, delete once everything is working for sure, eh
            <TextView
                android:id="@+id/display_json"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/display_json" />
			// get text view for json
			TextView jsonTextView = (TextView) findViewById(R.id.display_json);
			jsonTextView.setText(jsonReturnString);
			*/
			//Log.i(MainActivity.class.getName(), "jsonTextView: " + jsonTextView.toString());
			//Log.i(MainActivity.class.getName(), "json Text: " + jsonReturnString);
		}
		else{
			if (savedInstanceState == null) {
				getSupportFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment()).commit();
			}
		}
		
		// set up gridview
		GridView gridview = (GridView) findViewById(R.id.gridview_search);
		// do something wacky and ultimately add the images to the gridview
		ImageAdapter tmpImgAdapter = new ImageAdapter(this);
		tmpImgAdapter.setThumbUrls(mTestThumbUrls);
		// do something wacky
		gridview.setAdapter(tmpImgAdapter);

		// click this to do that
		/* IDEA:
		 * create arraylist of streamnames, then use 'position' to index the streamname
		 * instead of 'Toast' call 'view single stream' with the intent 'streamname'
		 * 1. call view single stream (after toast displays)
		 * 2. pass in stream id
		*/
		gridview.setOnItemClickListener(new OnItemClickListener() {
			// http://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener.html
			// parent:   adapterview where click happened
			// view:     view that was clicked
			// position: position of the view in the adapter
			// id:       row id of clicked item
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();

				Log.i(this.getClass().getSimpleName(), "adapterview: " + parent.toString());
				Log.i(this.getClass().getSimpleName(), "adapterview: " + parent.getAdapter().getItemId(position));
				Log.i(this.getClass().getSimpleName(), "adapterview: " + ((ImageAdapter) parent.getAdapter()).getStreamId(position));
				String testStreamId = ((ImageAdapter) parent.getAdapter()).getStreamId(position);
				//TODO: this call may need to be flexible for nearby, all, etc
				Log.i(this.getClass().getSimpleName(), "testStreamId: " + testStreamId.toString());
				startViewAllStreamsActivity(v, "single", testStreamId);
			}
		});
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	// copied and adapted from ViewAllStreamsActivity
	/** Called when the user clicks one of the grid elements */
	public void startViewAllStreamsActivity(View view, String streamType, String streamName){
		Intent intent = new Intent(this, ViewAllStreamsActivity.class);
		intent.putExtra(ViewAllStreamsActivity.DISPLAY_STREAM_SELECTOR, streamType);
		intent.putExtra(ViewAllStreamsActivity.STREAMID_SELECTOR, streamName);
		startActivity(intent);
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
