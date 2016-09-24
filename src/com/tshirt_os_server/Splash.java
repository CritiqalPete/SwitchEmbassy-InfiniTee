package com.tshirt_os_server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.flurry.android.FlurryAgent;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {
	Button have, dontHave, upparHave;
	RelativeLayout relLay;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mmDevice;
	byte[] data = null;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
	Thread workerThread;
	boolean failure = false, afterSuccess = false, lastHandle = false, upperClick = false, connectError = false;
	Handler handler;
	ListView listViewPaired;
	ArrayAdapter<String> adapter;
	ArrayList<String> arrayListpaired;
	ArrayList<BluetoothDevice> arrayListPairedBluetoothDevices;
	ListItemClickedonPaired listItemClickedonPaired;
	Dialog dialog;
	RelativeLayout dontRel;
	ImageView divider1, divider2, divider3, divider4, tagline, supported;
	Animation animFadein;
	Button url;
	TextView contTxt, head, words, versionTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		relLay = (RelativeLayout)findViewById(R.id.rv);
		new ASSL(this, relLay,1134,720, false);
		have = (Button)findViewById(R.id.button1);
		dontHave = (Button)findViewById(R.id.button2);
		dontRel = (RelativeLayout)findViewById(R.id.relativeLayout1);
		divider1 = (ImageView)findViewById(R.id.divider_1);
		divider2 = (ImageView)findViewById(R.id.divider_2);
		divider3 = (ImageView)findViewById(R.id.divider_3);
		contTxt = (TextView)findViewById(R.id.cont_txt);
		head = (TextView)findViewById(R.id.head);
		words = (TextView)findViewById(R.id.words);
		url = (Button)findViewById(R.id.url_btn);
		upparHave = (Button)findViewById(R.id.button3);
		divider4 = (ImageView)findViewById(R.id.divider_4);
		tagline = (ImageView)findViewById(R.id.tagline);
		supported = (ImageView)findViewById(R.id.supported);
		versionTxt = (TextView)findViewById(R.id.version_txt);

		arrayListpaired = new ArrayList<String>();
		arrayListPairedBluetoothDevices = new ArrayList<BluetoothDevice>();
		adapter= new ArrayAdapter<String>(Splash.this, R.layout.simple_list_item_1, arrayListpaired);

		have.setText("I HAVE 訊EE");
		dontHave.setText("I DON'T HAVE 訊EE");
		have.setTypeface(DataPref.dinMedium(getApplicationContext()));
		dontHave.setTypeface(DataPref.dinMedium(getApplicationContext()));
		head.setText("Switch Embassy's 訊ee is a programmable digital t-shirt that allows anybody to wear who they truly are and Stay True.");
		words.setText("The first run of 25 shirts are now in the hands of individuals around the world. To see what they are doing with it, visit us online by clicking the button below.");
		head.setTypeface(DataPref.dinMedium(getApplicationContext()));
		words.setTypeface(DataPref.dinLite(getApplicationContext()));
		versionTxt.setTypeface(DataPref.dinMedium(getApplicationContext()));
		contTxt.setText("I DON'T HAVE 訊EE");
		upparHave.setText("I HAVE 訊EE");
		upparHave.setTypeface(DataPref.dinMedium(getApplicationContext()));
		url.setTypeface(DataPref.dinMedium(getApplicationContext()));

		handler = new Handler();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		//listViewPaired.setAdapter(adapter);
		listItemClickedonPaired = new ListItemClickedonPaired();
		//listViewPaired.setOnItemClickListener(listItemClickedonPaired);

		// load the animation
		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.fade_in);  
		
		
	
	//	int dif=(int) ((1134*ASSL.Yscale())-((286*Math.min(ASSL.Xscale(), ASSL.Yscale())+185*ASSL.Xscale())));
//Log.v("diff = ", dif+"");
		supported.setMinimumHeight((int) ((1134*ASSL.Yscale())-((286*Math.min(ASSL.Xscale(), ASSL.Yscale())+185*ASSL.Xscale()))));
		
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String tempVer = pInfo.versionName;
			Log.v("version = ", ""+tempVer);
			versionTxt.setText("VERSION "+tempVer);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		url.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String url = "http://switchembassy.com/";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});

		have.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				FlurryAgent.logEvent("have button clicked.");
				if (mBluetoothAdapter == null) {
					//have_bt = true;
					Log.i("you don't have bluetooth device",".");
					Toast.makeText(getApplicationContext(),"No bluetooth adapter available", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(DataPref.mmSocket != null)
					{
						startActivity(new Intent(Splash.this, ImageActivity.class));
						finish();
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					}
					else
					{
						FlurryAgent.logEvent("have button, fetching paried list.");
						DataPref.showLoadingDialog(Splash.this, "Loading...");
						have.setVisibility(4);
						dontHave.setVisibility(4);
						divider1.setVisibility(4);
						divider2.setVisibility(4);
						divider3.setVisibility(4);

						if(!mBluetoothAdapter.isEnabled()){
							mBluetoothAdapter.enable();
						}
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getPairedDevices();
							}
						}, 1000);

					}
				}

			}
		});

		upparHave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				FlurryAgent.logEvent("uppar_have button clicked.");
				if (mBluetoothAdapter == null) {
					//have_bt = true;
					Log.i("you don't have bluetooth device",".");
					Toast.makeText(getApplicationContext(),"No bluetooth adapter available", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(DataPref.mmSocket != null)
					{
						startActivity(new Intent(Splash.this, ImageActivity.class));
						finish();
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					}
					else
					{
						FlurryAgent.logEvent("uppar_have button, fetching paried list.");
						upperClick = true;
						DataPref.showLoadingDialog(Splash.this, "Loading...");
						divider4.setVisibility(4);
						upparHave.setVisibility(4);
						divider1.setVisibility(4);
						dontRel.setVisibility(4);

						if(!mBluetoothAdapter.isEnabled()){
							mBluetoothAdapter.enable();
						}
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getPairedDevices();
							}
						}, 1000);

					}
				}

			}
		});

		final Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);		

		dontHave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				FlurryAgent.logEvent("dont_have button clicked.");
				
				supported.setMinimumHeight((int) (875*ASSL.Yscale()));

				dontRel.setVisibility(0);
				divider2.setVisibility(4);
				divider3.setVisibility(4);
				dontHave.setVisibility(4);

				dontRel.setAnimation(fadeInAnimation);

				TranslateAnimation anim=new TranslateAnimation(0,0,0,-130*ASSL.Yscale());
				anim.setDuration(1000);
				anim.setFillAfter(true);
				have.startAnimation(anim);
				divider1.startAnimation(anim);

				TranslateAnimation anim1=new TranslateAnimation(0,0,0,170*ASSL.Yscale());
				anim1.setDuration(1000);
				anim1.setFillAfter(true);
				//tagline.startAnimation(b);

				anim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						//dont_rel.setVisibility(0);
						divider1.clearAnimation();
						have.clearAnimation();
						divider4.setVisibility(0);
						upparHave.setVisibility(0);
						have.setVisibility(4);
						divider1.setVisibility(4);
					}
				});

				fadeInAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						dontRel.clearAnimation();
					}
				});
				// set animation listener
				//startActivity(new Intent(Splash.this, DontHave.class));
			}
		});


	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(this, "69JVCKH5XT2XXQPJH5M5");

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	private void getPairedDevices() {
		Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();     
		Log.v("length of paired devices",", "+pairedDevice.size());
		if(pairedDevice.size()>0)
		{
			arrayListpaired.clear();
			for(BluetoothDevice device : pairedDevice)
			{
				if(device.getName().startsWith("TShirt") || device.getName().startsWith("your_shirt"))
				{
					arrayListpaired.add(device.getName());
					Log.v("paired devices",", "+device.getName()+"\n"+device.getAddress());
					arrayListPairedBluetoothDevices.add(device);
				}
			}
		}
		adapter.notifyDataSetChanged();

		successPopup();
	}

	class ListItemClickedonPaired implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long idVal) {
			try {

				mmDevice = arrayListPairedBluetoothDevices.get(position);
				Log.i("selected device..",", "+mmDevice);
				new AsyncD().execute("");
				//openBT();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// finding bluetooth device...
	void findBT() {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().equals("TShirtOS01")) {
					Log.v("Congo.", "Your device found."+device);
					mmDevice = device;
					try {
						openBT();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("error on open bluetooth", "" + e.toString());
					}
					break;
				} else
				{
					Toast.makeText(getApplicationContext(),
							"your_shirt not paired yet, please try again.",
							Toast.LENGTH_LONG).show();
				}
			}
		} else{
			Toast.makeText(getApplicationContext(),
					"you don't have any paired device.", Toast.LENGTH_LONG)
					.show();
		}
	}

	// Open connection with your device.
	void openBT() throws IOException {
		Log.v("in openBT", "in openBT");
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // Standard
		// SerialPortService
		// ID
		try {
			DataPref.mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			DataPref.mmSocket.connect();
			DataPref.mmOutputStream = DataPref.mmSocket.getOutputStream();
			DataPref.mmInputStream = DataPref.mmSocket.getInputStream();
			Log.i("streams " + mmDevice.getName(), "," + DataPref.mmOutputStream
					+ "," + DataPref.mmInputStream.available()+", "+DataPref.mmSocket);

			data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00,
					0x00, 0x00, 0x00, (byte) 0xBB };
			beforeSend(data,"looping:1");

		} catch (IOException e) {
			Log.e("error", "Could not connect to device" + e.toString());
			connectError = true;
			//					Toast.makeText(
			//							getApplicationContext(),
			//							"Could not connect to device, socket might closed or timeout",
			//							Toast.LENGTH_LONG).show();
			
		}
	}

	boolean beforeSend(byte[] data, String actualVal) {
		try {

			DataPref.mmOutputStream.write(data);
			if (beginListenForData1(actualVal)) {
				DataPref.mmInputStream = DataPref.mmSocket.getInputStream();
				Log.v("recieved data", "" + DataPref.mmInputStream.available());
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(
					getApplicationContext(),
					"Could not connect to device, socket might closed or timeout",
					Toast.LENGTH_LONG).show();
			return false;
		}
	}

	boolean beginListenForData1(String actualValue) {

		final byte delimiter = 10; // This is the ASCII code for a newline

		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];

		while (!stopWorker) {
			try {
				int bytesAvailable = DataPref.mmInputStream.available();
				if (bytesAvailable > 0) {
					byte[] packetBytes = new byte[bytesAvailable];
					DataPref.mmInputStream.read(packetBytes);
					for (int i = 0; i < bytesAvailable; i++) {
						byte dataByte = packetBytes[i];
						if (dataByte == delimiter) {
							byte[] encodedBytes = new byte[readBufferPosition];
							System.arraycopy(readBuffer, 0, encodedBytes, 0,
									encodedBytes.length);
							final String data = new String(encodedBytes,
									"US-ASCII");

							readBufferPosition = 0;

							Log.i("data value", "," + data);

							if (data != null && data.startsWith(actualValue)) {
								afterSuccess = true;
								//listViewPaired.setVisibility(8);
								dialog.dismiss();
								
								startActivity(new Intent(Splash.this, ImageActivity.class));
								finish();
								overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
								return true;
							}
							return false;

						} else {
							readBuffer[readBufferPosition++] = dataByte;
						}
					}
					return false;
				}
			} catch (IOException ex) {
				stopWorker = true;
				return false;
			}
		}
		return false;
	}

	void successPopup() { // dialog for entering password
		DataPref.dismissLoadingDialog();

		dialog = new Dialog(Splash.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.success_popup);
		WindowManager.LayoutParams layParam = dialog.getWindow().getAttributes();
		layParam.dimAmount = 0.0f;
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		FrameLayout frameLay = (FrameLayout) dialog.findViewById(R.id.rv);
		new ASSL(this, frameLay,1134,720, false);
		listViewPaired = (ListView) dialog.findViewById(R.id.listViewPaired);
		listViewPaired.setAdapter(adapter);
		listViewPaired.setOnItemClickListener(listItemClickedonPaired);

		dialog.setOnCancelListener(new OnCancelListener() 
		{                   
			@Override
			public void onCancel(DialogInterface dialog) 
			{
				Log.e("back press of dialog","back press of dialog");
				dialog.dismiss();           
				if(upperClick)
				{
					upperClick = false;
					divider1.setVisibility(0);
					divider4.setVisibility(0);
					upparHave.setVisibility(0);
					dontRel.setVisibility(0);
				}
				else
				{
					have.setVisibility(0);
					dontHave.setVisibility(0);
					divider1.setVisibility(0);
					divider2.setVisibility(0);
					divider3.setVisibility(0);
				}
			}
		}); 

		dialog.show();
	}

	public boolean processData() {
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00,
				0x00, 0x00, 0x07, 0x00, (byte) 0xBB };
		if(!beforeSend(data,"images loaded from radio\r"))
		{
			return false;
		}

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x05,
				0x00, 0x01, 0x00, 0x00, 0x00, 0x12,(byte) 0xBB };
		if(!beforeSend(data,"matrix brightness:18"))
		{
			return false;
		}

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04,
				0x00, 0x00, 0x00, 0x04, 0x00, (byte) 0xBB };
		if(!beforeSend(data,"display:1"))
		{
			return false;
		}

		return true;
	}

	void closeBT() throws IOException {
		stopWorker = true;
		// Data.mmOutputStream.close();
		// Data.mmInputStream.close();
		DataPref.mmSocket.close();
	}

	public class BluetoothBg extends AsyncTask<String, String, String>
	{
		ProgressDialog dialog;
		Boolean haveBt=false;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = ProgressDialog.show(Splash.this, "", "Loading... ",true);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.cancel();
			if(haveBt){
				Toast.makeText(getApplicationContext(),
						"No bluetooth adapter available", Toast.LENGTH_LONG)
						.show();
			}
			if(afterSuccess)
			{
				startActivity(new Intent(Splash.this, ImageActivity.class));
				finish();
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
				haveBt = true;
				Log.i("you don't have bluetooth device",".");
			}

			if (!mBluetoothAdapter.isEnabled()) {
				mBluetoothAdapter.enable();
				findBT();
			} else {
				findBT();
			}
			Log.v("background","backgroud");
			return null;
		}

	}

	public class AsyncD extends AsyncTask<String, String, String>
	{
		ProgressDialog dialog;
		Boolean haveBt=false;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			DataPref.showLoadingDialog(Splash.this, "Connecting...");
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			DataPref.dismissLoadingDialog();
			if(connectError)
			{
				dialogPopup("Could not connect to device, socket might closed or timeout.");
				connectError = false;
			}
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				openBT();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
	
	/**
	 * Displays default android dialog for displaying alert message to user
	 * @param message string
	 */
	void dialogPopup(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
		builder.setMessage("" + message).setTitle(R.string.app_name);
		// builder.setIcon(R.drawable.tobuy_icon);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alertDialog = builder.create();
		//	      alertDialog.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
		alertDialog.show();
	}

}
