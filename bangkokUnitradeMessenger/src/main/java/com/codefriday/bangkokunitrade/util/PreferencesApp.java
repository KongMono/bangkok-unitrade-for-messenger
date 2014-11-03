package com.codefriday.bangkokunitrade.util;

import java.text.Format;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.codefriday.bangkokunitrade.dataset.UserDataset;

public class PreferencesApp {
	public static String LogintypeFB = "F";
	public String LogintypeInApp = "A";
	public String TAG = PreferencesApp.class.getSimpleName();
	private SharedPreferences appSharedPrefs;
	private Editor prefsEditor;
	Format formatter = new SimpleDateFormat("MMMM d, yyyy");
	
	public PreferencesApp(Context context) {
		
		this.appSharedPrefs = context.getSharedPreferences(context.getPackageName() + ".BKUMessenger", 0);
		this.prefsEditor = appSharedPrefs.edit();
	}
	
	public boolean firstTime() throws Exception {
		try {
			boolean firstTime = appSharedPrefs.getBoolean("firstTime", true);
			if (firstTime) {
				prefsEditor.putBoolean("firstTime", false);
				prefsEditor.commit();
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public void inputData(String user_id,String user_type,String username,String password) {
		
		prefsEditor.putString("user_id", user_id);
		prefsEditor.putString("user_type", user_type);
		prefsEditor.putString("username", username);
		prefsEditor.putString("password", password);
		prefsEditor.commit();
		
	}
	
	public void clearData() {
		
		UserDataset.getInstance().setUser_id(null);
		UserDataset.getInstance().setUser_type(null);
		UserDataset.getInstance().setUsername(null);
		UserDataset.getInstance().setPassword(null);
		
		prefsEditor.putString("user_id", "");
		prefsEditor.putString("user_type", "");
		prefsEditor.putString("username", "");
		prefsEditor.putString("password", "");
		prefsEditor.commit();
		
	}
	
	public void getData() {
		
		UserDataset.getInstance().setUser_id(appSharedPrefs.getString("user_id", ""));
		UserDataset.getInstance().setUser_type(appSharedPrefs.getString("user_type", ""));
		UserDataset.getInstance().setUsername(appSharedPrefs.getString("username", ""));
		UserDataset.getInstance().setPassword(appSharedPrefs.getString("password", ""));
		
	}
}
