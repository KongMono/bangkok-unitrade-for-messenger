package com.codefriday.bangkokunitrade.json;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codefriday.bangkokunitrade.dataset.DetailEntry;

public class DetailParser {
	private ArrayList<DetailEntry> arr;
	private JSONObject jObject;
	DetailEntry item;

	public DetailParser() {
		arr = new ArrayList<DetailEntry>();
	}
	
	public ArrayList<DetailEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {
			item = new DetailEntry();
			jObject = jsonObject;

			JSONObject responseObject = jObject.getJSONObject("content");
			JSONObject itemsObject = responseObject.getJSONObject("item");
			item.setId(itemsObject.getInt("order_id"));
			item.setNo(itemsObject.getString("no"));
			item.setSale(itemsObject.getString("sale"));
			item.setHospital(itemsObject.getString("hospital"));
			
			arr.add(item);
			
		} catch (JSONException e) {
			Log.e("DetailParser", "Error ", e);
		}
		return arr;
	}


}
