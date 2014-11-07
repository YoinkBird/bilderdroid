package com.example.myfirstapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

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
}