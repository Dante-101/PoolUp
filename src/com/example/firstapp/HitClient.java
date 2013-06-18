package com.example.firstapp;



import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;

/**
 * This example demonstrates the use of the {@link ResponseHandler} to simplify
 * the process of processing the HTTP response and releasing associated resources.
 */
public class HitClient {

	HttpClient httpclient = new DefaultHttpClient();


	public String sendGetRequest(String uri,String paramName,String json) throws URISyntaxException, ClientProtocolException, IOException
	{
		HttpGet httpget = new HttpGet(uri);
		if(paramName!=null && json !=null)
		{
			Uri.Builder b = Uri.parse(uri).buildUpon();
			Uri u = b.appendQueryParameter("payload", json).build();
			httpget = new HttpGet(u.toString());
		}

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpclient.execute(httpget, responseHandler);
		return responseBody;

	}


	public String sendPostRequest(String uri,List<NameValuePair> params) throws ClientProtocolException, IOException
	{
		HttpPost httppost = new HttpPost(uri);
		if(params!=null)
			httppost.setEntity(new UrlEncodedFormEntity(params));
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpclient.execute(httppost, responseHandler);
		return responseBody;
	}


	public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ,String mode){
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString
		.append(Double.toString( sourcelog));
		urlString.append("&destination=");// to
		urlString
		.append(Double.toString( destlat));
		urlString.append(",");
		urlString.append(Double.toString( destlog));
		urlString.append("&sensor=false&mode="+mode+"&alternatives=true");
		return urlString.toString();
	}

	public String makeURL (String srcLocation ,String destLocation ,String mode){
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");// from
		urlString.append(srcLocation);
		urlString.append("&destination=");// to
		urlString.append(destLocation);
		urlString.append("&sensor=false&mode="+mode+"&alternatives=true");
		return urlString.toString();
	}  

   
	
	
	

	public final static void main(String[] args) throws Exception {
		System.out.println("HelloWorld");
		HitClient hitserver = new HitClient();
		String uri =hitserver.makeURL("SanFrancisco","MountainView","driving");
		String response = hitserver.sendGetRequest(uri, null,null);
		System.out.println(response);
	}

}
