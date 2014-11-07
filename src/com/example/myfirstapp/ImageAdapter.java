package com.example.myfirstapp;

import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	// Why 'm'Everything? "Non-public, non-static field names start with m."
	// src: https://source.android.com/source/code-style.html#follow-field-naming-conventions
    private Context mContext;
    private Integer[] mThumbIds;
    private String[] mThumbUrls;
    
    /*
    // test array of images
    private Integer[] mTestThumbIds = {
    		R.drawable.sample_2, R.drawable.sample_3,
    		R.drawable.sample_4, R.drawable.sample_5,
    		R.drawable.sample_6, R.drawable.sample_7,
    		R.drawable.sample_0, R.drawable.sample_1,
    		R.drawable.sample_2, R.drawable.sample_3,
    		R.drawable.sample_4, R.drawable.sample_5,
    		R.drawable.sample_6, R.drawable.sample_7,
    		R.drawable.sample_0, R.drawable.sample_1,
    		R.drawable.sample_2, R.drawable.sample_3,
    		R.drawable.sample_4, R.drawable.sample_5,
    		R.drawable.sample_6, R.drawable.sample_7
    };
    */

    //Constructor
    public ImageAdapter(Context c) {
        mContext = c;
        //this.setThumbIds(mTestThumbIds);
    }

    public int getCount() {
    	int length = 0;
    	if(mThumbIds != null){
    		length = mThumbIds.length;
    	}else if(mThumbUrls != null){
    		length = mThumbUrls.length;
    	}
        return length;
    }

    /* Normally, getItem(int) should return the actual object 
     *   at the specified position in the adapter, 
     * but it's ignored for this example. 
     * 
     * (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return null;
    }

    //  getItemId(int) should return the row id of the item, but it's not needed here.
    public long getItemId(int position) {
        return 593; //relatively uncommon value to see if this function is getting called
    }

    // TODO: yo man, make this legit instead of returning some hard-coded junk, yeah?
    public String getStreamId(int position) {
    	String streamId = "android_grid_test";
    	streamId = "grass";
        return streamId;
    }
    
    /* define the array of images 
     * 
     */
    public void setThumbIds(Integer[] mPassThumbIds){
    	// references to our images
    	mThumbIds = mPassThumbIds;
    }
    public void setThumbUrls(String[] mPassThumbIds){
    	// references to our images
    	mThumbUrls = mPassThumbIds;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

    	if(mThumbIds != null){
    		imageView.setImageResource(mThumbIds[position]);
    	}else if(mThumbUrls != null){
    		if(false){
    			Bitmap downloadedBitmap = null;
    			/* BEGIN Quick HACK */
    			//TODO: 
    			// DO NOT USE THIS IN PRODUCTION
    			downloadedBitmap = crappyImgDownload(mThumbUrls[position]);
    			/* END Quick HACK */
    			imageView.setImageBitmap(downloadedBitmap );
    		}else{
    			new RetrieveImgTask(imageView).execute(mThumbUrls[position]);
    		}
    	}


        return imageView;
    }
    
    private Bitmap crappyImgDownload(String imgurlString){
        URL url;
        Bitmap bmp = null;
        try {
        	// TODO: put on background thread
        	// TODO: 
        	// DO NOT USE THIS IN PRODUCTION
        	// see overall thread at http://stackoverflow.com/a/9289190
        	// Just for testing, allow network access in the main thread
        	// NEVER use this is productive code
        	StrictMode.ThreadPolicy policy = new StrictMode.
        			ThreadPolicy.Builder().permitAll().build();
        	StrictMode.setThreadPolicy(policy); 

        	url = new URL(imgurlString);
        	bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return bmp;
    	
    }

}