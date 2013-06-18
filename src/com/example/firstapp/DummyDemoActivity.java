package com.example.firstapp;


import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.firstapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class DummyDemoActivity extends FragmentActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	private String userId;
	private String baseUri = "http://192.168.125.138:4567/registerTrip";
	private String geoCodeUri = "http://maps.google.com/maps/api/geocode/json";
	public final static String EXTRA_MESSAGE = "com.example.firstapp.MESSAGE";
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private LocationClient mLocationClient; 
	private TextView sourceLocation;
	private TextView destLocation;
	private TimePicker time;
	// Define a DialogFragment that displays the error dialog
	@SuppressLint("NewApi")
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;
		// Default constructor. Sets the dialog field to null
		@SuppressLint("NewApi")
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}
		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}
		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent(); 
		userId = intent.getStringExtra(FrontActivity.EXTRA_MESSAGE);
		mLocationClient = new LocationClient(this, this, this);
		
		setContentView(R.layout.activity_dummy);
		sourceLocation = (TextView) findViewById(R.id.source);
		destLocation = (TextView) findViewById(R.id.destination);
		time = (TimePicker)findViewById(R.id.timePicker1);

	}
	public LatLng getLatAndLong(String uri) throws InterruptedException, ExecutionException, JSONException
	{
		String [] urls = new String[1]; 

		urls[0]=uri;
		AsyncTask<String,Void,String> jsonResponse = new RetreiveMapTask().execute(urls); 
		String json = jsonResponse.get();
		JSONObject jsonObject = new JSONObject(json);
		double longitude = ((JSONArray)jsonObject.get("results")).
				getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

		double latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
				.getJSONObject("geometry").getJSONObject("location")
				.getDouble("lat");
		return new LatLng(latitude,longitude); 
	}

	@SuppressWarnings("deprecation")
	public void sendTripRequest(View view) { 
		String source = sourceLocation.getText().toString(); 
		String destination =  destLocation.getText().toString();
		double hours = time.getCurrentHour();
		double mins = time.getCurrentMinute();
		//Asnc this part
		Record record = new Record(); 
		record.setDriver(true); 
		record.setRider(false); 
		record.setTime(hours*60+mins);
		record.setUserId(userId);
		LatLng src=null;
		LatLng dest = null;
		try {
			source=URLEncoder.encode(source,"utf-8");
			destination=URLEncoder.encode(destination,"utf-8");

			src = getLatAndLong(geoCodeUri+"?address="+source+"&sensor=false");
			dest = getLatAndLong(geoCodeUri+"?address="+destination+"&sensor=false");
		}catch(Exception e)
		{ 
			//Log.i("Exception",e.getMessage());
			System.out.println("Exception "+e);
		}
		record.setSrcLat(src.latitude);
		record.setSrcLong(src.longitude);
		record.setDestLat(dest.latitude);
		record.setDestlong(dest.longitude);
		String[] urls={baseUri,"payload",new Gson().toJson(record)};
		new InnerTask().execute(urls); 
		try {

		}catch(Exception e) { 

		}
	}

	class InnerTask extends AsyncTask<String, Void,String> {

		protected String doInBackground(String... urls) {
			try {

				HitClient client = new HitClient();
				String params = null; 
				String payload = null;

				if(urls.length>1) { 
					params = urls[1];
				}
				if(urls.length>2) {
					payload = urls[2];
				}
				String json = client.sendGetRequest(urls[0], params,payload);
				return json;
			} catch (Exception e) {
				Log.i("Exception "," "+e+" \n\n");
				return null;
			}
		}

		protected void onPostExecute(String feed) {
			// TODO: check this.exception 
			// TODO: do something with the feed
		}
	}


	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		String message = "whatever";
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);

	}
	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
		Log.i("Status ","called");
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode =
				GooglePlayServicesUtil.
				isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates",
					"Google Play services is available.");
			// Continue
			System.out.println("fuck yeah");
			return true;
			// Google Play services was not available for some reason
		} else {
			// Get the error code
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode,
					this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment =
						new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				//  errorFragment.show(getSupportFragmentManager(), "tag")
			}
			System.out.println("fuck");
			return false;
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		//LocationUtils.getLatLng(this, mLocationClient.getLastLocation());
		Geocoder coder = new Geocoder(this); 
		try {
			List<Address> list = coder.getFromLocation(mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude(), 4);
			String ans = " "; 
			for(Address a:list) { 
				int m = a.getMaxAddressLineIndex();
				for(int k=0;k<m;k++){ 
					ans+=a.getAddressLine(k)+" ";
				}
				break;
			}
			sourceLocation.setText(ans);
		}catch(Exception e) { 

		}
	}

	/*
	 * Called by Location Services if the connection to the
	 * location client drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * Called by Location Services if the attempt to
	 * Location Services fails.
	 */

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

		/*
		 * Google Play services can resolve some errors it detects.
		 * If the error has a resolution, try sending an Intent to
		 * start a Google Play services activity that can resolve
		 * error.
		 */
		if (connectionResult.hasResolution()) {
			try {

				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(
						this,
						LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */

			} catch (IntentSender.SendIntentException e) {

				// Log the error
				e.printStackTrace();
			}
		} else {

			// If no resolution is available, display a dialog to the user with the error.
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	private void showErrorDialog(int errorCode) {

		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
				errorCode,
				this,
				LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null) {

			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);

			// Show the error dialog in the DialogFragment
			errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
		}
	}

}
