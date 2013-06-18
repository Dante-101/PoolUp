package com.example.firstapp;

import android.os.AsyncTask;
import android.util.Log;


class RetreiveMapTask extends AsyncTask<String, Void,String> {

    private Exception exception;

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
      
    }
 }