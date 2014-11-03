package com.codefriday.bangkokunitrade.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codefriday.bangkokunitrade.dataset.TransportsEntry;


public class TransportsParser {
	private ArrayList<TransportsEntry> arr;
	private JSONObject jObject;
	TransportsEntry item;

	public TransportsParser() {
		arr = new ArrayList<TransportsEntry>();
	}
	
	public ArrayList<TransportsEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {

			jObject = jsonObject;

			JSONObject responseObject = jObject.getJSONObject("content");
			JSONArray itemsjsonArray = responseObject.getJSONArray("item");

			for (int i = 0; i < itemsjsonArray.length(); i++) {
				item = new TransportsEntry();
				jObject = itemsjsonArray.getJSONObject(i);
				item.setId(jObject.getInt("id"));
				item.setTransport_type(jObject.getString("transport_type"));
				arr.add(item);
			}

		} catch (JSONException e) {
			Log.e("StoreParser", "Error ", e);
		}
		return arr;
	}


}
