package com.codefriday.bangkokunitrade.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.codefriday.bangkokunitrade.dataset.AddStockEntry;

public class AddStockParser {
	private ArrayList<AddStockEntry> arr;
	private JSONObject jObject;
	AddStockEntry item;
	String status,message,order_id;

	public AddStockParser() {
		arr = new ArrayList<AddStockEntry>();
	}
	
	public ArrayList<AddStockEntry> getData(JSONObject jsonObject) {
		arr.clear();

		try {
			
			jObject = jsonObject;

			
			status = jObject.getString("status");

            if (!status.equalsIgnoreCase("ERROR")){
                message = jObject.getString("message");
                order_id = jObject.getString("order_id");

                JSONArray itemsArray = jObject.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    jObject = itemsArray.getJSONObject(i);

                    item = new AddStockEntry();
                    item.setStatus(status);
                    item.setMessage(message);
                    item.setOrder_id(order_id);
                    item.setNo(jObject.getString("no"));
                    item.setType(jObject.getString("type"));
                    item.setChecked(false);

                    arr.add(item);
                }
            }

		} catch (JSONException e) {
			Log.e("AddStockParser", "Error ", e);
		}
		return arr;
	}


}
