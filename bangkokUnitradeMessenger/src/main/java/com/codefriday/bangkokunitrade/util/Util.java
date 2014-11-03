package com.codefriday.bangkokunitrade.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Util extends Activity{
	
	

	public static boolean isOnline(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	public static String StringToMD5(String string){
		String toEnc = string;
		MessageDigest mdEnc = null;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		mdEnc.update(toEnc.getBytes(), 0, toEnc.length());
		String md5 = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted 
		return md5;
	}
	
	public static int averageARGB(Bitmap pic) {
		int A, R, G, B;
		A = R = G = B = 0;
		int pixelColor;
		int width = pic.getWidth();
		int height = pic.getHeight();
		int size = width * height;

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				pixelColor = pic.getPixel(x, y);
				A += Color.alpha(pixelColor);
				R += Color.red(pixelColor);
				G += Color.green(pixelColor);
				B += Color.blue(pixelColor);
			}
		}

		A /= size;
		R /= size;
		G /= size;
		B /= size;

		int color = Color.argb(A, R, G, B);

		return color;

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
	
	public static Typeface getFont(Context context) {
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/THSarabunNew_Bold.ttf");
		return face;
	}

	public static Typeface getBoldFont(Context context) {
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/THSarabunNew.ttf");
		return face;
	}
	
	public static boolean stringIsNotNull(final String string){  
	   if (string != null && !string.isEmpty() && !string.trim().isEmpty()) {
		   return true;
	   }
	   return false;
	}  
	
	public static String isNotNullNotEmptyNotWhiteSpace(final String string){  
	   if (string != null && !string.isEmpty() && !string.trim().isEmpty()) {
		   return string;
	   }
	   return "";
	}  
	public static String isNotNullNotEmptyNotWhiteSpaceLocate(final String string){  
	   if (string != null && !string.isEmpty() && !string.trim().isEmpty()) {
		   return string;
	   }
	   return "0";
	}  
	        
    public static Calendar getCalendar(){
        return Calendar.getInstance(Locale.ENGLISH);
    }
		
	public static Date getCurrentDate(){
		return getCalendar().getTime();
	}
	
	public static String datetoStringFormat(Date date, String formatOutput){
		SimpleDateFormat sdf = new SimpleDateFormat(formatOutput);
		return sdf.format(date);		
	}
	
	public static Date toDate(String date, String format) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);	
	}
	
	public static String getCurrentDate(String format){
		return datetoStringFormat(getCalendar().getTime(),format);
	}
	
	public static Date addDay(Date current, int days){
		Calendar cal = getCalendar();
		cal.setTime(current);
		cal.add(Calendar.DATE, + days);
		
		return cal.getTime();
	}
	
	public static Date subDay(Date current, int days){
		Calendar cal = getCalendar();
		cal.setTime(current);
		cal.add(Calendar.DATE, - days);
		
		return cal.getTime();
	}
	
	public static String addDay(Date current, int days, String outFormat){
		Calendar cal = getCalendar();
		cal.setTime(current);
		cal.add(Calendar.DATE, + days);
		
		return datetoStringFormat(cal.getTime(),outFormat);
	}
	
	public static String subDay(Date current, int days, String outFormat){
		Calendar cal = getCalendar();
		cal.setTime(current);
		cal.add(Calendar.DATE, - days);
		
		return datetoStringFormat(cal.getTime(),outFormat);
	}
}
