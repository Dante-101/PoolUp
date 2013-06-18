package com.example.firstapp;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.asynctasks.RetreiveMapTask;
import com.example.connections.HitClient;
import com.example.firstapp.R;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;



public class DisplayMessageActivity extends FragmentActivity {
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	private static final LatLng MOUNTAIN_VIEW = new LatLng(37.4, -122.1);
	HitClient client = new HitClient();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_location_demo);
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(MOUNTAIN_VIEW)      // Sets the center of the map to Mountain View
		.zoom(9).build() ;                  // Sets the zoom
		// Sets the tilt of the camera to 30 degree.build();                   // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		LatLng cord=null;

		try {
			cord = getLatAndLong("http://maps.google.com/maps/api/geocode/json?address=sanjose&sensor=false");
		} catch (Exception e) {
			Log.i("Exception",e.getMessage());
		}
		if(cord!=null)
			Log.i("Cordinate",""+cord.latitude+" "+cord.longitude);
		setupMap();
		displayPath("SanFranscisco","MountainView","driving");
	}

	@Override
	protected void onResume() {
		super.onResume();

		LatLng cord=null;
		try {
			cord = getLatAndLong("http://maps.google.com/maps/api/geocode/json?address=sanjose&sensor=false");
		} catch (Exception e) {
			Log.i("Exception",e.getMessage());
		} 
		if(cord!=null)
			Log.i("Cordinate",""+cord.latitude+" "+cord.longitude);
		setupMap();
		displayPath("SanFranscisco","MountainView","driving");

	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if(mLocationClient!=null)
		mLocationClient.disconnect();
	}


	public void  displayPath(String source ,String destination,String mode)
	{
		Log.i("Introduction","Hello i am called");
		try{
			String uri = client.makeURL(source, destination, mode);
			String [] urls = new String[1]; 
			urls[0]=uri;
			AsyncTask<String,Void,String> jsonResponse = new RetreiveMapTask().execute(urls); 
			String json = jsonResponse.get();
			Log.i("response json",json);
			drawPath(json);
		}catch(Exception e)
		{
			System.out.println("Got Exception "+e);
			Log.i("Exception ",""+e);
			e.printStackTrace();
		}

	}

	public LatLng getLatAndLong(String uri) throws InterruptedException, ExecutionException, JSONException{
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



	public void drawPath(String  result) {


		List<LatLng> list ;

		try {
			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
			String encodedString = overviewPolylines.getString("points");
			list = decodePoly(encodedString);

			if(list.size() <=0)
				mMap.addMarker(new MarkerOptions().position(new LatLng(37.777897,-122.056353)).title("sanfran"));

			for(int z = 0; z<list.size()-1;z++){
				LatLng src= list.get(z);
				LatLng dest= list.get(z+1);
				Log.i("deb","asa");
				Log.i("src","zz "+src.longitude+" "+src.latitude);
				Log.i("dest",dest.longitude+" "+dest.latitude);	
				Polyline line = mMap.addPolyline(new PolylineOptions()
				.add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
				.width(5)
				.color(Color.RED));
			}

		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	} 

	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng( (((double) lat / 1E5)),
					(((double) lng / 1E5) ));
			poly.add(p);
		}

		return poly;
	}




	public void setupMap()
	{

		Log.i("check","PRINT YESS");
		//			mMap.addMarker(new MarkerOptions().position(new LatLng(37.77503150,-122.06848490)).title("sanfran"));
		//			Polyline line = mMap.addPolyline(new PolylineOptions()
		//			.add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
		//			.width(5)
		//			.color(Color.RED));
	}

}


