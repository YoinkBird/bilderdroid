package com.example.myfirstapp;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class ViewAllStreamsActivity extends Activity {
	public final static String DISPLAY_STREAM_SELECTOR = "com.example.myfirstapp.DISPLAY_STREAM_SELECTOR";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// parse intent to determine what to do
		String displayStreamType = determineStreamType();
		
		// set up layout
		loadLayoutByStreamType(displayStreamType);

		// get images
		String[] mTestThumbUrls = null;
		mTestThumbUrls = loadImagesByStreamType(displayStreamType);
		Log.i(this.getClass().getSimpleName(), "mTestThumbUrls: " + mTestThumbUrls.toString());
		// set up gridview
		GridView gridview = (GridView) findViewById(R.id.gridview);
		// do something wacky and ultimately add the images to the gridview
		ImageAdapter tmpImgAdapter = new ImageAdapter(this);
		tmpImgAdapter.setThumbUrls(mTestThumbUrls);
		// do something wacky
		gridview.setAdapter(tmpImgAdapter);

		// click this to do that
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(ViewAllStreamsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_all_streams, menu);
		return true;
	}

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
	// determine type of stream to display
	private String determineStreamType (){
		// parse intent to determine what to do
		/* Either:
		 * * show latest streams (default)
		 * * show latest subscribed streams (button)
		 * * show nearby streams (button)
		 */
		String displayStreams = "all";
		Intent intent = getIntent();
		//TODO: remove hardcode for testing
		displayStreams = intent.getStringExtra(ViewAllStreamsActivity.DISPLAY_STREAM_SELECTOR);
		if(displayStreams == null){
			displayStreams = "all";
		}
		Log.i(this.getClass().getSimpleName(),  "DISPLAY_STREAM_SELECTOR - displayStreams: " + displayStreams);
		
		return displayStreams;
	}
	
	private void loadLayoutByStreamType(String streamType){
		// store the layout id to load - default is 'all streams'
		int layoutId = R.layout.fragment_view_all_streams;
		// choose layout based on streamtype
		if(streamType.equals("all")){
			layoutId = R.layout.fragment_view_all_streams;
		}
		if(streamType.equals("nearby")){
			layoutId = R.layout.fragment_view_nearby_streams;
		}
		if(streamType.equals("subscribed")){
			layoutId = R.layout.fragment_view_all_streams;
		}
		// load layout
		setContentView(layoutId);
                // configure buttons
		setupButtonListeners();
		return;
	}
	
	// load images based on stream view type (e.g. all, nearby, subscribed, etc)
	private String[] loadImagesByStreamType(String streamType){
		Log.i(this.getClass().getSimpleName(),  "in method: loadImagesByStreamType");
		String[] thumbUrlStrArray = null;
		//TODO: create simple URL to retrieve all images for a stream, all covers for all streams, etc
		String jsonRequestURL = null;
		if(streamType.equals("all")){
			//json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewallstreams?redirect=0";
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
			// Get the JSON Array or Object
			CustomJson customJsonObj = new CustomJson();
			JSONArray jsonImgArray = customJsonObj.getJsonArray(jsonRequestURL);
			// Get data from the JSON Array or Object
			CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
			thumbUrlStrArray = customJsonBilderappObj.parseJsonFromViewAllStreams(jsonImgArray);
		}
		if(streamType.equals("nearby")){
			//TODO: determine the json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			//TODO: implement this api; using the 'viewall' for now as a placeholder
			//IDEA: add a 'nearby=<gpscoord>' to the map-view API 
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewallstreams?redirect=0";
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
			// Get the JSON Array or Object
			CustomJson customJsonObj = new CustomJson();
			JSONArray jsonImgArray = customJsonObj.getJsonArray(jsonRequestURL);
			// Get data from the JSON Array or Object
			CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
			thumbUrlStrArray = customJsonBilderappObj.parseJsonFromViewAllStreams(jsonImgArray);
		}
		if(streamType.equals("subscribed")){
			//TODO: determine the json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			//TODO: implement this api; using the 'viewall' for now as a placeholder
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewallstreams?redirect=0";
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
			// Get the JSON Array or Object
			CustomJson customJsonObj = new CustomJson();
			JSONArray jsonImgArray = customJsonObj.getJsonArray(jsonRequestURL);
			// Get data from the JSON Array or Object
			CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
			thumbUrlStrArray = customJsonBilderappObj.parseJsonFromViewAllStreams(jsonImgArray);
		}
		return thumbUrlStrArray;
	}
	// buttons
	/** Called when the user clicks the button for 'search' */
	public void sendSearchQuery(View view){
		Intent intent = new Intent(this, SearchStreamsActivity.class);
		String message = "";
		EditText editText = (EditText) findViewById(R.id.editText_search_streams);
		message = editText.getText().toString();
		intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	/** Called when the user clicks one of the buttons for viewing streams*/
	public void startViewAllStreamsActivity(View view, String streamType){
		Intent intent = new Intent(this, ViewAllStreamsActivity.class);
		intent.putExtra(DISPLAY_STREAM_SELECTOR, streamType);
		startActivity(intent);
	}

   private void setupButtonListeners(){
     setupButtonListeners(null);
   }
   private void setupButtonListeners(String streamType){
     setupButtonListeners_viewAllStreams();
     setupButtonListeners_viewNearbyStreams();
     /*
     if(streamType != null){
      //* calling with type not working for some reason
      //* was intended to be called from loadLayoutByStreamType with
      //* setupButtonListeners(streamType)
       if(streamType.equals("all")){
         setupButtonListeners_viewAllStreams();
       }
       if(streamType.equals("subscribed")){
         setupButtonListeners_viewAllStreams();
       }
       if(streamType.equals("nearby")){
//         setupButtonListeners_viewNearbyStreams();
       }
     }
     */
   }
   // set up buttons for the 'View All Streams' layout
   private void setupButtonListeners_viewAllStreams(){
	 // listen to the button for 'search'
     Button searchResultsButton = (Button) findViewById(R.id.button_search_streams);
     if(searchResultsButton != null){
       searchResultsButton.setOnClickListener(
           new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                     //TODO
                     sendSearchQuery(v);
             }
           }
           );
     }
	 // listen to the button for 'nearby'
     Button viewNearbyStreamsButton = (Button) findViewById(R.id.button_find_nearby_streams);
     if(viewNearbyStreamsButton != null){
       viewNearbyStreamsButton.setOnClickListener(
           new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                     //TODO
                     startViewAllStreamsActivity(v, "nearby");
             }
           }
           );
     }
	 // listen to the button for 'subscribed streams'
     Button viewSubscribedStreamsButton = (Button) findViewById(R.id.button_subscribed_streams);
     if(viewSubscribedStreamsButton != null){
       viewSubscribedStreamsButton.setOnClickListener(
           new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                     //TODO
                     startViewAllStreamsActivity(v, "subscribed");
             }
           }
           );
       }
   }
   // set up buttons for the 'View Nearby Streams' layout
   private void setupButtonListeners_viewNearbyStreams(){
     Button morePicturesButton = (Button) findViewById(R.id.button_viewnearbystreams_more_pictures);
     if(morePicturesButton != null){
       morePicturesButton.setOnClickListener(
           new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               //TODO
               startViewAllStreamsActivity(v, "nearby");
             }
           }
           );
     }
     // Add a listener to the 'view streams' button
     Button viewAllStreamsButton = (Button) findViewById(R.id.button_view_all_streams);
     if(viewAllStreamsButton != null){
       viewAllStreamsButton.setOnClickListener(
           new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               //TODO
               startViewAllStreamsActivity(v, "all");
             }
           }
           );
     }
   }
}
