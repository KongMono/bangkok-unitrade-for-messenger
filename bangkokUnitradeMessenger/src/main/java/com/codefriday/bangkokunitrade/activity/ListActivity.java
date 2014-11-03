package com.codefriday.bangkokunitrade.activity;

import com.codefriday.bangkokunitrade.util.Util;
import com.codefriday.bangkokunitrade.util.gpsTracker;
import com.codefriday.bangkokunitrade.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ListActivity extends Activity implements OnClickListener{
	private Button btnSenditem;
	private Button btnReceiveditem;
	boolean isOnline = false;
	gpsTracker gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		init();
		
		try {
			if(gps.canGetLocation()){
	        	Log.d("latitude", String.valueOf(gps.getLatitude()));
	        	Log.d("longitude", String.valueOf(gps.getLongitude()));
	        	Toast.makeText(getApplicationContext(),
	        			String.valueOf(gps.getLatitude())+"/"+String.valueOf(gps.getLongitude()),
	        			Toast.LENGTH_LONG).show();
	        }else{
	        	gps.showSettingsAlert();
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void init() {
		
		isOnline = Util.isOnline(this);
		
		btnSenditem = (Button) findViewById(R.id.button1);
		btnReceiveditem = (Button) findViewById(R.id.button2);

		btnSenditem.setOnClickListener(this);
		btnSenditem.setTypeface(Util.getBoldFont(this));
		btnSenditem.setTextSize(30f);			

		btnReceiveditem.setOnClickListener(this);
		btnReceiveditem.setTypeface(Util.getBoldFont(this));
		btnReceiveditem.setTextSize(30f);
		
		gps = new gpsTracker(this);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			Intent intent = new Intent(this,ListStockActivity.class);
			startActivity(intent);
			break;
		case R.id.button2:
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		gps.stopUsingGPS();
	}

}
