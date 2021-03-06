package com.example.myfirstapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CustomUpload {
	Context mContextCustomUpload = null;

	public CustomUpload(Context context) {
		mContextCustomUpload = context;
		// TODO Auto-generated constructor stub
	}
	public CustomUpload() {
		// TODO Auto-generated constructor stub
	}
	public static void postImage(String filePath){
		postImage(filePath,null);
	}
	public static void postImage(String filePath, String streamId){
		if(streamId == null){
			streamId = "android_upload_test";
		}
		// http://loopj.com/android-async-http/ 
		// search for "Uploading Files with RequestParams"
	    File imgFile = new File(filePath);
	    RequestParams params = new RequestParams();
	    try {
			params.put("streamid", streamId);
			params.put("files", imgFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // http://loopj.com/android-async-http/doc/com/loopj/android/http/AsyncHttpClient.html
	    AsyncHttpClient client = new AsyncHttpClient();
	    String uploadUrl = null;
	    // java.net.MalformedURLException: No valid URI scheme was provided // uploadUrl = "fizzenglorp.appspot.com/blueimp_upload?streamid=android_upload_test";
	    //TODO: move to string resource
//	    uploadUrl = "http://fizzenglorp.appspot.com/blueimp_upload?streamid=android_upload_test";
//	    uploadUrl = "http://fizzenglorp.appspot.com/blueimp_upload";
	    uploadUrl = getUploadUrl(streamId);
	    Log.d("CustomUpload", "upload attempt " + params.toString());
	    Log.d("CustomUpload", "file name: " + imgFile.getPath());
	    client.post(uploadUrl, params, new AsyncHttpResponseHandler() {
	    	/*
	        @Override
	        public void onSuccess(String response) {
	            Log.w("async", "success!!!!");
	        }
	        */

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				String errorString = "";
        	   Log.d(this.getClass().getSimpleName(), "upload failure " + statusCode);
        	   error.printStackTrace(System.out);
				
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
        	    Log.d(this.getClass().getSimpleName(), "upload success! ");
				String content = null;
				try {
					content = new String(responseBody, "UTF-8"); // from http://stackoverflow.com/q/26787928
					Log.d(this.getClass().getSimpleName(), "response:" + content);
//					sendDataToUrl(content);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	    });
	}  
	public static String getUploadUrl(String streamId){
//		String blobstoreGenUrl = mContextCustomUpload.getString(R.string.blobstore_generation_url);
		String blobstoreGenUrl = "http://fizzenglorp.appspot.com/getblobstoreurl";
		blobstoreGenUrl += "?streamid=" + streamId;
		CustomJson httpSendObj = new CustomJson();
		Log.d("CustomUpload", "retrieval requested for: " + blobstoreGenUrl);
		String imgUploadURL = httpSendObj.sendHttpRequest(blobstoreGenUrl);
		Log.d("CustomUpload", "retrieval result: " + imgUploadURL);
		return imgUploadURL;
	}
}

/*
new AsyncHttpResponseHandler() {
	        @Override
	        public void onSuccess(String response) {
	            Log.w("async", "success!!!!");
	        }
	    }
*/