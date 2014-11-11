package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CustomJsonBilderapp {

	//TODO: import context, just in case
	public CustomJsonBilderapp() {
		// TODO Auto-generated constructor stub
	}


	/*
	 * generate grid-view json
	 * this means creating two arrays an initialising the ImageAdapter
	 * a bit hacky, but a quick  solution
	 */
	public void generateGridJson(JSONArray jsonImgArray, ImageAdapter gridViewImgAdapter){
		//json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
		List<String> thumbUrlsArrayList = new ArrayList<String>();
		List<String> streamIdsArrayList  = new ArrayList<String>();

		// get data from json
		for(int i = 0; i < jsonImgArray.length(); i++){
			String imgUrl = null;
			String streamIdString = null;
			try {
				imgUrl = jsonImgArray.getJSONObject(i).getString("coverurl");
				streamIdString = jsonImgArray.getJSONObject(i).getString("streamid");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO: check the 'getJsonObject' return types instead of checking for != 'null'
			if(imgUrl != null && imgUrl != "null"){
				Log.i(this.getClass().getSimpleName(), "imgUrl: " + imgUrl.toString());
				thumbUrlsArrayList.add(imgUrl);
			}
			if(streamIdString != null && streamIdString != "null"){
				Log.i(this.getClass().getSimpleName(), "streamIdString: " + streamIdString.toString());
				streamIdsArrayList.add(streamIdString);
			}
		}
		// prepare to convert ArrayList to array
		String[] thumbUrlsArray = new String[thumbUrlsArrayList.size()];
		String[] streamIdsArray = new String[streamIdsArrayList.size()];
		Log.i(this.getClass().getSimpleName(), thumbUrlsArrayList.toString());

		// set up adapter
		gridViewImgAdapter.setThumbUrls((String[]) thumbUrlsArrayList.toArray(thumbUrlsArray));
		gridViewImgAdapter.setStreamIds((String[]) streamIdsArrayList.toArray(streamIdsArray));

//		return  (String[]) thumbUrlsArrayList.toArray();
		return;
	}
	
	public String[] parseJsonFromViewAllStreams(JSONArray jsonImgArray){
		//json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
		List<String> thumbUrlsArrayList = new ArrayList<String>();

		for(int i = 0; i < jsonImgArray.length(); i++){
			String imgUrl = null;
			try {
				imgUrl = jsonImgArray.getJSONObject(i).getString("coverurl");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO: check the 'getJsonObject' return types instead of checking for != 'null'
			if(imgUrl != null && imgUrl != "null"){
				Log.i(this.getClass().getSimpleName(), "imgUrl: " + imgUrl.toString());
				thumbUrlsArrayList.add(imgUrl);
			}
		}
		String[] array = new String[thumbUrlsArrayList.size()];
		Log.i(this.getClass().getSimpleName(), thumbUrlsArrayList.toString());
		return  thumbUrlsArrayList.toArray(array);
//		return  (String[]) thumbUrlsArrayList.toArray();
	}
	
	
	public String[] parseJsonFromViewSingleStream(JSONObject jsonImgObject){
		//json format:
		//{"markers":[{"timestamp":2015013,"content":"http:\/\/lh3.ggpht.com\/8KxOkIk974YKqc7WqQA6KvwAJoAzxsSzUiXwAHadz-cWuZOBQPhNvPLC-N1lJxpJDplP3ArfV6XZk5S9ursNmXkK","longitude":-63,"latitude":64},{"timestamp":2015013,"content":"http:\/\/lh6.ggpht.com\/SoSoJSfFbRgoXck2m0OE5cy9inxNRRpg-S87eMmnYsPjKKdXbGGl03RoLHojCGhbXTX05De7bGAevKVjHe8C7q8","longitude":-37,"latitude":-86},{"timestamp":2015013,"content":"http:\/\/lh5.ggpht.com\/ccvfHJ2PNkTHuYZe9kBSQNzYH3-rz3NWfnmPCIEC4MAwQeoAZzuscBdtjqW8SJoQTZQ7IJ__u_zOAxBmcf0WFw","longitude":52,"latitude":-20},{"timestamp":2015013,"content":"http:\/\/lh6.ggpht.com\/stxt-tOHFna2J4YiCLCLeVtM5S5dIQAsV9FPm9EZFFhXhqDQkXnGEwN9bIkG9RVLfqWruxqQsKPWVbTSKWoMQmUE","longitude":17,"latitude":60},{"timestamp":2015013,"content":"http:\/\/lh3.ggpht.com\/1ogCl35Cg9ARmT37HXBBdU6qmAi_KxLtgyDOpyeK-jNCwWknyvWrJcAKOc4hnEZS1LOUK683j4e0NKz13oQ7dg","longitude":54,"latitude":50},{"timestamp":2015013,"content":"http:\/\/lh6.ggpht.com\/ZWLuvuuMWYfejK3jE7Gfjug2pqpQ0TnzFRaT9ZWATROg0I4YEsuvSeFJJca0Jbinoe0NRTtduIkha-VSHMl0Kw","longitude":-14,"latitude":83},{"timestamp":2015013,"content":"http:\/\/lh3.ggpht.com\/tgMYwNmsTqJiMcxkwRxDxE3-HnuKIxJtU3Jdtowcj6QDtXrRXImj-ZTeUS4kEYDQbiBVCfjZhIQZtaO_Tn75rzU","longitude":34,"latitude":87}]}

		List<String> thumbUrlsArrayList = new ArrayList<String>();

		JSONArray jsonImgArray = null;
		try {
			// {"markers":[{"content":"http:....",}]}
			jsonImgArray = jsonImgObject.getJSONArray("markers");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i = 0; i < jsonImgArray.length(); i++){
			String imgUrl = null;
			try {
//				imgUrl = jsonImgArray.getJSONObject(i).getString("coverurl");
				imgUrl = jsonImgArray.getJSONObject(i).getString("content");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//TODO: check the 'getJsonObject' return types instead of checking for != 'null'
			if(imgUrl != null && imgUrl != "null"){
				Log.i(this.getClass().getSimpleName(), "imgUrl: " + imgUrl.toString());
				thumbUrlsArrayList.add(imgUrl);
			}
		}
		String[] array = new String[thumbUrlsArrayList.size()];
		Log.i(this.getClass().getSimpleName(), "thumbUrlsArrayList: " + thumbUrlsArrayList.toString());
		return  thumbUrlsArrayList.toArray(array);
//		return  (String[]) thumbUrlsArrayList.toArray();
	}

	// TODO: call this from ViewAllStreamsActivity and remove the definition from there
	public String getJsonUrlByStreamType(String streamType){
		Log.i(this.getClass().getSimpleName(), "method: getJsonUrlByStreamType");
		String jsonRequestURL = null;
		if(streamType.equals("all")){
			//json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewallstreams?redirect=0";
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
		}
		if(streamType.equals("search")){
			//TODO: make json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			//json format: [{"id":"<streamid>", -NO:- "coverurl":"http://<url>"},]
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
		}
		// caveat: don't call this without an argument
		if(streamType.equals("single")){
			String searchTerm = null;
			//caveat: if none specified this is not going to be fun
			//TODO:			searchTerm = getStreamId();
			//TODOne: this returns 'img src=...' for the javascript map; needs to be fixed!
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewsinglestream?geoview=0&stream_id=" + searchTerm;
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
		}
		if(streamType.equals("nearby")){
			//TODO: determine the json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			//TODO: implement this api; using the 'viewall' for now as a placeholder
			//IDEA: add a 'nearby=<gpscoord>' to the map-view API 
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewallstreams?redirect=0";
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
		}
		if(streamType.equals("subscribed")){
			//TODO: determine the json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			//TODO: implement this api; using the 'viewall' for now as a placeholder
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewallstreams?redirect=0";
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
		}
		return jsonRequestURL;
	}
	// TODO: call this from ViewAllStreamsActivity and remove the definition from there
	public String[] loadImagesByStreamType(String streamType){
		return this.loadImagesByStreamType(streamType, null);
	}
	public String[] loadImagesByStreamType(String streamType, String jsonRequestURL){
		Log.i(this.getClass().getSimpleName(),  "in method: loadImagesByStreamType");
		String[] thumbUrlStrArray = null;
		//TODO: create simple URL to retrieve all images for a stream, all covers for all streams, etc
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
		if(streamType.equals("search")){
			//TODO: make json format: [{"streamid":"<streamid>","coverurl":"http://<url>"},]
			//json format: [{"id":"<streamid>", -NO:- "coverurl":"http://<url>"},]
//			jsonRequestURL = "http://fizzenglorp.appspot.com/viewallstreams?redirect=0";
			Log.i(this.getClass().getSimpleName(),  "jsonRequestURL for " + streamType + ": " + jsonRequestURL.toString());
			// Get the JSON Array or Object
			CustomJson customJsonObj = new CustomJson();
			JSONArray jsonImgArray = customJsonObj.getJsonArray(jsonRequestURL);
			// Get data from the JSON Array or Object
//			CustomJsonBilderapp customJsonBilderappObj = new CustomJsonBilderapp();
			thumbUrlStrArray = this.parseJsonFromViewAllStreams(jsonImgArray);
		}
		// caveat: don't call this without an argument
		if(streamType.equals("single")){
			String searchTerm = null;
			//caveat: if none specified this is not going to be fun
			//TODO:			searchTerm = getStreamId();
			//TODOne: this returns 'img src=...' for the javascript map; needs to be fixed!
			jsonRequestURL = "http://fizzenglorp.appspot.com/viewsinglestream?geoview=0&stream_id=" + searchTerm;
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
}