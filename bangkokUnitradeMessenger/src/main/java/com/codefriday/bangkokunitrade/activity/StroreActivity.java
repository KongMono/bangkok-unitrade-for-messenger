package com.codefriday.bangkokunitrade.activity;



import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.codefriday.bangkokunitrade.adapter.ListStoreAdapter;
import com.codefriday.bangkokunitrade.dataset.StoreEntry;
import com.codefriday.bangkokunitrade.dataset.UserDataset;
import com.codefriday.bangkokunitrade.json.StoreParser;
import com.codefriday.bangkokunitrade.util.Api;
import com.codefriday.bangkokunitrade.util.PreferencesApp;
import com.codefriday.bangkokunitrade.R;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StroreActivity extends SherlockActivity implements OnTouchListener,OnItemClickListener{
	private ListView listStore;
	private StoreParser parser = new StoreParser();
	private ArrayList<StoreEntry> storeEntries;
	private ProgressDialog dialog;
	private ListStoreAdapter adapter;
	private LinearLayout layout;
	private TextView HeadNo;
	private Button buttonSend,buttonCancel,buttonAddpic;
	private ImageView imageView;
	public File outputFileName;
	public String filePath;
	AlertDialog helpDialog;
	int targetW = 800;
	int targetH = 800;
	AQuery aq;
	Api api;
	PreferencesApp app;
	UserDataset dataset = UserDataset.getInstance();
	private static final int takePhotoCode = 1;
	File mLastTakenImageAsJPEGFile;
	Bitmap originalBitmap;
	private String strApiCall;
	int position = 0;
	int FixOneTouch = 0;
	private ArrayList<File> listFileOutputName = new ArrayList<File>();
	private ArrayList<File> listFilePicPath = new ArrayList<File>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);

		int titleId = getResources().getIdentifier("action_bar_title","id","android");
	    TextView titleBar = (TextView) findViewById(titleId);
	    titleBar.setTextSize(20f);
	    titleBar.setTextColor(getResources().getColor(R.color.white));
	     
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo_title_bar));
		
		dialog = ProgressDialog.show(this, "","Loading. Please wait...", true);
		dialog.setCancelable(false);
		dialog.cancel();
		
		init();
		
		ApiGetDataStore();

	}

	private void ApiGetDataStore() {
		strApiCall = api.getApiStore();
		aq.ajax(strApiCall, JSONObject.class, new AjaxCallback<JSONObject>() {
			ArrayList<StoreEntry> arrayList = new ArrayList<StoreEntry>();
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				 try {
					 if (json != null) {
						 arrayList = parser.getData(json);
						 storeEntries = arrayList;
						 adapter = new ListStoreAdapter(StroreActivity.this, arrayList);
						 listStore.setAdapter(adapter);
					 }
				 }catch(Exception e){
					 e.printStackTrace();
				 }finally{
					 dialog.dismiss();
				 }
			}
        });
		
	}
	
	private void init() {
		aq = new AQuery(this);
		api = new Api(this);
		app = new PreferencesApp(this);
		listStore = (ListView) findViewById(R.id.list);
		listStore.setOnItemClickListener(this);
		storeEntries = new ArrayList<StoreEntry>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	        	app.clearData();
	        	Intent intent = new Intent(this,MainActivity.class);
				startActivity(intent);
				finish();
	            return true;
	            
	        case R.id.action_refresh:
	        	dialog.show();
	        	ApiGetDataStore();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
		if (storeEntries != null) {
			position = pos;
			if (storeEntries.get(pos).getStatus().contains("0")) {
				showPopUp();
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case takePhotoCode:
			processPhoto();
			break;
		default:
			break;
		}
	}
	
	private void showPopUp() {

		final Builder helpBuilder = new Builder(this);
		final LayoutInflater inflater = getLayoutInflater();
		final View view = inflater.inflate(R.layout.layout_store_addphoto, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		TextView header = (TextView) view.findViewById(R.id.textheader);
		header.setTextSize(20f);
		HeadNo = (TextView) view.findViewById(R.id.HeadNo);

		HeadNo.setTextSize(15f);
		
		buttonAddpic = (Button) view.findViewById(R.id.btnAddpic);
		buttonAddpic.setTextSize(15f);
		buttonAddpic.setText("เพิ่มรูป");
		buttonAddpic.setTextColor(getResources().getColor(R.color.white));
		buttonAddpic.setOnTouchListener(this);

		
		buttonSend = (Button) view.findViewById(R.id.buttonSend);
		buttonSend.setTextSize(15f);
		buttonSend.setText("บันทึก");
		buttonSend.setTextColor(getResources().getColor(R.color.white));
		buttonSend.setOnTouchListener(this);
		
		buttonCancel= (Button) view.findViewById(R.id.buttonCancel);
		buttonCancel.setTextSize(15f);
		buttonCancel.setText("ยกเลิก");
		buttonCancel.setTextColor(getResources().getColor(R.color.white));
		buttonCancel.setOnTouchListener(this);

		layout = (LinearLayout) view.findViewById(R.id.addpic);
		helpBuilder.setView(view);

		
		HeadNo.setText("เลขที่คำสั่งซื้อ: "+ storeEntries.get(position).getRequest_no());
		// Call API Detail
		helpDialog = helpBuilder.create();
		helpDialog.setCancelable(false);
		helpDialog.show();
	}
	
	protected void processPhoto() {

		int imageExifOrientation = 0;
		ExifInterface exif;
		try {
			exif = new ExifInterface(outputFileName.getAbsolutePath());
			imageExifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int rotationAmount = 0;

		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			rotationAmount = 270;
		}
		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			rotationAmount = 90;
		}
		if (imageExifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			rotationAmount = 180;
		}

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(outputFileName.getAbsolutePath(), bmOptions);
		int photoWidth = bmOptions.outWidth;
		int photoHeight = bmOptions.outHeight;

		int scaleFactor = Math.min(photoWidth / targetW, photoHeight / targetH);

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap scaledDownBitmap = BitmapFactory.decodeFile(
				outputFileName.getAbsolutePath(), bmOptions);
		
		// collect File OutputName
		listFileOutputName.add(outputFileName);
		
		if (rotationAmount != 0) {
			Matrix mat = new Matrix();
			mat.postRotate(rotationAmount);
			scaledDownBitmap = Bitmap.createBitmap(scaledDownBitmap, 0, 0,
					scaledDownBitmap.getWidth(), scaledDownBitmap.getHeight(),
					mat, true);
		}

		FileOutputStream outFileStream = null;
		try {
			mLastTakenImageAsJPEGFile = createImageFile(".jpg");
			outFileStream = new FileOutputStream(mLastTakenImageAsJPEGFile);
			scaledDownBitmap.compress(Bitmap.CompressFormat.JPEG, 100,outFileStream);
			// collect File Path.
			listFilePicPath.add(mLastTakenImageAsJPEGFile);
			originalBitmap = scaledDownBitmap;

			imageView = new ImageView(this);
			imageView.setPadding(5, 5, 5, 5);
			imageView.setLayoutParams(new LayoutParams(400, 400));
			imageView.setImageBitmap(originalBitmap);
			layout.addView(imageView);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private File createImageFile(String fileExtensionToUse) throws IOException {

		File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"bangkokUnitradePicture");

		if (!storageDir.exists()) {
			if (!storageDir.mkdir()) {
			}
		}
		if (!storageDir.isDirectory()) {
		}

		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = storeEntries.get(position).getRequest_no() + "_" + timeStamp + "_image";

		File image = File.createTempFile(imageFileName, fileExtensionToUse,storageDir);

		return image;
	}
	
	public void UploadFile() {
		// Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
			
		String strUrlServer = "http://codelo.gs/p/bangkokunitrade/api/stores/verify/format/json";
		
		String resServer = uploadFiletoServer(listFilePicPath, strUrlServer);
		final AlertDialog.Builder ad = new AlertDialog.Builder(this);
		/**
		 * Get result from Server (Return the JSON Code) StatusID = ?
		 * [0=Failed,1=Complete] Error = ? [On case error return custom error
		 * message]
		 * 
		 * Eg Upload Failed = {"StatusID":"0","Error":"Cannot Upload file!"} Eg
		 * Upload Complete = {"StatusID":"1","Error":""}
		 */

		/*** Default Value ***/
		String strStatusID = "0";
		String strError = "Unknow Status!";

		try {
			JSONObject c = new JSONObject(resServer);
			strStatusID = c.getString("StatusID");
			strError = c.getString("Error");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (strStatusID.equals("0")) {
			ad.setTitle("Error!");
			ad.setIcon(android.R.drawable.btn_star_big_on);
			ad.setMessage(strError);
			ad.setPositiveButton("Close", null);
			ad.show();
		} else {
			Toast.makeText(this, "Upload file Successfully",Toast.LENGTH_SHORT).show();
			listFilePicPath.clear();
			listFileOutputName.clear();
		}
	}
	
	public String uploadFiletoServer(ArrayList<File> listFile, String strUrlServer) {
		
		String strSDPath1 = null,strSDPath2 = null,strSDPath3 = null;
		
		for (int i = 0; i < listFile.size(); i++) {
			if (i == 0) {
				strSDPath1 = listFile.get(i).getAbsolutePath();
			}else if (i == 1) {
				strSDPath2 = listFile.get(i).getAbsolutePath();
			}else{
				strSDPath3 = listFile.get(i).getAbsolutePath();
			}
		}
		
		
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		int resCode = 0;
		String resMessage = "";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		FileInputStream fileInputStream1 = null,fileInputStream2 = null,fileInputStream3 = null;
		try {
			
			/** Check file on SD Card ***/
			for (int i = 0; i < listFileOutputName.size(); i++) {
				File file = new File(listFileOutputName.get(i).getAbsolutePath());
				if (!file.exists()) {
					return "{\"StatusID\":\"0\",\"Error\":\"Please check path on SD Card\"}";
				}
			}
			if (strSDPath1 != null) {
				fileInputStream1 = new FileInputStream(new File(strSDPath1));
			}
			if (strSDPath2 != null) {
				fileInputStream2 = new FileInputStream(new File(strSDPath2));
			}
			if (strSDPath3 != null) {
				fileInputStream3 = new FileInputStream(new File(strSDPath3));
			}

			URL url = new URL(strUrlServer);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

			DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			
			outputStream.writeBytes("Content-Disposition: form-data; name=\"order_id\""+ lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(String.valueOf(storeEntries.get(position).getId())  + lineEnd);
			
			// Read file1 ******************************************
			if (strSDPath1 != null) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"fileUpload_1\";filename=\"" + strSDPath1 +"\"" + lineEnd);
				outputStream.writeBytes(lineEnd);
				bytesAvailable = fileInputStream1.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				bytesRead = fileInputStream1.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream1.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream1.read(buffer, 0, bufferSize);
				}
				
				outputStream.writeBytes(lineEnd);
			}
			
			//  ****************************************************************
			// Read file2 ******************************************
			if (strSDPath2 != null) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"fileUpload_2\";filename=\"" + strSDPath2 +"\"" + lineEnd);
				outputStream.writeBytes(lineEnd);
				bytesAvailable = fileInputStream2.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				
				bytesRead = fileInputStream2.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream2.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream2.read(buffer, 0, bufferSize);
				}
				
				outputStream.writeBytes(lineEnd);
			}
			
			//  ****************************************************************
			// Read file3 ******************************************
			
			if (strSDPath3 != null) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"fileUpload_3\";filename=\"" + strSDPath3 +"\"" + lineEnd);
				outputStream.writeBytes(lineEnd);
				bytesAvailable = fileInputStream3.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				
				bytesRead = fileInputStream3.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream3.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream3.read(buffer, 0, bufferSize);
				}
			}
			
			// Read complete ******************************************
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens+ lineEnd);

			// Response Code and Message
			resCode = conn.getResponseCode();
			if (resCode == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();

				int read = 0;
				while ((read = is.read()) != -1) {
					bos.write(read);
				}
				byte[] result = bos.toByteArray();
				bos.close();

				resMessage = new String(result);

			}

			Log.d("resCode=", Integer.toString(resCode));
			Log.d("resMessage=", resMessage.toString());

			if (fileInputStream1 != null) {
				fileInputStream1.close();
			}
			
			if (fileInputStream2 != null) {
				fileInputStream2.close();
			}
			
			if (fileInputStream3 != null) {
				fileInputStream3.close();
			}
			
			outputStream.flush();
			outputStream.close();

			return resMessage.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}finally{
			FixOneTouch--;
			helpDialog.dismiss();
			ApiGetDataStore();
		}
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.btnAddpic:
			
			if (event.getAction() == MotionEvent.ACTION_UP) {
				
				if (listFilePicPath.size() < 3) {
					try {
						outputFileName = createImageFile(".jpg");
					} catch (IOException e) {
						e.printStackTrace();
					}
					filePath = outputFileName.getPath();
					
					Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(outputFileName));
					startActivityForResult(CameraIntent, takePhotoCode);
				}else{
					Toast.makeText(this, "Limit 3 Pictures.",Toast.LENGTH_SHORT).show();
				}
			}
			
			break;
		case R.id.buttonSend:
			if (FixOneTouch == 0 && listFilePicPath.size() > 0) {
				FixOneTouch++;
				dialog.show();
				UploadFile();
			}else{
				Toast.makeText(this, "At least 1 Pictures",Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.buttonCancel:
			helpDialog.dismiss();
			listFilePicPath.clear();
			listFileOutputName.clear();
			break;
		default:
			break;
		}
		return false;
	}
	

}
