package com.example.myfirstapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.widget.TextView;
import android.widget.Toast;

/* Note:
 * Strategy to enable 'singlestream' is:
 * add to 'loadLayoutByStreamType' with 'view_nearby_streams' layout
 * in 'loadImagesByStreamType' use images from 'all streams' as the single-stream has not been enabled yet
 * then:
 * add layout for 'singleStream'
 * in 'loadImagesByStreamType' enable single-stream images
 */

public class ViewAllStreamsActivity extends Activity {
	public final static String DISPLAY_STREAM_SELECTOR = "com.example.myfirstapp.DISPLAY_STREAM_SELECTOR";
	public final static String STREAMID_SELECTOR       = "com.example.myfirstapp.STREAMID_SELECTOR";

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
		setGridViewByStreamType(displayStreamType, tmpImgAdapter);
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
				Toast.makeText(ViewAllStreamsActivity.this, "" + position, Toast.LENGTH_SHORT).show();

				Log.i(this.getClass().getSimpleName(), "adapterview: " + parent.toString());
				Log.i(this.getClass().getSimpleName(), "adapterview: " + parent.getAdapter().getItemId(position));
				Log.i(this.getClass().getSimpleName(), "adapterview: " + ((ImageAdapter) parent.getAdapter()).getStreamId(position));
				String testStreamId = ((ImageAdapter) parent.getAdapter()).getStreamId(position);
				//TODO: this call may need to be flexible for nearby, all, etc
				Log.i(this.getClass().getSimpleName(), "testStreamId: " + testStreamId.toString());
				startViewAllStreamsActivity(v, "single", testStreamId);
			}
		});
	}
	public void setGridViewByStreamType(String streamType, ImageAdapter gridViewImgAdapter){
		if(
				streamType.equals("all") ||
				streamType.equals("search") ||
				streamType.equals("subscribed") ||
				streamType.equals("nearby")){
			CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
			String jsonRequestURL = customJsonBilderappObj.getJsonUrlByStreamType(streamType);
			// Get the JSON Array or Object
			CustomJson customJsonObj = new CustomJson();
			JSONArray imgJsonArray = customJsonObj.getJsonArray(jsonRequestURL);
			// set up adapter
			customJsonBilderappObj.generateGridJson(imgJsonArray, gridViewImgAdapter);

		}
		
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
	// determine whether streamId specified, if so, return it
	//caveat: if none specified this is not going to be fun
	private String getStreamId (){
		String streamId = null;
		// get intent, check if null
		Intent intent = getIntent();
		String streamIdSelector = intent.getStringExtra(ViewAllStreamsActivity.STREAMID_SELECTOR);
		if(streamIdSelector != null){
			streamId = streamIdSelector;
		}
		return streamId;
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
		if(streamType.equals("single")){
			layoutId = R.layout.fragment_view_single_stream;
		}
		// load layout
		setContentView(layoutId);
                // configure buttons
		setupButtonListeners();
		changeElementsByStreamType(streamType);
		return;
	}

	private void changeElementsByStreamType(String streamType){
		Log.i(this.getClass().getSimpleName(),  "in method: changeElementsByStreamType");
		Log.i(this.getClass().getSimpleName(),  "streamType: " + streamType);
		// store the layout id to load - default is 'all streams'
		int layoutId = R.layout.fragment_view_all_streams;
		// choose layout based on streamtype
		if(streamType.equals("all")){
		}
		if(streamType.equals("nearby")){
//            ((TextView)findViewById(R.id.location_latitude)).setText(Float.toString(locationInfo.lastLat));
//            ((TextView)findViewById(R.id.location_longitude)).setText(Float.toString(locationInfo.lastLong));
		}
		if(streamType.equals("subscribed")){
		}
		if(streamType.equals("single")){
			TextView singleStreamTextView_title = (TextView) findViewById(R.id.textView_viewnearbystreams_title);
			String titleString = (String) singleStreamTextView_title.getText();
			titleString += getStreamId();
			singleStreamTextView_title.setText(titleString);
		}
		return;
	}
	
	// urlencode - put in method to abstract out the try catch
	private String urlEncodeTryCatch(String httpUrl){
		String encodedHttpUrl = null;
		try {
			encodedHttpUrl = URLEncoder.encode(httpUrl, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.i(this.getClass().getSimpleName(), "httpUrl" + httpUrl + "encoded: " + encodedHttpUrl);
		return encodedHttpUrl;
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
		// caveat: don't call this without an argument
		if(streamType.equals("single")){
			String searchTerm = null;
			//caveat: if none specified this is not going to be fun
			searchTerm = getStreamId();
			//TODOne: this returns 'img src=...' for the javascript map; needs to be fixed!
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewsinglestream?geoview=0&stream_id=" + urlEncodeTryCatch(searchTerm);
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
			// Get the JSON Array or Object
			CustomJson customJsonObj = new CustomJson();
			JSONObject jsonImgObject = customJsonObj.getJsonObject(jsonRequestURL);
			// Get data from the JSON Array or Object
			CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
			thumbUrlStrArray = customJsonBilderappObj.parseJsonFromViewSingleStream(jsonImgObject);
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
		intent.putExtra(MainActivity.SEARCH_QUERY, message);
		startActivity(intent);
	}
	/** Called when the user clicks one of the buttons for viewing streams*/
	public void startViewAllStreamsActivity(View view, String streamType){
		Intent intent = new Intent(this, ViewAllStreamsActivity.class);
		if(streamType.equals("nearby")){
			intent = new Intent(this, ViewSingleStreamActivity.class);
		}
		intent.putExtra(DISPLAY_STREAM_SELECTOR, streamType);
		startActivity(intent);
	}
	/** Called when the user clicks one of the grid elements */
	public void startViewAllStreamsActivity(View view, String streamType, String streamName){
		Intent intent = new Intent(this, ViewAllStreamsActivity.class);
		intent.putExtra(DISPLAY_STREAM_SELECTOR, streamType);
		intent.putExtra(STREAMID_SELECTOR, streamName);
		startActivity(intent);
	}

   private void setupButtonListeners(){
     setupButtonListeners(null);
   }
   private void setupButtonListeners(String streamType){
     setupButtonListeners_viewAllStreams();
     setupButtonListeners_viewNearbyStreams();
     setupButtonListeners_viewSingleStream();
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
   // set up buttons for the 'View Nearby Streams' layout
   private void setupButtonListeners_viewSingleStream(){
     Button morePicturesButton = (Button) findViewById(R.id.button_viewsinglestream_more_pictures);
     if(morePicturesButton != null){
       morePicturesButton.setOnClickListener(
           new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               //TODO
               startViewAllStreamsActivity(v, "single");
             }
           }
           );
     }
     Button uploadPicturesButton = (Button) findViewById(R.id.button_viewsinglestream_upload);
     if(uploadPicturesButton != null){
       uploadPicturesButton.setOnClickListener(
           new View.OnClickListener() {
             @Override
             public void onClick(View v) {
            	 String streamType = getStreamId();
            	 Intent intent = new Intent(v.getContext(), UploadActivity.class);
            	 if(streamType != null){
            		 intent.putExtra(STREAMID_SELECTOR, streamType);
            	 }
            	 startActivity(intent);
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
