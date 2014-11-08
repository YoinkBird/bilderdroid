package com.example.myfirstapp;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
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
public class UploadActivity extends Activity {

	private Bitmap bitmap;


	// http://developer.android.com/guide/topics/media/camera.html#preview-layout
	//   last code sample before 'Capturing Pictures'
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_upload);
		setupButtonListeners();
	}


	private void setupButtonListeners(){
		// Add a listener to the 'Use this Picture' button
		Button uploadFileButton = (Button) findViewById(R.id.button_upload_choose_file);
		if(uploadFileButton != null){
			uploadFileButton.setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}
					);
		}
		//TODO: move this to the correct view
		// Add a listener to the 'Upload Picture' button
		Button uploadPictureButton = (Button) findViewById(R.id.button_upload_upload);
		uploadPictureButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// Upload hard-coded file-name
						CustomUpload.postImage();
					}
				}
				);
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
