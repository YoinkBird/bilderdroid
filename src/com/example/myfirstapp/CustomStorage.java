package com.example.myfirstapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class CustomStorage {

	public CustomStorage() {
		// TODO Auto-generated constructor stub
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
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "test.jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	    Log.d("MyCameraApp", "file name: " + mediaFile.getPath());

	    return mediaFile;
	}
   public void storeImage(Bitmap bitmap){
     CustomStorage storeFileObj = new CustomStorage();
     File imgFile = storeFileObj.getOutputMediaFile(1);
     // http://stackoverflow.com/a/673014
     FileOutputStream imgOutStream = null;
     try {
       imgOutStream = new FileOutputStream(imgFile);
       bitmap.compress(Bitmap.CompressFormat.JPEG,90, imgOutStream);
       //Log.d("MyCameraApp", "imgOutStream: " + imgOutStream.toString());
       imgOutStream.flush();
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
     }
	    Log.d("CustomStorage - storeImage", "file name: " + imgFile.getPath());
//     Toast.makeText(getApplicationContext(), imgFile.getPath(), Toast.LENGTH_LONG).show();
   }

}
