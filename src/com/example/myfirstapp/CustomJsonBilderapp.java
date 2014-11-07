package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CustomJsonBilderapp {

	public CustomJsonBilderapp() {
		// TODO Auto-generated constructor stub
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
}