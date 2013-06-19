package com.example.firstapp;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.connections.ConnectionsManager;
import com.example.connections.HitClient;
import com.example.datatypes.Record;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class LoaderActivity extends ListActivity implements OnClickListener {
	Button btn;
	HitClient client = new HitClient();
	String uri=ConnectionsManager.db_url+"users.json";

	TextView heading;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_loader);
	}


	public void onClick(View view){
		//detect the view that was "clicked"
		switch(view.getId())
		{
		case R.id.button2:
			new LongOperation(this,null).execute("");
			break;

		}

	}
	public List<String> getPotentialDrivers()
	{
		List<String> output = new ArrayList<String>();
		try {
			String json =client.sendGetRequest(uri, null, null);
			JSONObject js = new JSONObject(json);
			Iterator it = js.keys();
			while(it.hasNext())
			{

				String name = (String)it.next();
				JSONObject res = (JSONObject)js.get(name);
				output.add(res.toString().toString()); 
			}
			return output;     

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}

	public void submit(View view) {

		String[] messages =new String[2]; 
		AsyncTask task = new LongOperation(this,messages).execute();

	}

	private class LongOperation extends AsyncTask<String, Void, String> {
		ListActivity activity;
		private Context context;
		private ProgressDialog dialog;
		List<String> messages;
		String[] msgs;
		public LongOperation(ListActivity activity,String[] msgs)  {
			this.activity = activity;
			context = activity;
			dialog = new ProgressDialog(context);
			this.msgs=msgs;
			messages = new ArrayList<String>(); 

		}
		@Override
		protected String doInBackground(String... params) {
			messages = getPotentialDrivers();
			return "Executed";
		}      

		@Override
		protected void onPostExecute(String result) {
			List<Record> titles = new ArrayList<Record>(messages.size());
			Gson gson = new Gson();
			for (String msg : messages){
				Record rec = gson.fromJson(msg,Record.class);
				Log.i("User",rec.getUserId());
				titles.add(rec); 
			}

			ArrayAdapter<Record> adapter =new InteractiveArrayAdapter(activity,titles);
			activity.setListAdapter(adapter);
			adapter.notifyDataSetChanged();

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

		}

		@Override
		protected void onPreExecute() {
			dialog.setTitle("Loading");
			dialog.setMessage("Wait while loading...");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

}