package com.example.myfirstapp;

import org.json.JSONArray;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

public class ViewSingleStreamActivity extends Activity {
    private LocationInfo mLocationInfo;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_nearby_streams);

		// Get the message from the intent
		Intent intent = getIntent();
		String streamType = intent.getStringExtra(ViewAllStreamsActivity.STREAMID_SELECTOR);
		if(streamType == null){
			streamType = "nearby";
		}

		// set up gridview
		GridView gridview = (GridView) findViewById(R.id.gridview);
		// do something wacky and ultimately add the images to the gridview
		ImageAdapter tmpImgAdapter = new ImageAdapter(this);
//		tmpImgAdapter.setThumbUrls(mTestThumbUrls);
		setGridViewByStreamType(streamType, tmpImgAdapter);
		// do something wacky
		gridview.setAdapter(tmpImgAdapter);

		// set up button listeners. "nearby" has no application in this activity
        setupButtonListeners("nearby");
    }
    private void setupButtonListeners(String streamType){
    	((Button) findViewById(R.id.location_forcelocation)).setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			LocationLibrary.forceLocationUpdate(ViewSingleStreamActivity.this);
    			Toast.makeText(getApplicationContext(), "Forcing a location update", Toast.LENGTH_SHORT).show();
    		}
    	});
        ((Button) findViewById(R.id.location_getlocation)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	// update text view with GPS coord
//                LocationLibrary.getLocationUpdate(LocationTestActivity.this);
            	LocationInfo latestInfo = new LocationInfo(getBaseContext());
//                Toast.makeText(getApplicationContext(), "Getting a location update", Toast.LENGTH_SHORT).show();
            	updateGpsTextView();
            }
        });
    }
    public void setUpGridView(){
    	
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

    public void updateGpsTextView(){
    	String urlParams = getGpsCoordParamString();
    	Toast.makeText(getApplicationContext(), "Getting a location update:\n" + urlParams, Toast.LENGTH_SHORT).show();
    	TextView gpsCoordTxtView = (TextView) findViewById(R.id.textView_viewnearbystreams_location);
    	gpsCoordTxtView.setText("GPS: " + urlParams);

    }
    private String getGpsCoordParamString () {
    	String urlParams = "";
    	String[] geoViewParams = getGpsCoordPair();
    	if(geoViewParams != null){
    		urlParams += "&lat=" + geoViewParams[0] + "&long=" + geoViewParams[1];
    		//                	urlParams = urlEncodeTryCatch(urlParams);
    	}
		return urlParams;

    }
    private String[] getGpsCoordPair () {
    	
//    private String[] getGpsCoordPair(final LocationInfo locationInfo) {
    	Log.i(this.getClass().getSimpleName(), "mLocationInfo retrieve");
//    	mLocationInfo.refresh(getApplicationContext());
    	String [] gpsCoordPair = new String[2];
    	if(mLocationInfo == null){
    		mLocationInfo = new LocationInfo(getBaseContext());
    	}
    	gpsCoordPair[0] = Float.toString(mLocationInfo.lastLong);
    	gpsCoordPair[1] = (Float.toString(mLocationInfo.lastLat));
    	return gpsCoordPair;
    }
    
    @Override
    public void onResume() {
        super.onResume();

        // cancel any notification we may have received from LocationTestBroadcastReceiver
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1234);
        
        refreshDisplay();

        // This demonstrates how to dynamically create a receiver to listen to the location updates.
        // You could also register a receiver in your manifest.
        final IntentFilter lftIntentFilter = new IntentFilter(LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction());
        registerReceiver(lftBroadcastReceiver, lftIntentFilter);
   }

    @Override
    public void onPause() {
        super.onPause();
        
    	Log.i(this.getClass().getSimpleName(), "onPause");
        unregisterReceiver(lftBroadcastReceiver);
   }

    @Override
    public void onStop(){
    	Log.i(this.getClass().getSimpleName(), "onStop");
    	super.onStop();
    }
    
    private void refreshDisplay() {
        refreshDisplay(new LocationInfo(this));
    }

    private void refreshDisplay(final LocationInfo locationInfo) {
    	/*
        final View locationTable = findViewById(R.id.location_table);
        final Button buttonShowMap = (Button) findViewById(R.id.location_showmap);
        final TextView locationTextView = (TextView) findViewById(R.id.location_title);
        */

        if (locationInfo.anyLocationDataReceived()) {
        	// update global field
        	mLocationInfo = locationInfo;
        	updateGpsTextView();
        	/*
            locationTable.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.location_timestamp)).setText(LocationInfo.formatTimeAndDay(locationInfo.lastLocationUpdateTimestamp, true));
            ((TextView)findViewById(R.id.location_latitude)).setText(Float.toString(locationInfo.lastLat));
            ((TextView)findViewById(R.id.location_longitude)).setText(Float.toString(locationInfo.lastLong));
            ((TextView)findViewById(R.id.location_accuracy)).setText(Integer.toString(locationInfo.lastAccuracy) + "m");
            ((TextView)findViewById(R.id.location_provider)).setText(locationInfo.lastProvider);
            if (locationInfo.hasLatestDataBeenBroadcast()) {
                locationTextView.setText("Latest location has been broadcast");
            }
            else {
                locationTextView.setText("Location broadcast pending (last " + LocationInfo.formatTimeAndDay(locationInfo.lastLocationUpdateTimestamp, true) + ")");
            }
            buttonShowMap.setVisibility(View.VISIBLE);
            buttonShowMap.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + locationInfo.lastLat + "," + locationInfo.lastLong + "?q=" + locationInfo.lastLat + "," + locationInfo.lastLong + "(" + locationInfo.lastAccuracy + "m at " + LocationInfo.formatTimeAndDay(locationInfo.lastLocationUpdateTimestamp, true) + ")"));
                    startActivity(intent);
                }
            });
            */
        }
        else {
        	/*
            locationTable.setVisibility(View.GONE);
            buttonShowMap.setVisibility(View.GONE);
            locationTextView.setText("No locations recorded yet");
            */
        }
     }
    
    private final BroadcastReceiver lftBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // extract the location info in the broadcast
            final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
            // refresh the display with it
            refreshDisplay(locationInfo);
        }
    };
}