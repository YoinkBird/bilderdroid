package com.example.myfirstapp;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;

import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CustomUpload {

	public CustomUpload() {
		// TODO Auto-generated constructor stub
	}
	// For texting, backwards compat only!
	public static void postImage(){
	    postImage(Environment.getExternalStorageDirectory().getPath() + "/Pictures/MyCameraApp/test.jpg");
	}
	public static void postImage(String filePath){
		// http://loopj.com/android-async-http/ 
		// search for "Uploading Files with RequestParams"
	    File imgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/MyCameraApp/test.jpg");
	    RequestParams params = new RequestParams();
	    try {
			params.put("streamid", "android_upload_test");
			params.put("files", imgFile);
			params.put("files", new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/MyCameraApp/test.jpg"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // http://loopj.com/android-async-http/doc/com/loopj/android/http/AsyncHttpClient.html
	    AsyncHttpClient client = new AsyncHttpClient();
	    String uploadUrl = null;
	    // java.net.MalformedURLException: No valid URI scheme was provided // uploadUrl = "fizzenglorp.appspot.com/blueimp_upload?streamid=android_upload_test";
//	    uploadUrl = "http://fizzenglorp.appspot.com/blueimp_upload?streamid=android_upload_test";
	    uploadUrl = "http://fizzenglorp.appspot.com/blueimp_upload";
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
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
        	   Log.d(this.getClass().getSimpleName(), "upload success! ");
				
			}
	    });
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