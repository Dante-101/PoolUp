package com.example.firstapp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.connections.ConnectionsManager;
import com.example.connections.HitClient;
import com.example.datatypes.Record;
import com.google.gson.Gson;

public class LoaderActivity extends ListActivity implements OnClickListener, OnInitListener {
	Button btn;
	HitClient client = new HitClient();
	String uri=ConnectionsManager.db_url+"users.json";
	ProgressDialog dialog;
	TextView heading;
	TextToSpeech tts;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_loader);
		tts = new TextToSpeech(this,this);
	}
	
	@Override
	public void onDestroy() {

		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}
	

	public void onClick(View view){

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

	public void clickUser(Record record) { 
		Log.i("came","here");
		tts.speak(record.getUserId()+" has been sent a bid", TextToSpeech.QUEUE_FLUSH, null);

	}
	public void submit(View view) {
		new LongOperation(this).execute();
	}

	private class LongOperation extends AsyncTask<String, Void, String> {
		ListActivity activity;
		private Context context;
		List<String> messages;
		public LongOperation(ListActivity activity)  {
			this.activity = activity;
			context = activity;

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
			dialog = ProgressDialog.show(activity, "Working..", "Downloading Data...", true, false);

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {

			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

}