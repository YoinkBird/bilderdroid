package com.example.myfirstapp;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/*
Gallery Tutorial from: http://viralpatel.net/blogs/pick-image-from-galary-android-app/
 */
public class UploadActivity extends Activity {

	private Bitmap bitmap;
	private static int RESULT_LOAD_IMAGE = 1; // for gallery intent

	// http://developer.android.com/guide/topics/media/camera.html#preview-layout
	//   last code sample before 'Capturing Pictures'
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_upload);
		setupButtonListeners();
	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
             
            // NOTE: does not work correctly; device runs out of memory
            ImageView imageView = (ImageView) findViewById(R.id.imageView_upload_img_preview);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
         
        }
     
     
    }

	private void setupButtonListeners(){
		// Add a listener to the 'Choose from Library' button
		Button choosePictureButton = (Button) findViewById(R.id.button_upload_choose_file);
		if(choosePictureButton != null){
			choosePictureButton.setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(intent, RESULT_LOAD_IMAGE);
						}
					}
					);
		}
		// Add a listener to the 'Use Camera' button
		Button useCameraButton = (Button) findViewById(R.id.button_upload_use_camera);
		if(useCameraButton != null){
			useCameraButton.setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(v.getContext(), CameraActivity.class);
							startActivity(intent);
						}
					}
					);
		}
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
