package com.example.myfirstapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class CustomStorage {
//	http://stackoverflow.com/a/14681859
	Context mContextCustomStorage = null;

	public CustomStorage(Context context) {
		// TODO Auto-generated constructor stub
		mContextCustomStorage = context;
		Log.d("CustomStorage - constructor", "context:" + mContextCustomStorage.getPackageName());
	}
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
//	private static Uri getOutputMediaFileUri(int type){
	public static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
//	private static File getOutputMediaFile(int type){
	public static File getOutputMediaFile(int type){
		//TODO: read "Checking media availability" at
		// http://developer.android.com/guide/topics/data/data-storage.html#filesExternal

	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

           Log.d("MyCameraApp", "file storage dir: " + mediaStorageDir.getPath());
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	    Log.d("MyCameraApp", "file name: " + mediaFile.getPath());

	    return mediaFile;
	}
   public String storeImage(Bitmap bitmap){
     File imgFile = this.getOutputMediaFile(1);
     // http://stackoverflow.com/a/673014
     FileOutputStream imgOutStream = null;
     try {
       imgOutStream = new FileOutputStream(imgFile);
       bitmap.compress(Bitmap.CompressFormat.JPEG,90, imgOutStream);
       //Log.d("MyCameraApp", "imgOutStream: " + imgOutStream.toString());
       imgOutStream.flush();
       // register file so it shows up in Gallery
	   Log.d("CustomStorage - storeImage", "file object name: " + imgFile.getPath());
	   if(true){ // this works in phone, but doesn't show up over USB so I am using both
		   runMediaScanner(imgFile);
	   }
	   else if(true){
		   // oh god, what have I done?
		   // http://stackoverflow.com/a/22498023
		   // oh well, other method not working yet.
		   // is there an eternal punishment waiting for those who do this?
    	   Uri uri = Uri.fromFile(imgFile);
    	   Log.d("CustomStorage - storeImage", "file uri name: " + uri.getPath());
    	   Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
    	   Log.d("CustomStorage - storeImage", "file intent name: " + intent);
    	   mContextCustomStorage.sendBroadcast(intent);
       }
     } catch (IOException e) {
       // TODO Auto-generated catch block
       e.printStackTrace();
     } finally{
       try {
         if(imgOutStream != null){
           imgOutStream.close();
         }
       } catch (IOException e){
         e.printStackTrace();
       }
       // risky - free up memory to avoid some of the erorrs I keep getting
//       bitmap.recycle();
       // "remove data" to free up memory to avoid some of the erorrs I keep getting
       bitmap = null;
     }
	    Log.d("CustomStorage - storeImage", "file name: " + imgFile.getPath());
//     Toast.makeText(getApplicationContext(), imgFile.getPath(), Toast.LENGTH_LONG).show();
	 return imgFile.getPath();
   }
   
//   public void runMediaScanner(String filePath){
   public void runMediaScanner(File file){
	   // this works in phone but does not appear to show up in USB
	   Log.d("CustomStorage - runMediaScanner", "file object name: " + file.getPath());
	   MediaScannerConnection.scanFile(
			   mContextCustomStorage,
			   new String[] { file.toString() }, 
			   null,
			   new MediaScannerConnection.OnScanCompletedListener() {
				   public void onScanCompleted(String path, Uri uri) {
					   Log.i("ExternalStorage", "Scanned " + path + ":");
					   Log.i("ExternalStorage", "-> uri=" + uri);
				   }
			   });
	   return;
   }

}
