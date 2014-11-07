package com.example.myfirstapp;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


/*
 * TODO: set up "concurrency" for more efficient gridview
 * http://developer.android.com/training/displaying-bitmaps/process-bitmap.html#concurrency
 * TODO: set up caching
 * http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 */

//									 AsyntTask<Params, Progress, Result>
public class RetrieveImgTask extends AsyncTask<String, Void, Bitmap>{
    private final WeakReference<ImageView> imageViewReference;

	private Exception imgException;
	
	// "bind" to a given view
    public RetrieveImgTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }
	
    // load image in background
    // coding reminder: varargs - http://stackoverflow.com/a/13665935
	protected Bitmap doInBackground(String... urls){
        URL url;
        Bitmap bmp = null;
        try {
        	url = new URL(urls[0]);
        	bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return bmp;
		
	}

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}