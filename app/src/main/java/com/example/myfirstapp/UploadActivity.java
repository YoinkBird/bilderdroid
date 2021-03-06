package com.example.myfirstapp;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.TextView;

/*
Gallery Tutorial from: http://viralpatel.net/blogs/pick-image-from-galary-android-app/
 */
public class UploadActivity extends Activity {

	private Bitmap bitmap;
	private static int RESULT_LOAD_IMAGE = 1; // for gallery intent
	private String globalFilePathString = null;
	private String mStreamId = null;

	// http://developer.android.com/guide/topics/media/camera.html#preview-layout
	//   last code sample before 'Capturing Pictures'
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_upload);
		mStreamId = getStreamId();
		updateStreamTextView();
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
            globalFilePathString = picturePath;
            cursor.close();
             
            // Display filepath as confirmation
            TextView fileUploadTextView = (TextView) findViewById(R.id.textView_upload_filename);
            fileUploadTextView.setText("File:" + picturePath);
            // NOTE: does not work correctly; device runs out of memory
            ImageView imageView = (ImageView) findViewById(R.id.imageView_upload_img_preview);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setImageBitmap(
            	    decodeSampledBitmapFromResource(getResources(), picturePath, 100, 100));
            
            // TODO: enable 'upload' button (it's not "disabled" by default yet)
            
         
        }
     
     
    }
    public static int calculateInSampleSize(
    		BitmapFactory.Options options, int reqWidth, int reqHeight) {
    	// Raw height and width of image
    	final int height = options.outHeight;
    	final int width = options.outWidth;
    	int inSampleSize = 1;

    	if (height > reqHeight || width > reqWidth) {

    		final int halfHeight = height / 2;
    		final int halfWidth = width / 2;

    		// Calculate the largest inSampleSize value that is a power of 2 and keeps both
    		// height and width larger than the requested height and width.
    		while ((halfHeight / inSampleSize) > reqHeight
    				&& (halfWidth / inSampleSize) > reqWidth) {
    			inSampleSize *= 2;
    		}
    	}

    	return inSampleSize;
    }
    // adapted from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
    public static Bitmap decodeSampledBitmapFromResource(Resources res, String imgPath,
    		int reqWidth, int reqHeight) {

    	// First decode with inJustDecodeBounds=true to check dimensions
    	final BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
//    	BitmapFactory.decodeResource(res, resId, options);
    	BitmapFactory.decodeFile(imgPath, options);

    	// Calculate inSampleSize
    	options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    	// Decode bitmap with inSampleSize set
    	options.inJustDecodeBounds = false;
//    	return BitmapFactory.decodeResource(res, resId, options);
    	return BitmapFactory.decodeFile(imgPath, options);
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
						CustomUpload.postImage(globalFilePathString, mStreamId);
					}
				}
				);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	// determine whether streamId specified, if so, return it
	//caveat: if none specified this is not going to be fun
	private String getStreamId (){
		String streamId = null;
		// get intent, check if null
		Intent intent = getIntent();
		String streamIdSelector = intent.getStringExtra(ViewAllStreamsActivity.STREAMID_SELECTOR);
		if(streamIdSelector != null){
			streamId = streamIdSelector;
		}
		return streamId;
	}
	private String updateStreamTextView(){
		if(mStreamId != null){
			return updateStreamTextView(mStreamId);
		}
		return updateStreamTextView(getStreamId());
	}
	private String updateStreamTextView(String streamid){
		TextView uploadTextView_title = (TextView) findViewById(R.id.textView_upload_stream_title);
		String titleString = (String) uploadTextView_title.getText();
		titleString += streamid;
		uploadTextView_title.setText(titleString);
		return titleString;
	}
}
