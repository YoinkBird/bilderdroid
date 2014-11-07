package com.example.myfirstapp;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class ViewAllStreamsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_view_all_streams);

		String[] mTestThumbUrls;
		String searchTerm = "android";
		String jsonRequestURL = "http://fizzenglorp.appspot.com/genericquery?redirect=0&term=" + searchTerm;
		//TODO: create simple URL to retrieve all images for a stream, all covers for all streams, etc
		if(true){
			//json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewallstreams?redirect=0&term=" + searchTerm;
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL: " + jsonRequestURL.toString());
//			JSONArray jsonImgArray = getJsonArray(jsonRequestURL);
			CustomJson customJsonObj = new CustomJson();
			JSONArray jsonImgArray = customJsonObj.getJsonArray(jsonRequestURL);
			CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
			mTestThumbUrls = customJsonBilderappObj.parseJsonFromViewAllStreams(jsonImgArray);
		}
		GridView gridview = (GridView) findViewById(R.id.gridview);
		//gridview.setAdapter(new ImageAdapter(this));
		//TODO: set img array in constructor so anonymous creation can be done again
		ImageAdapter tmpImgAdapter = new ImageAdapter(this);
//		tmpImgAdapter.setThumbIds(mTestThumbIds);
		Log.i(this.getClass().getSimpleName(), "mTestThumbUrls: " + mTestThumbUrls.toString());
		tmpImgAdapter.setThumbUrls(mTestThumbUrls);
		gridview.setAdapter(tmpImgAdapter);

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
}
