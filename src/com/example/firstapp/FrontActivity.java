package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class FrontActivity extends Activity {
	public final static String EXTRA_MESSAGE="com.example.firstapp.USERID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_front);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.front, menu);
		return true;
	}
	
	@Override
	public void onPause() { 
		super.onPause();
		EditText userName=(EditText)findViewById(R.id.editText1);
		userName.setText("");
		userName.setHint("username");
		setContentView(R.layout.activity_front);
	}
	
	public void sendDriver(View view) { 
		Intent intent = new Intent(this,DummyDemoActivity.class); 
		EditText userName=(EditText)findViewById(R.id.editText1);
		String userId = userName.getText().toString(); 
		intent.putExtra(EXTRA_MESSAGE, userId);
		startActivity(intent);
		
		Intent service = new Intent(this,UpdateLocationService.class);
		this.startService(service);
		
	}
	
	public void sendRider(View view) { 
		Intent intent = new Intent(this,LoaderActivity.class); 
		EditText userName=(EditText)findViewById(R.id.editText1);
		String userId = userName.getText().toString(); 
		intent.putExtra(EXTRA_MESSAGE, userId);
		startActivity(intent);
	}

}
