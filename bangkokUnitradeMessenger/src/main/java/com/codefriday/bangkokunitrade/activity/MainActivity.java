package com.codefriday.bangkokunitrade.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.codefriday.bangkokunitrade.dataset.UserDataset;
import com.codefriday.bangkokunitrade.util.Api;
import com.codefriday.bangkokunitrade.util.PreferencesApp;
import com.codefriday.bangkokunitrade.util.Util;
import com.codefriday.bangkokunitrade.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener{

	private EditText edittxtUserName, edittxtPassword;
	private Button btnlogin;
	private ProgressDialog dialog;
	AQuery aq;
	Api api;
	PreferencesApp app;
	UserDataset dataset = UserDataset.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		dialog = ProgressDialog.show(this, "","Loading. Please wait...", true);
		dialog.setCancelable(false);
		dialog.cancel();
		
		init();

		btnlogin.setOnTouchListener(this);
	}

	private void init() {
		aq = new AQuery(this);
		api = new Api(this);
		app = new PreferencesApp(this);
		edittxtUserName = (EditText) findViewById(R.id.user);
		edittxtPassword = (EditText) findViewById(R.id.password);
		btnlogin = (Button) findViewById(R.id.sign_in_button);

		edittxtUserName.setHint("Username");
		edittxtPassword.setHint("Password");
		
		edittxtPassword.setTypeface(Typeface.DEFAULT);
		edittxtPassword.setTransformationMethod(new PasswordTransformationMethod());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		dialog.show();
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			String md5 = Util.StringToMD5(edittxtPassword.getText().toString());
			String url = api.getApiauth();
	
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("username",edittxtUserName.getText().toString());
			Log.d("pass", md5);
			params.put("password",md5);

			aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
				Intent intent = new Intent();
			    @Override
			    public void callback(String url, JSONObject json, AjaxStatus status) {
			    	
			    	if (json != null) {
			    		JSONObject responseObject = json;
						 try {
							 app.inputData(responseObject.getString("id"),
									 responseObject.getString("user_type"),
									 edittxtUserName.getText().toString(),
									 edittxtPassword.getText().toString());
							 
							 app.getData();
							 
						} catch (JSONException e) {
							e.printStackTrace();
						}
						 
					 	switch (Integer.parseInt(dataset.getUser_type())) {
					 	
							case UserDataset.Type_System:
								Toast.makeText(getApplicationContext(), "No Content", Toast.LENGTH_LONG).show();
								break;
								
							case UserDataset.Type_Administrator:
								Toast.makeText(getApplicationContext(), "No Content", Toast.LENGTH_LONG).show();
								break;
								
							case UserDataset.Type_Sale:
								Toast.makeText(getApplicationContext(), "No Content", Toast.LENGTH_LONG).show();
								break;
								
							case UserDataset.Type_Product:
								Toast.makeText(getApplicationContext(), "No Content", Toast.LENGTH_LONG).show();
								break;
								
							case UserDataset.Type_Messenger:
								intent = new Intent(getApplicationContext(),ListStockActivity.class);
								startActivity(intent);
								finish();
								break;
								
							case UserDataset.Type_Store:
								intent = new Intent(getApplicationContext(),StroreActivity.class);
								startActivity(intent);
								finish();
								break;
	
							default:
								Toast.makeText(getApplicationContext(), "No Content", Toast.LENGTH_LONG).show();
								break;
						}
					 	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
						 
					}else{
						Toast.makeText(getApplicationContext(), status.getMessage(), Toast.LENGTH_LONG).show();
					}
			    	dialog.cancel();
			    }
		    });
			
			return true;
		}
		return false;
	}
	

}
