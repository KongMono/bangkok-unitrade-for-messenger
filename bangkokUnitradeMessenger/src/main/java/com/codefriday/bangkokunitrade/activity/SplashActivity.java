package com.codefriday.bangkokunitrade.activity;

import com.codefriday.bangkokunitrade.R;
import com.codefriday.bangkokunitrade.dataset.UserDataset;
import com.codefriday.bangkokunitrade.util.PreferencesApp;
import com.codefriday.bangkokunitrade.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private Handler mHandler = new Handler();
	private ImageView splashImage,splashName;
	UserDataset dataset = UserDataset.getInstance();

	public void onDestroy() {
		super.onDestroy();
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splashscreen);

		splashImage = (ImageView) findViewById(R.id.splashimage);
		splashName = (ImageView) findViewById(R.id.splashname);
		mHandler.postDelayed(runApp, 1500);
	}

	private void goToNextActivity() {

		PreferencesApp app = new PreferencesApp(this);

		app.getData();

		if (Util.stringIsNotNull(dataset.getUser_id())
				&& Util.stringIsNotNull(dataset.getUser_type())) {
			Intent intent = new Intent();

			switch (Integer.parseInt(dataset.getUser_type())) {
			case UserDataset.Type_System:
				Toast.makeText(getApplicationContext(), "No Content",
						Toast.LENGTH_LONG).show();
				break;

			case UserDataset.Type_Administrator:
				Toast.makeText(getApplicationContext(), "No Content",
						Toast.LENGTH_LONG).show();
				break;

			case UserDataset.Type_Sale:
				Toast.makeText(getApplicationContext(), "No Content",
						Toast.LENGTH_LONG).show();
				break;

			case UserDataset.Type_Product:
				Toast.makeText(getApplicationContext(), "No Content",
						Toast.LENGTH_LONG).show();
				break;

			case UserDataset.Type_Messenger:
				intent = new Intent(this, ListStockActivity.class);
				startActivity(intent);
				break;
				
			case UserDataset.Type_Store:
				intent = new Intent(this, StroreActivity.class);
				startActivity(intent);
				break;

			default:
				Toast.makeText(getApplicationContext(), "No Content",
						Toast.LENGTH_LONG).show();
				break;
			}
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}

		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		boolean r = false;
		if (keyCode == 4) {
			System.exit(0);
			r = true;
		}
		return r;
	}


	Runnable runApp = new Runnable() {
		@Override
		public void run() {
			goToNextActivity();
		}
	};
}