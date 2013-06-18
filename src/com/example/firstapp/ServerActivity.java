package com.example.firstapp;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class ServerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listenForNotifications();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void listenForNotifications ()
	{
		try {
		 final int port = 12345; 
         ServerSocket mServerSocket = new ServerSocket(port); 
         
         Log.i("hello","world");
         if(mServerSocket!=null)
         Log.i("Host Address",mServerSocket.getInetAddress().getHostAddress());
           
         while(true){ 
        	
            // System.out.println("Running server on port : " + Integer.toString(port)); 
             Socket mSocket = mServerSocket.accept(); 
             DataInputStream inData = new DataInputStream(mSocket.getInputStream()); 
             String inJSON = inData.readUTF(); 
               
             DataOutputStream outData = new DataOutputStream(mSocket.getOutputStream()); 
             String outJSON = outData.toString(); 
               
             mSocket.close(); 
         }}catch(Exception e)
         {
        	 Log.i("Exception",e.getMessage());
         }
		}
	
	

}
