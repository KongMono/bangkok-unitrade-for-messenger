package com.codefriday.bangkokunitrade.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codefriday.bangkokunitrade.dataset.StoreEntry;


public class StoreParser {
	private ArrayList<StoreEntry> arr;
	private JSONObject jObject;
	StoreEntry item;

	public StoreParser() {
		arr = new ArrayList<StoreEntry>();
	}
	
	public ArrayList<StoreEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {

			jObject = jsonObject;

			JSONObject responseObject = jObject.getJSONObject("content");
			JSONArray itemsjsonArray = responseObject.getJSONArray("item");

			for (int i = 0; i < itemsjsonArray.length(); i++) {
				item = new StoreEntry();
				jObject = itemsjsonArray.getJSONObject(i);
				item.setId(jObject.getInt("id"));
				item.setRequest_no(jObject.getString("request_no"));
				item.setStatus(jObject.getString("status"));
				arr.add(item);
			}

		} catch (JSONException e) {
			Log.e("StoreParser", "Error ", e);
		}
		return arr;
	}


}
