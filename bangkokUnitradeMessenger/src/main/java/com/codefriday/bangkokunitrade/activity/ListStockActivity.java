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
import java.util.Calendar;
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
import com.codefriday.bangkokunitrade.adapter.ListCheckStockAdapter;
import com.codefriday.bangkokunitrade.adapter.ListDetailAdapter;
import com.codefriday.bangkokunitrade.adapter.ListStockAdapter;
import com.codefriday.bangkokunitrade.adapter.TransportsAdapter;
import com.codefriday.bangkokunitrade.dataset.AddStockEntry;
import com.codefriday.bangkokunitrade.dataset.DetailEntry;
import com.codefriday.bangkokunitrade.dataset.StockEntry;
import com.codefriday.bangkokunitrade.dataset.TransportsEntry;
import com.codefriday.bangkokunitrade.dataset.UserDataset;
import com.codefriday.bangkokunitrade.json.AddStockParser;
import com.codefriday.bangkokunitrade.json.DetailParser;
import com.codefriday.bangkokunitrade.json.StockParser;
import com.codefriday.bangkokunitrade.json.TransportsParser;
import com.codefriday.bangkokunitrade.util.Api;
import com.codefriday.bangkokunitrade.util.ExpandableHeightListView;
import com.codefriday.bangkokunitrade.util.PreferencesApp;
import com.codefriday.bangkokunitrade.util.Util;
import com.codefriday.bangkokunitrade.util.gpsTracker;
import com.codefriday.bangkokunitrade.R;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ListStockActivity extends SherlockActivity implements OnTouchListener,OnItemClickListener {
	boolean isOnline = false;
	private gpsTracker gps;
	private ListView list;
	private Button btnAddStock, btntakePhoto;
	private LinearLayout layout;
	private EditText EditTextdate,EditTextcar;
	private Spinner spinner;
	private TextView  HeadNo, HeadSale, HeadHospotal,HeadSendBy,HeadTimeSend,HeadCarNum;
	private ImageView imageView;
	private ListStockAdapter stockAdapter;
	private ListDetailAdapter listDetailAdapter;
	private TransportsAdapter transportsAdapter;
	public File outputFileName;
	public String filePath;
	private ArrayList<AddStockEntry> addStockEntries  = new ArrayList<AddStockEntry>();
	private ArrayList<TransportsEntry> transportsEntries = new ArrayList<TransportsEntry>();
	private ArrayList<StockEntry> stockEntries = new ArrayList<StockEntry>();
	private ArrayList<DetailEntry> detailEntries = new ArrayList<DetailEntry>();
	private static final int takePhotoCode = 1;
	private static final int cropPhotoCode = 2;
	private static final int sendPhotoToServer = 3;
	public static final int captureBarcode = 4;
	public final int STARTTIME_DIALOG_ID = 1;
	public String transports_id = null;
	AlertDialog helpDialog;
	int targetW = 800;
	int targetH = 800;
	Uri fileUri;
	private String strApiCall;
	Api api;
	AQuery aq;
	ExpandableHeightListView listDetail;
	TransportsParser transportsParser;
	StockParser stockParser;
	DetailParser detailParser;
	UserDataset dataset = UserDataset.getInstance();
	ProgressDialog dialog;
	Bitmap originalBitmap;
	File mLastTakenImageAsJPEGFile;
	PreferencesApp app;
	int FixOneTouch = 0;
	int position = 0;
	private int hour;
	private int minute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock);

		dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
		dialog.setCancelable(true);
		dialog.show();
		
		/********* display current time on screen Start ********/

		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		/********* display current time on screen End ********/
			 	 
		int titleId = getResources().getIdentifier("action_bar_title","id","android");
	    TextView titleBar = (TextView) findViewById(titleId);
	    titleBar.setTextSize(20f);
	    titleBar.setTextColor(getResources().getColor(R.color.white));
	     
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo_title_bar));
		
		init();
		
		ApiGetListOrderDelivery();

		try {
			if (gps.canGetLocation()) {
				Log.d("latitude", String.valueOf(gps.getLatitude()));
				Log.d("longitude", String.valueOf(gps.getLongitude()));
			} else {
				gps.showSettingsAlert();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		app = new PreferencesApp(this);
		api = new Api(this);
		aq = new AQuery(this);
		gps = new gpsTracker(this);
		stockParser = new StockParser();
		detailParser = new DetailParser();
		transportsParser = new TransportsParser();

		btnAddStock = (Button) findViewById(R.id.buttonAdd);
		btnAddStock.setText("เพิ่มสินค้า");
		btnAddStock.setTextColor(getResources().getColor(R.color.white));
		btnAddStock.setTextSize(20f);

		list = (ListView) findViewById(R.id.list);

		isOnline = Util.isOnline(this);
		list.setOnItemClickListener(this);
		btnAddStock.setOnTouchListener(this);

	}

	private void ApiGetListOrderDelivery() {

		strApiCall = api.getApiOrderDelivery(dataset.getUser_id());
		aq.ajax(strApiCall, JSONObject.class, new AjaxCallback<JSONObject>() {
			ArrayList<StockEntry> arrayList = new ArrayList<StockEntry>();

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				stockEntries.clear();
				try {
					if (json != null) {
						arrayList = stockParser.getData(json);
						stockEntries = arrayList;
						stockAdapter = new ListStockAdapter(ListStockActivity.this, arrayList);
						list.setAdapter(stockAdapter);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
                    dialog.dismiss();
                }
            }
		});
	}
	
	private void ApiGetApiAdmit(String barcode) {

		strApiCall = api.getApiAdmit(barcode);
		aq.ajax(strApiCall, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				try {
					if (json != null) {
						AddStockParser addStockParser = new AddStockParser();
                        if(addStockParser.getData(json).size() > 0){
                            showPopUpAddCheckStock(addStockParser.getData(json));
                        }else{
                            Toast.makeText(ListStockActivity.this, "สินค้าส่งไม่ตรงกับรายการที่เลือก", Toast.LENGTH_SHORT).show();
                        }

					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
                    dialog.dismiss();
                }
			}
		});
	}

	private void ApiDetail(String barcode) {

		strApiCall = api.getApiDetail(barcode);
		aq.ajax(strApiCall, JSONObject.class, new AjaxCallback<JSONObject>() {
			ArrayList<DetailEntry> arrayList = new ArrayList<DetailEntry>();
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				try {
					if (json != null) {
						detailEntries.clear();
						arrayList = detailParser.getData(json);
						detailEntries = arrayList;

						HeadNo.setText("เลขที่คำสั่งซื้อ: "
								+ arrayList.get(0).getNo());
						HeadSale.setText("ผู้แทน : "
								+ arrayList.get(0).getSale());
						HeadHospotal.setText("โรงพยาบาล: "
								+ arrayList.get(0).getHospital());
						
						ApiGetSpinnerData();
						
					}

				} catch (Exception e) {
					e.printStackTrace();
				}finally {
                    dialog.dismiss();
                }
			}
		});
	}
	
	private void ApiGetSpinnerData() {
		strApiCall = api.getApiTransportsType();
		aq.ajax(strApiCall, JSONObject.class, new AjaxCallback<JSONObject>() {
			ArrayList<TransportsEntry> arrayList = new ArrayList<TransportsEntry>();
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				 try {
					 if (json != null) {
						 arrayList = transportsParser.getData(json);
						 transportsEntries = arrayList;
						 transportsAdapter = new TransportsAdapter(ListStockActivity.this, arrayList);
						 spinner.setAdapter(transportsAdapter);
					 }
				 }catch(Exception e){
					 e.printStackTrace();
				 }finally{
					 dialog.dismiss();
				 }
			}
        });
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
	        	ApiGetListOrderDelivery();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		gps.stopUsingGPS();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
		if (stockEntries != null) {
			position = pos;
			if (stockEntries.get(pos).getStatus().contains("1")) {
				Intent inent = new Intent(this, CaptureActivity.class);
				startActivityForResult(inent, captureBarcode);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                ApiGetApiAdmit("53C8CBE");
            }

            switch (requestCode) {

                case takePhotoCode:
                    processPhoto();
                    break;
                // recive (ขาส่ง)
                case captureBarcode:
                    if (data != null) {
                        String barcode = data.getStringExtra("barcode");

                        // fix
                        if (!stockEntries.get(position).getBarcode().equalsIgnoreCase(barcode)) {
                            showPopUp(stockEntries.get(position).getBarcode());
                        }else{
                            Toast.makeText(ListStockActivity.this, "สินค้าส่งไม่ตรงกับรายการที่เลือก", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                // add addstock (เพิ่มสินค้า)
                case cropPhotoCode:
                    imageView = new ImageView(this);
                    imageView.setPadding(5, 5, 5, 5);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                    layout.addView(imageView);
                    break;
                default:
                    break;
            }

        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }


	}


    private void showPopUp(String barcode) {

		final Builder helpBuilder = new Builder(this);
		final LayoutInflater inflater = getLayoutInflater();
		final View view = inflater.inflate(R.layout.layout_add_photo, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

		TextView header = (TextView) view.findViewById(R.id.textheader);
		TextView textheaderData = (TextView) view.findViewById(R.id.textheaderData);
		
		HeadNo = (TextView) view.findViewById(R.id.HeadNo);
		HeadSale = (TextView) view.findViewById(R.id.HeadSale);
		HeadHospotal = (TextView) view.findViewById(R.id.HeadHospotal);
		HeadSendBy = (TextView) view.findViewById(R.id.HeadSendBy);
		HeadTimeSend =(TextView) view.findViewById(R.id.HeadTimeSend);
		HeadCarNum = (TextView) view.findViewById(R.id.HeadCarNum);
		spinner = (Spinner) view.findViewById(R.id.spinnerSendBy);
		EditTextdate = (EditText) view.findViewById(R.id.EditTextTime);
		EditTextdate.setOnTouchListener(this);
		EditTextcar = (EditText) view.findViewById(R.id.EditTextcar);
		updateTime(hour, minute);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				transports_id = String.valueOf(transportsEntries.get(position).getId());
		    }
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});

		header.setTextSize(20f);
		textheaderData.setTextSize(20f);
		
		HeadNo.setTextSize(15f);
		HeadSale.setTextSize(15f);
		HeadHospotal.setTextSize(15f);
		HeadSendBy.setTextSize(15f);
		HeadTimeSend.setTextSize(15f);
		HeadCarNum.setTextSize(15f);
		
		btntakePhoto = (Button) view.findViewById(R.id.buttonPhoto);
		btntakePhoto.setTextSize(15f);
		btntakePhoto.setText("เพิ่มรูป");
		btntakePhoto.setTextColor(getResources().getColor(R.color.white));
		btntakePhoto.setOnTouchListener(this);

		layout = (LinearLayout) view.findViewById(R.id.addpic);
		helpBuilder.setView(view);

		// Call API Detail
		ApiDetail(barcode);
		
		helpDialog = helpBuilder.create();
		helpDialog.show();
	}
	
	
	private void showPopUpAddCheckStock(ArrayList<AddStockEntry> addStockEntries) {

		final Builder helpBuilder = new Builder(this);
		final LayoutInflater inflater = getLayoutInflater();
		final View view = inflater.inflate(R.layout.layout_add_checkstock, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

		TextView header = (TextView) view.findViewById(R.id.textheader);
		final ListView listView = (ListView) view.findViewById(R.id.listcheckstock);
		
		Button buttoncomfirmCheck  = (Button) view.findViewById(R.id.buttoncomfirmCheck);
		buttoncomfirmCheck.setTextSize(15f);
		buttoncomfirmCheck.setText("ยืนยันการรับ");
		buttoncomfirmCheck.setTextColor(getResources().getColor(R.color.white));
		buttoncomfirmCheck.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					Boolean ischeckAll = true;
					for (int i = 0; i < listView.getAdapter().getCount(); i++) {
						AddStockEntry item = (AddStockEntry)listView.getAdapter().getItem(i);
					    if (!item.getChecked()) {
					    	ischeckAll = false;
					    }
					}
					if (ischeckAll) {
						Toast.makeText(ListStockActivity.this, "เช็ครายการเรียบร้อย ", Toast.LENGTH_SHORT).show();
						ApiGetListOrderDelivery();
						helpDialog.dismiss();
					}else{
						Toast.makeText(ListStockActivity.this, "กรุณาเช็ครายการให้ครบ ", Toast.LENGTH_SHORT).show();
						return false;
					}
				}
				return false;
			}
		});
		
		
		ListCheckStockAdapter adapter = new ListCheckStockAdapter(ListStockActivity.this, addStockEntries);
		listView.setAdapter(adapter);
		

		header.setTextSize(20f);
		
		helpBuilder.setView(view);
		helpDialog = helpBuilder.create();
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
			scaledDownBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					outFileStream);
			
			originalBitmap = scaledDownBitmap;

			// cropImage(outputFileName);

			imageView = new ImageView(this);
			imageView.setPadding(5, 5, 5, 5);
//			imageView.setLayoutParams(new LayoutParams(400, 400));
			imageView.setImageBitmap(originalBitmap);
			layout.addView(imageView);

			btntakePhoto.setText("บันทึก");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cropImage(File file) {
		Uri uriFromPath = Uri.fromFile(new File(file.getAbsolutePath()));
		Intent intent = new Intent("com.android.camera.action.CROP")
				.setDataAndType(uriFromPath, "image/*")
				.putExtra("crop", "true")
				.putExtra("aspectX", targetW)
				.putExtra("aspectY", targetH)
				.putExtra("outputX", targetW)
				.putExtra("outputY", targetH)
				.putExtra("scale", true)
				.putExtra("scaleUpIfNeeded", true)
				.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
				.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, cropPhotoCode);
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
		String imageFileName = detailEntries.get(0).getNo() + "_" + timeStamp + "_image";

		File image = File.createTempFile(imageFileName, fileExtensionToUse,storageDir);

		return image;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.buttonAdd:
			if (event.getAction() == MotionEvent.ACTION_UP) {
                new IntentIntegrator(this).initiateScan();
            }
			break;
		case R.id.buttonPhoto:

			if (event.getAction() == MotionEvent.ACTION_UP) {
				try {
					outputFileName = createImageFile(".jpg");
				} catch (IOException e) {
					e.printStackTrace();
				}
				filePath = outputFileName.getPath();

				Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(outputFileName));
				startActivityForResult(CameraIntent, takePhotoCode);
				
			}
			break;
		case sendPhotoToServer:
			if (FixOneTouch == 0) {
				FixOneTouch++;
				dialog.show();
				UploadFile();
			}
			break;
		case R.id.EditTextTime:
			showDialog(STARTTIME_DIALOG_ID);
			break;
			
		default:
			break;
		}
		return false;
	}

	public void UploadFile() {
		// Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
		String strSDPath = mLastTakenImageAsJPEGFile.getAbsolutePath();
		String strUrlServer = "http://apps.bucmedical.com/api/order/confirm/format/json";
		String resServer = uploadFiletoServer(strSDPath, strUrlServer);

		
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
		}

	}

	public String uploadFiletoServer(String strSDPath, String strUrlServer) {

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		int resCode = 0;
		String resMessage = "";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		try {
			
			/** Check file on SD Card ***/
			File file = new File(outputFileName.getAbsolutePath());
			if (!file.exists()) {
				return "{\"StatusID\":\"0\",\"Error\":\"Please check path on SD Card\"}";
			}

			FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));

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
			outputStream.writeBytes(String.valueOf(detailEntries.get(0).getId()) + lineEnd);
			
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"transport_type_id\""+ lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(transports_id + lineEnd);
			
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"plate_no\""+ lineEnd);
			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(EditTextcar.getText().toString()  + lineEnd);
			
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"fileUpload\";filename=\"" + strSDPath +"\"" + lineEnd);
             
//			outputStream.writeBytes("Content-Disposition: form-data;"
//					+ " order_id=\""+ String.valueOf(detailEntries.get(0).getId()) +"\";"
//					+ " transport_type_id=\""+ transports_id +"\";"
//					+ " plate_no=\"" + EditTextcar.getText().toString() +"\";"
//					+ "name=\"fileUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
//
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
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

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();

			return resMessage.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}finally{
			FixOneTouch--;
			helpDialog.dismiss();
			ApiGetListOrderDelivery();
		}
	}
	
	private TimePickerDialog.OnTimeSetListener StarttimePickerListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
			hour = hourOfDay;
			minute = minutes;

			String timeSet = "";
			if (hour > 12) {
				hour -= 12;
				timeSet = "PM";
			} else if (hour == 0) {
				hour += 12;
				timeSet = "AM";
			} else if (hour == 12)
				timeSet = "PM";
			else
				timeSet = "AM";

			String min = "";
			if (minute < 10)
				min = "0" + minute;
			else
				min = String.valueOf(minute);

			String aTime = new StringBuilder().append(hour).append(':')
					.append(min).append(" ").append(timeSet).toString();

			EditTextdate.setText(aTime);

		}

	};
	
	// Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {
         
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
         
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
 
        // Append in a StringBuilder
         String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
 
         EditTextdate.setText(aTime);
    }
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case STARTTIME_DIALOG_ID:
			return new TimePickerDialog(this, StarttimePickerListener, hour, minute,false);
		}
		return null;
	}
}
