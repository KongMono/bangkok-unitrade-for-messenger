package com.codefriday.bangkokunitrade.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codefriday.bangkokunitrade.dataset.StockEntry;


public class StockParser {
	private ArrayList<StockEntry> arr;
	private JSONObject jObject;
	StockEntry item;

	public StockParser() {
		arr = new ArrayList<StockEntry>();
	}
	
	public ArrayList<StockEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {

			jObject = jsonObject;

			JSONObject responseObject = jObject.getJSONObject("content");
			JSONArray itemsjsonArray = responseObject.getJSONArray("item");

			for (int i = 0; i < itemsjsonArray.length(); i++) {
				item = new StockEntry();
				jObject = itemsjsonArray.getJSONObject(i);
				item.setId(jObject.getInt("order_id"));
				item.setNo(jObject.getString("no"));
				item.setBarcode(jObject.getString("barcode"));
				item.setHospital(jObject.getString("hospital"));
				item.setStatus(jObject.getString("status"));
				arr.add(item);
			}

		} catch (JSONException e) {
			Log.e("StockParser", "Error ", e);
		}
		return arr;
	}


}
