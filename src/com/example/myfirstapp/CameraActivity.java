package com.example.myfirstapp;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

//source: http://www.tutorialspoint.com/android/android_camera.htm
/*
//TODO: tutorials at
http://www.airpair.com/android/android-camera-development
http://www.airpair.com/android/android-camera-surface-view-fragment

http://www.androidhive.info/2013/09/android-working-with-camera-api/

http://developer.android.com/guide/topics/media/camera.html#custom-camera
*/
public class CameraActivity extends Activity {

   private Camera cameraObject;
   private CameraPreview showCamera;
   private Bitmap bitmap;
   
   // http://developer.android.com/guide/topics/media/camera.html#access-camera
   public static Camera isCameraAvailiable(){
      Camera object = null;
      try {
         object = Camera.open(); 
      }
      catch (Exception e){
      }
      return object; 
   }

   //TODO: 'restart preview' after picture is taken
   // http://developer.android.com/training/camera/cameradirect.html#TaskRestartPreview
   private PictureCallback capturedIt = new PictureCallback() {

      @Override
      public void onPictureTaken(byte[] data, Camera camera) {

         bitmap = BitmapFactory.decodeByteArray(data , 0, data .length);
         if(bitmap==null){
           Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
         }
         else
         {
           Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
         }
         // http://developer.android.com/guide/topics/media/camera.html#release-camera
         cameraObject.release();
       }
   };

   // http://developer.android.com/guide/topics/media/camera.html#preview-layout
   //   last code sample before 'Capturing Pictures'
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_camera);
      setupButtonListeners();
      cameraObject = isCameraAvailiable();
      showCamera = new CameraPreview(this, cameraObject);
      FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
      preview.addView(showCamera);
   }
   public void snapIt(View view){
      cameraObject.takePicture(null, null, capturedIt);
      //TODO: enable the 'use this picture' button
   }
   

   private void setupButtonListeners(){
     // Add a listener to the 'Use this Picture' button
     Button savePictureButton = (Button) findViewById(R.id.button_camera_save_picture);
     savePictureButton.setOnClickListener(
         new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             // store the bitmap
             storeImage();
           }
         }
         );
     //TODO: move this to the correct view
     // Add a listener to the 'Upload Picture' button
     Button uploadPictureButton = (Button) findViewById(R.id.button_upload_picture);
     uploadPictureButton.setOnClickListener(
         new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             // Upload hard-coded file-name
             CustomUpload.postImage();
           }
         }
         );
     // Add a listener to the 'View Streams' button
     Button viewAllStreamsButton = (Button) findViewById(R.id.button_upload_view_all_streams);
     if(viewAllStreamsButton != null){
       viewAllStreamsButton.setOnClickListener(
           new View.OnClickListener() {
             @Override
             public void onClick(View v) {
               //TODO
               Intent intent = new Intent(v.getContext(), ViewAllStreamsActivity.class);
               startActivity(intent);
             }
           }
           );
     }
   }
   
   public void storeImage(){
	   storeImage(this.bitmap);
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
     Toast.makeText(getApplicationContext(), imgFile.getPath(), Toast.LENGTH_LONG).show();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }
}
