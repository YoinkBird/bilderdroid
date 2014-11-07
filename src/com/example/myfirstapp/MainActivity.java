package com.example.myfirstapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(true){
			// Set the text view as the activity layout
			setContentView(R.layout.fragment_login);
			setupButtonListeners();
		}
		else{
			setContentView(R.layout.activity_main);
			if (savedInstanceState == null) {
				getFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment()).commit();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_search:
				//openSearch();
				return true;
			case R.id.action_settings:
				//openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	/** Called when the user clicks the Send button */
	public void sendSearchQuery(View view){
		Intent intent = new Intent(this, SearchStreamsActivity.class);
		String message = "";
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	/** Called when the user clicks the Send button */
	public void startViewAllStreamsActivity(View view){
		Intent intent = new Intent(this, ViewAllStreamsActivity.class);
		String message = "android";
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

   private void setupButtonListeners(){
     // Add a listener to the 'view streams' button
     Button viewAllStreamsButton = (Button) findViewById(R.id.button_view_all_streams);
     viewAllStreamsButton.setOnClickListener(
         new View.OnClickListener() {
           @Override
           public void onClick(View v) {
        	   //TODO
        	   startViewAllStreamsActivity(v);
           }
         }
         );
     // Add a listener to the 'Login' button
     Button loginGmailButton = (Button) findViewById(R.id.button_login_gmail);
     loginGmailButton.setOnClickListener(
         new View.OnClickListener() {
           @Override
           public void onClick(View v) {
        	   //TODO
           }
         }
         );
   }
}
