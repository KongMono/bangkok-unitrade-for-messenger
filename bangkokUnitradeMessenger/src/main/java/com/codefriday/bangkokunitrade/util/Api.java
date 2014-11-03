package com.codefriday.bangkokunitrade.util;

import android.content.Context;
import android.util.Log;

public class Api {

	private Context context;
	private String TAG = getClass().getSimpleName();

	public Api(Context c) {
		context = c;
	}
	// post
	public String getApiauth() {
		StringBuilder br = new StringBuilder();
		br.append("http://apps.bucmedical.com/api/auth/verify/format/json");
		Log.d("getApiauth", br.toString());
		return br.toString();
	}
	
	public String getApiOrderDelivery(String id) {
		StringBuilder br = new StringBuilder();
		br.append("http://apps.bucmedical.com/api/order/delivery/user_id/");
		br.append(id);
		br.append("/format/json");
		Log.d("getApiOrderDelivery", br.toString());
		return br.toString();
	}
	
	public String getApiDetail(String barcode) {
		StringBuilder br = new StringBuilder();
		br.append("http://apps.bucmedical.com/api/order/detail/barcode/");
		br.append(barcode);
		br.append("/format/json");
		Log.d("getApiDetail", br.toString());
		return br.toString();
	}
	
	public String getApiConfirm() {
		StringBuilder br = new StringBuilder();
		br.append("http://apps.bucmedical.com/api/order/confirm/format/json");
		Log.d("getApiConfirm", br.toString());
		return br.toString();
	}
	
	public String getApiAdmit(String barcode) {
		StringBuilder br = new StringBuilder();
		br.append("http://apps.bucmedical.com/api/order/admit/barcode/");
		br.append(barcode);
		br.append("/format/json");
		Log.d("getApiOrderDelivery", br.toString());
		return br.toString();
	}
	
	public String getApiStore() {
		StringBuilder br = new StringBuilder();
		br.append("http://apps.bucmedical.com/api/stores/orders/format/json");
		Log.d("getApiStore", br.toString());
		return br.toString();
	}
	
	public String getApiTransportsType() {
		StringBuilder br = new StringBuilder();
		br.append("http://apps.bucmedical.com/api/transports/list/format/json");
		Log.d("getApiTransportsType", br.toString());
		return br.toString();
	}
	
	public String getApiMessengerAdmit(String barcode) {
		StringBuilder br = new StringBuilder();
		br.append("http://codelo.gs/p/bangkokunitrade/api/order/delivery/barcode/");
		br.append(barcode);
		br.append("/format/json");
		Log.d("getApiMessengerAdmit", br.toString());
		return br.toString();
	}
	
	

	
	
	
	
	
	
}
