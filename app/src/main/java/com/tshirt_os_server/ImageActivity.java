package com.tshirt_os_server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.tshirt_os.extras.AppStatus;
import com.tshirt_os.extras.Item;
import com.tshirt_os_utils.Alphabets;
import com.tshirt_os_utils.Blank;
import com.tshirt_os_utils.YoloGif;

public class ImageActivity extends Activity {
	Button settings, gif, low, medium, high, textBtn, activate, disable,
			speedLow, speedMid, speedHigh;
	RelativeLayout relLay, container, settingCont, textCont;
	TextView contHead, choose, connected, bright, settingTxt, count,
			displayTxt, serverTxt, speedTxt, staticTxt;
	byte[] data = null;
	byte[] bettaryData = null;
	Images images;
	int entFram;
	int runLength = 28;
	byte blankRuntxt[];
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	ImageView divider3, divider2, divider1, onOffBtn, serverOnOff, textOnOff;
	volatile boolean stopWorker;
	Boolean batteryInfo = false;
	ProgressDialog dialog, dialog1;
	YoloGif yolo;
	ConnectionDetector connDect;
	Boolean isInternetPresent1 = false, serverSuccess = false,
			textBtnClick = false;
	Map<String, byte[]> dataMap;
	boolean successImg[];

	ImageLoader imLoader;
	EditText et;
	Alphabets alpbt;
	Field[] fieldsArray = new Field[120];
	Field[] fieldsArrayTop = new Field[15];
	Field[] fieldsArrayBot = new Field[15];
	Field[] fieldsArraySpl = new Field[120];

	private static final int SIZEOFIINTINHALFBYTES = 8;
	private static final int NUMBEROFBITSINAHALFBYTE = 4;
	private static final int HALFBYTE = 0x0F;
	private static final char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	byte[][] byteArr;
	byte[][] byteArrMid;
	byte[][] byteArrBot;
	byte[][] byteArrRunn;
	Object obj, objTop, objBot, splObj, botSplObj, topSplObj;
	Field fields[], fieldsTop[], fieldsBot[], splFields[] = null,
			topSplFields[] = null, botSplFields[] = null;
	RadioGroup radioGroup;
	RadioButton radioBtn;
	GridView gridView;
	// LinearLayout parLayout;
	String url = "http://54.191.19.160/aop/admin_prod/";
//	String url = "http://54.191.19.160/aop/admin_dev/";
	int totalHeight = 0, num = 0;
	View view = null;
	int speedLimit = 4;
	public boolean run = false, textOff = false, staticTxtRun;
	Map<Character, String> spclCharMap = new HashMap<Character, String>();
	Map<Character, String> botspclCharMap = new HashMap<Character, String>();
	Map<Character, String> topspclCharMap = new HashMap<Character, String>();
	Class splChar, botSplChar, topSplChar;
	Handler handler;

	private ImageView bat_status, imageView3, imageView4;
	private int textlength = 120;
	private GifAdapter adapter;
	ArrayList<ImageData> gridArray = new ArrayList<ImageData>();
	public List<Item> items = new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		relLay = (RelativeLayout) findViewById(R.id.rv);
		new ASSL(this, relLay, 1134, 720, false);
		// setupUI(findViewById(R.id.rv));
		settings = (Button) findViewById(R.id.setting);
		contHead = (TextView) findViewById(R.id.image_txt);
		choose = (TextView) findViewById(R.id.textView1);

		gif = (Button) findViewById(R.id.gif);

		divider3 = (ImageView) findViewById(R.id.divider_3);
		divider2 = (ImageView) findViewById(R.id.divider_2);
		divider1 = (ImageView) findViewById(R.id.divider_1);
		container = (RelativeLayout) findViewById(R.id.container);
		settingCont = (RelativeLayout) findViewById(R.id.setting_container);
		textCont = (RelativeLayout) findViewById(R.id.text_container);
		connected = (TextView) findViewById(R.id.con_txt);
		bright = (TextView) findViewById(R.id.bright_txt);
		count = (TextView) findViewById(R.id.batery_txt);
		bat_status = (ImageView) findViewById(R.id.imageView1);
		imageView3 = (ImageView) findViewById(R.id.imageView3);
		imageView4 = (ImageView) findViewById(R.id.imageView4);

		activate = (Button) findViewById(R.id.activate);
		disable = (Button) findViewById(R.id.disable);
		settingTxt = (TextView) findViewById(R.id.textView4);
		et = (EditText) findViewById(R.id.editText1);
		low = (Button) findViewById(R.id.low);
		medium = (Button) findViewById(R.id.medium);
		high = (Button) findViewById(R.id.high);
		displayTxt = (TextView) findViewById(R.id.display_txt);
		onOffBtn = (ImageView) findViewById(R.id.on_off);
		serverTxt = (TextView) findViewById(R.id.server_txt);
		serverOnOff = (ImageView) findViewById(R.id.server_on_off);
		textBtn = (Button) findViewById(R.id.text_btn);

		staticTxt = (TextView) findViewById(R.id.static_txt);
		textOnOff = (ImageView) findViewById(R.id.text_on_off);
		gridView = (GridView) findViewById(R.id.grid_view);
		// parLayout = (LinearLayout) findViewById(R.id.par_lay);
		speedTxt = (TextView) findViewById(R.id.speed_txt);
		speedLow = (Button) findViewById(R.id.speed_low);
		speedMid = (Button) findViewById(R.id.speed_medium);
		speedHigh = (Button) findViewById(R.id.speed_high);
		displayTxt.setTypeface(DataPref.dinMedium(getApplicationContext()));
		serverTxt.setTypeface(DataPref.dinMedium(getApplicationContext()));
		connected.setTypeface(DataPref.dinMedium(getApplicationContext()));
		bright.setTypeface(DataPref.dinMedium(getApplicationContext()));
		speedTxt.setTypeface(DataPref.dinMedium(getApplicationContext()));
		staticTxt.setTypeface(DataPref.dinMedium(getApplicationContext()));
		settingTxt.setTypeface(DataPref.dinMedium(getApplicationContext()));
		count.setTypeface(DataPref.dinMedium(getApplicationContext()));
		settings.setTypeface(DataPref.dinMedium(getApplicationContext()));
		contHead.setTypeface(DataPref.dinMedium(getApplicationContext()));
		choose.setTypeface(DataPref.dinMedium(getApplicationContext()));
		gif.setTypeface(DataPref.dinMedium(getApplicationContext()));

		images = new Images();
		yolo = new YoloGif();
		alpbt = new Alphabets();
		handler = new Handler();
		imLoader = new ImageLoader(getApplicationContext());
		connDect = new ConnectionDetector(getApplicationContext());
		isInternetPresent1 = connDect.isConnectingToInternet();

		botspclCharMap.put('!', "botXcla");
		botspclCharMap.put('"', "botQuots");
		botspclCharMap.put('#', "botHash");
		botspclCharMap.put('$', "botDollar");
		botspclCharMap.put('%', "botPerc");
		botspclCharMap.put('&', "botAnd");
		botspclCharMap.put('(', "botLeftBrac");
		botspclCharMap.put(')', "botRightBrac");
		botspclCharMap.put('*', "botStar");
		botspclCharMap.put('+', "botPlus");
		botspclCharMap.put(',', "botComma");
		botspclCharMap.put('-', "botMinus");
		botspclCharMap.put('0', "botZero");
		botspclCharMap.put('1', "botOne");
		botspclCharMap.put('2', "botTwo");
		botspclCharMap.put('3', "botThree");
		botspclCharMap.put('4', "botFour");
		botspclCharMap.put('5', "botFive");
		botspclCharMap.put('6', "botSix");
		botspclCharMap.put('7', "botSeven");
		botspclCharMap.put('8', "botEight");
		botspclCharMap.put('9', "botNine");
		botspclCharMap.put(':', "botColon");
		botspclCharMap.put(';', "botSemiColon");
		botspclCharMap.put('<', "botLeftArrow");
		botspclCharMap.put('=', "botEqual");
		botspclCharMap.put('>', "botRightArrow");
		botspclCharMap.put('?', "botQues");
		botspclCharMap.put('@', "botAtRate");
		botspclCharMap.put('[', "botLeftSquareBrac");
		botspclCharMap.put('\\', "botBackSlash");
		botspclCharMap.put(']', "botRightSquareBrac");
		botspclCharMap.put('^', "botXor");
		botspclCharMap.put('_', "bot_underscore");
		botspclCharMap.put('`', "botUpparDot");
		botspclCharMap.put('.', "botDot");
		botspclCharMap.put(':', "botColon");
		botspclCharMap.put('{', "botLeftCurlyBrac");
		botspclCharMap.put('|', "botOrOpp");
		botspclCharMap.put('}', "botRightCurlyBrac");
		botspclCharMap.put('~', "botTild");
		botspclCharMap.put('£', "botPound");
		botspclCharMap.put('€', "botEuro");
		botspclCharMap.put('¥', "botYen");

		spclCharMap.put('!', "xcla");
		spclCharMap.put('"', "quots");
		spclCharMap.put('#', "hash");
		spclCharMap.put('$', "dollar");
		spclCharMap.put('%', "perc");
		spclCharMap.put('&', "and");
		spclCharMap.put('(', "leftBrac");
		spclCharMap.put(')', "rightBrac");
		spclCharMap.put('*', "star");
		spclCharMap.put('+', "plus");
		spclCharMap.put(',', "comma");
		spclCharMap.put('-', "minus");
		spclCharMap.put('0', "zero");
		spclCharMap.put('1', "one");
		spclCharMap.put('2', "two");
		spclCharMap.put('3', "three");
		spclCharMap.put('4', "four");
		spclCharMap.put('5', "five");
		spclCharMap.put('6', "six");
		spclCharMap.put('7', "seven");
		spclCharMap.put('8', "eight");
		spclCharMap.put('9', "nine");
		spclCharMap.put(':', "colon");
		spclCharMap.put(';', "semiColon");
		spclCharMap.put('<', "leftArro");
		spclCharMap.put('=', "equal");
		spclCharMap.put('>', "rightArrow");
		spclCharMap.put('?', "ques");
		spclCharMap.put('@', "atRate");
		spclCharMap.put('[', "leftSquareBrac");
		spclCharMap.put('\\', "backSlash");
		spclCharMap.put(']', "rightSquareBrac");
		spclCharMap.put('^', "xor");
		spclCharMap.put('_', "underscore");
		spclCharMap.put('`', "upparDot");
		spclCharMap.put('.', "dot");
		spclCharMap.put(':', "colon1");
		spclCharMap.put('{', "leftCurlyBrac");
		spclCharMap.put('|', "orOpp");
		spclCharMap.put('}', "rightCurlyBrac");
		spclCharMap.put('~', "tild");
		spclCharMap.put('�', "pound");
		spclCharMap.put('�', "euro");
		spclCharMap.put('�', "yen");

		topspclCharMap.put('!', "topXcla");
		topspclCharMap.put('"', "topQuots");
		topspclCharMap.put('#', "topHash");
		topspclCharMap.put('$', "topDollar");
		topspclCharMap.put('%', "topPerc");
		topspclCharMap.put('&', "topAnd");
		topspclCharMap.put('(', "topLeftBrac");
		topspclCharMap.put(')', "topRightBrac");
		topspclCharMap.put('*', "topStar");
		topspclCharMap.put('+', "topPlus");
		topspclCharMap.put(',', "topComma");
		topspclCharMap.put('-', "topMinus");
		topspclCharMap.put('0', "topZero");
		topspclCharMap.put('1', "topOne");
		topspclCharMap.put('2', "topTwo");
		topspclCharMap.put('3', "topThree");
		topspclCharMap.put('4', "topFour");
		topspclCharMap.put('5', "topFive");
		topspclCharMap.put('6', "topSix");
		topspclCharMap.put('7', "topSeven");
		topspclCharMap.put('8', "topEight");
		topspclCharMap.put('9', "topNine");
		topspclCharMap.put(':', "topColon");
		topspclCharMap.put(';', "topSemiColon");
		topspclCharMap.put('<', "topLeftArrow");
		topspclCharMap.put('=', "topEqual");
		topspclCharMap.put('>', "topRightArrow");
		topspclCharMap.put('?', "topQues");
		topspclCharMap.put('@', "topAtRate");
		topspclCharMap.put('[', "topLeftSquareRrac");
		topspclCharMap.put('\\', "topBackSlash");
		topspclCharMap.put(']', "topRightSquareBrac");
		topspclCharMap.put('^', "topXor");
		// topspclCharMap.put('_', "top_underscore");
		topspclCharMap.put('`', "topUpparDot");
		topspclCharMap.put('.', "topDot");
		// topspclCharMap.put(':', "top_colon1");
		topspclCharMap.put('{', "topLeftCurlyBrac");
		topspclCharMap.put('|', "topOrOpp");
		topspclCharMap.put('}', "topRightCurlyBrac");
		topspclCharMap.put('~', "topTild");
		topspclCharMap.put('�', "topPound");
		topspclCharMap.put('�', "topEuro");
		topspclCharMap.put('�', "topYen");

		try {
			Class cls = Class.forName("com.tshirt_os_utils.Alphabets");
			obj = cls.newInstance();
			fields = cls.getDeclaredFields();

			Class clsTop = Class.forName("com.tshirt_os_utils.Top_alphabet");
			objTop = clsTop.newInstance();
			fieldsTop = clsTop.getDeclaredFields();

			Class clsBot = Class.forName("com.tshirt_os_utils.Bottom_alphabet");
			objBot = clsBot.newInstance();
			fieldsBot = clsBot.getDeclaredFields();

			splChar = Class.forName("com.tshirt_os_utils.Special_Char");
			splObj = splChar.newInstance();
			splFields = splChar.getDeclaredFields();

			botSplChar = Class.forName("com.tshirt_os_utils.BotSpecialChar");
			botSplObj = botSplChar.newInstance();
			botSplFields = botSplChar.getDeclaredFields();

			topSplChar = Class.forName("com.tshirt_os_utils.TopSpecialChar");
			topSplObj = topSplChar.newInstance();
			topSplFields = topSplChar.getDeclaredFields();

		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		items.add(new Item("Dvd", R.drawable.dvdptwave, 45, 5));
		items.add(new Item("Ilsf", R.drawable.ilsf, 10, 10));
		items.add(new Item("HorseRunning", R.drawable.horserunning, 11, 10));
		items.add(new Item("HeartSM2", R.drawable.heartsm, 13, 20));
		items.add(new Item("Goal", R.drawable.goal, 68, 10));
		items.add(new Item("Invade", R.drawable.invade, 71, 20));
		items.add(new Item("PreviewSunLines", R.drawable.previewsunlines, 24, 6));
		
		items.add(new Item("Heartbeat", R.drawable.heartbeat, 3, 20));
		items.add(new Item("Running2", R.drawable.running, 11, 12));
		items.add(new Item("Matrix", R.drawable.matrix, 16, 8));
		items.add(new Item("Spiral", R.drawable.spiral, 4, 6));
		items.add(new Item("BirdFlying", R.drawable.birdflying, 13, 10));
		
		items.add(new Item("Tie_graphics", R.drawable.tie_graphics, 64, 10));
		items.add(new Item("Uniqlo", R.drawable.uniqlo_basic, 49, 10));
		
//		items.add(new Item("15", R.drawable.image15, 0, 0));
//		items.add(new Item("16", R.drawable.image16, 0, 0));
//		items.add(new Item("17", R.drawable.image17, 0, 0));
//		items.add(new Item("18", R.drawable.image18, 0, 0));
//		items.add(new Item("19", R.drawable.image19, 0, 0));
		
		
		// isInternetPresent = false;
		if (DataPref.serverOff) {
			if (AppStatus.getInstance(getApplicationContext()).isOnline(getApplicationContext())) {
				showData();

			} else {
				dialogPopup("You don't have Internet Connection.");
				// Toast.makeText(getApplicationContext(),
				// "You don't have Internet Connection.",
				// Toast.LENGTH_LONG).show();
				serverSuccess = false;
				processNormalImages();
			}
		} else {
			serverSuccess = false;
			processNormalImages();
		}

		speedLow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				if (!textOff) {
					run = false;
					speedLow.setBackgroundResource(R.drawable.s_low_press);
					speedMid.setBackgroundResource(R.drawable.s_mid);
					speedHigh.setBackgroundResource(R.drawable.s_high);
					speedLimit = 12;
					if (et.getText().toString().length() > 0) {
						runLength = 28;
						new AsyncD_runn_text().execute("");
					}
				}
			}
		});
		speedMid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				if (!textOff) {
					run = false;
					speedLow.setBackgroundResource(R.drawable.s_low);
					speedMid.setBackgroundResource(R.drawable.s_mid_press);
					speedHigh.setBackgroundResource(R.drawable.s_high);
					speedLimit = 4;
					if (et.getText().toString().length() > 0) {
						runLength = 28;
						new AsyncD_runn_text().execute("");
					}
				}
			}
		});
		speedHigh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				if (!textOff) {
					run = false;
					speedLow.setBackgroundResource(R.drawable.s_low);
					speedMid.setBackgroundResource(R.drawable.s_mid);
					speedHigh.setBackgroundResource(R.drawable.s_high_press);
					speedLimit = 2;
					if (et.getText().toString().length() > 0) {
						runLength = 28;
						new AsyncD_runn_text().execute("");
					}
				}
			}
		});

		serverTxt.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view1) {
				serverPopup();
				return false;
			}
		});

		onOffBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view1) {
				FlurryAgent.logEvent("Display on/off button clicked.");
				run = false;
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (textBtnClick) {
							// getByteArr(4, 50, "Blank");
							sendBlank();
							// Toast.makeText(getApplicationContext(),"Text button clicked.",Toast.LENGTH_LONG).show();
							textBtnClick = false;
						}

						if (DataPref.off) {
							DataPref.off = false;
							onOffBtn.setBackgroundResource(R.drawable.on_btn);
							data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04,
									0x00, 0x00, 0x00, 0x04, 0x00, (byte) 0xBB };
							beforeSend1(data, "display:0");
						} else {
							DataPref.off = true;
							onOffBtn.setBackgroundResource(R.drawable.off_btn);
							data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04,
									0x00, 0x00, 0x00, 0x03, 0x00, (byte) 0xBB };
							beforeSend1(data, "display:1");
						}
					}
				}, 500);

			}
		});

		textOnOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				if (textOff) {
					et.setHint("120 Characters only");
					textlength = 120;
					// et.setKeyListener(DigitsKeyListener.getInstance(",{}[]()*^%$#>!~,?/|\`@+-_.�۴;:`&lt;
					// abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
					textOff = false;
					textOnOff.setBackgroundResource(R.drawable.off_btn);
				} else {
					et.setHint("5 Characters only");
					textlength = 5;
					// et.setKeyListener(TextKeyListener.);

					// et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
					textOff = true;
					// staticTxtRun = true;d
					textOnOff.setBackgroundResource(R.drawable.on_btn);
				}

			}
		});

		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(textlength);
		et.setFilters(FilterArray);

		et.addTextChangedListener(new TextWatcher() {

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 0) {
					int count1 = textlength;
					count1 = count1 - s.length();
					Log.e("asfdsaf", "" + count1 + "," + count);
					// if (count1 == 0) {
					// disable.setVisibility(View.GONE);
					// activate.setVisibility(View.VISIBLE);
					// } else {
					// if (count1 == textlength) {
					// disable.setVisibility(View.VISIBLE);
					// activate.setVisibility(View.GONE);
					// } else {
					// disable.setVisibility(View.GONE);
					// activate.setVisibility(View.VISIBLE);
					// }
					// }
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		serverOnOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view1) {
				FlurryAgent.logEvent("server on/off button clicked.");
				if (DataPref.serverOff) {

					// RelativeLayout.LayoutParams p = new
					// RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					// (int) (185.0f * ASSL.Yscale()));
					// p.addRule(RelativeLayout.BELOW, R.id.supported);
					// imageView3.setLayoutParams(p);

					// removeImages();
					// img = null;
					DataPref.serverOff = false;
					serverSuccess = false;
					serverOnOff.setBackgroundResource(R.drawable.off_btn);
					// parLayout.setVisibility(View.VISIBLE);
					gridView.setVisibility(View.VISIBLE);
					processNormalImages();
				} else {

					serverOnOff.setBackgroundResource(R.drawable.on_btn);
					if (AppStatus.getInstance(getApplicationContext()).isOnline(getApplicationContext())) {
						DataPref.serverOff = true;
						showData();
					} else {
						dialogPopup("You don't have Internet Connection.");
						// Toast.makeText(getApplicationContext(),
						// "You don't have Internet Connection.",
						// Toast.LENGTH_LONG).show();
						serverSuccess = false;
						DataPref.serverOff = false;
						serverOnOff.setBackgroundResource(R.drawable.off_btn);
						processNormalImages();
					}
				}
			}
		});

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x05, 0x00, 0x01, 0x00,
				0x00, 0x00, 0x63, (byte) 0xBB };
		data[8] = (byte) 0x3F;
		beforeSend(data);

		getBattery();
		// sendBlank();
		sendFlush();

		low.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				FlurryAgent.logEvent("low display clicked.");
				low.setBackgroundResource(R.drawable.low_onclick);
				medium.setBackgroundResource(R.drawable.medium);
				high.setBackgroundResource(R.drawable.high);
				data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x05, 0x00, 0x01,
						0x00, 0x00, 0x00, 0x63, (byte) 0xBB };
				data[8] = (byte) 0x05;
				beforeSend(data);
			}
		});

		medium.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				FlurryAgent.logEvent("medium display clicked.");
				low.setBackgroundResource(R.drawable.low);
				medium.setBackgroundResource(R.drawable.medium_onclick);
				high.setBackgroundResource(R.drawable.high);
				data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x05, 0x00, 0x01,
						0x00, 0x00, 0x00, 0x63, (byte) 0xBB };
				data[8] = (byte) 0x1F;
				beforeSend(data);
			}
		});

		high.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				FlurryAgent.logEvent("high display clicked.");
				low.setBackgroundResource(R.drawable.low);
				medium.setBackgroundResource(R.drawable.medium);
				high.setBackgroundResource(R.drawable.high_onclick);
				data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x05, 0x00, 0x01,
						0x00, 0x00, 0x00, 0x63, (byte) 0xBB };
				data[8] = (byte) 0x3F;
				beforeSend(data);
			}
		});

		settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				FlurryAgent.logEvent("settings button clicked.");
				// run = false;
				count.setText(DataPref.batteryStatus);

				String val[] = DataPref.batteryStatus.split("%");
				int value = Integer.parseInt(val[0]);

				if (value > 60) {
					bat_status.setImageResource(R.drawable.full_battery);
				} else if (value > 30) {
					bat_status.setImageResource(R.drawable.battery);
				} else if (value > 5) {
					bat_status.setImageResource(R.drawable.half_battery);
				} else {
					bat_status.setImageResource(R.drawable.low_battery);
				}

				settingCont.setVisibility(View.VISIBLE);
				textCont.setVisibility(View.GONE);
				container.setVisibility(View.GONE);

				imageView4.setVisibility(View.VISIBLE);
				imageView3.setVisibility(View.GONE);

				settings.setVisibility(View.GONE);
				gif.setVisibility(View.VISIBLE);
				textBtn.setVisibility(View.VISIBLE);

				divider1.setVisibility(View.GONE);
				divider2.setVisibility(View.VISIBLE);
				divider3.setVisibility(View.VISIBLE);

			}
		});

		textBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {

				FlurryAgent.logEvent("text button clicked.");
				// supported.setMinimumHeight((int) (800 * ASSL.Yscale()));

				settingCont.setVisibility(View.GONE);
				textCont.setVisibility(View.VISIBLE);
				container.setVisibility(View.GONE);

				imageView4.setVisibility(View.VISIBLE);
				imageView3.setVisibility(View.GONE);

				settings.setVisibility(View.VISIBLE);
				gif.setVisibility(View.VISIBLE);
				textBtn.setVisibility(View.GONE);

				divider1.setVisibility(View.VISIBLE);
				divider2.setVisibility(View.GONE);
				divider3.setVisibility(View.VISIBLE);
			}
		});

		gif.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {

				textBtnClick = false;
				if (img != null) {
					img.setAlpha(1.0f);
					img = null;
				}

				FlurryAgent.logEvent("gif button clicked.");

				settingCont.setVisibility(View.GONE);
				textCont.setVisibility(View.GONE);
				container.setVisibility(View.VISIBLE);

				imageView4.setVisibility(View.VISIBLE);
				imageView3.setVisibility(View.GONE);

				settings.setVisibility(View.VISIBLE);
				gif.setVisibility(View.GONE);
				textBtn.setVisibility(View.VISIBLE);

				divider1.setVisibility(View.VISIBLE);
				divider2.setVisibility(View.VISIBLE);
				divider3.setVisibility(View.GONE);

				// container.setVisibility(View.VISIBLE);
				// textCont.setVisibility(View.GONE);
				// settingCont.setVisibility(View.GONE);
				//
				// divider3.setVisibility(4);
				// gif.setVisibility(4);
				//
				// imageView4.setVisibility(View.VISIBLE);
				// imageView3.setVisibility(View.GONE);

			}
		});

		// gifMid.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View view1) {
		//
		//
		// textBtnClick = false;
		// if (img != null) {
		// img.setAlpha(1.0f);
		// img = null;
		// }
		//
		// // supported.setMinimumHeight((int) (2170 * ASSL.Yscale()));
		// if (DataPref.serverOff) {
		// // supported.setMinimumHeight(367 + (int)
		// // ((((Data.name.length + 2)/3) * 211) * ASSL.Yscale()));
		// ViewGroup.LayoutParams params2 = supported
		// .getLayoutParams();
		// // params2.height = (num * 250) + 665;
		// int min = (int) Math.min(ASSL.Xscale(), ASSL.Yscale());
		// params2.height = (num * (280*min)) + (665*min);
		// supported.setLayoutParams(params2);
		// } else {
		// // supported.setMinimumHeight((int) (2170 * ASSL.Yscale()));
		// ViewGroup.LayoutParams params2 = supported
		// .getLayoutParams();
		// params2.height = (int) (2170 * ASSL.Yscale());
		// supported.setLayoutParams(params2);
		//
		// ViewGroup.LayoutParams params1 = container.getLayoutParams();
		// params1.height = LayoutParams.WRAP_CONTENT;
		// container.setLayoutParams(params1);
		// }
		//
		// AlphaAnimation anim = new AlphaAnimation(0, 1);
		// anim.setDuration(1000);
		// anim.setFillAfter(false);
		//
		// textCont.setVisibility(View.GONE);
		// gifMid.setVisibility(4);
		// divider11.setVisibility(4);
		//
		// settingCont.setVisibility(View.GONE);
		// gifLow.setVisibility(View.GONE);
		// divider5.setVisibility(View.GONE);
		//
		// divider1.setVisibility(View.VISIBLE);
		// settings.setVisibility(View.VISIBLE);
		// container.setVisibility(View.VISIBLE);
		//
		// divider1.clearAnimation();
		// settings.clearAnimation();
		// container.clearAnimation();
		//
		// divider1.setAnimation(anim);
		// settings.setAnimation(anim);
		// container.setAnimation(anim);
		//
		// divider9.setVisibility(View.VISIBLE);
		// textMid.setVisibility(View.VISIBLE);
		//
		// divider8.setVisibility(4);
		// textLow.setVisibility(4);
		// }
		// });

		// textLow.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View view1) {
		//
		// // supported.setMinimumHeight((int) (800 * ASSL.Yscale()));
		// ViewGroup.LayoutParams params2 = supported.getLayoutParams();
		// params2.height = (int) (1200 * ASSL.Yscale());
		// supported.setLayoutParams(params2);
		//
		// settingCont.setVisibility(View.GONE);
		//
		// textLow.setVisibility(View.GONE);
		// divider8.setVisibility(View.GONE);
		// gifLow.setVisibility(View.GONE);
		// divider5.setVisibility(View.GONE);
		//
		// settings.setVisibility(View.VISIBLE);
		// divider1.setVisibility(View.VISIBLE);
		// textCont.setVisibility(View.VISIBLE);
		// divider11.setVisibility(View.VISIBLE);
		// gifMid.setVisibility(View.VISIBLE);
		// }
		// });

		// textMid.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View view1) {
		//
		// // supported.setMinimumHeight((int) (800 * ASSL.Yscale()));
		// ViewGroup.LayoutParams params2 = supported.getLayoutParams();
		// params2.height = (int) (1200 * ASSL.Yscale());
		// supported.setLayoutParams(params2);
		//
		// container.setVisibility(View.GONE);
		//
		// textMid.setVisibility(View.GONE);
		// divider9.setVisibility(View.GONE);
		// gifLow.setVisibility(View.GONE);
		// divider5.setVisibility(View.GONE);
		//
		// settings.setVisibility(View.VISIBLE);
		// divider1.setVisibility(View.VISIBLE);
		// textCont.setVisibility(View.VISIBLE);
		// divider11.setVisibility(View.VISIBLE);
		// gifMid.setVisibility(View.VISIBLE);
		// }
		// });

		et.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView textView, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					// text_process();
					// running_text_process();
					textBtnClick = true;
					if (textOff) {
						run = false;
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								textProcess();
							}
						}, 500);

					} else {
						sendFlush2();
						blankRuntxt = new byte[5376];
						if (run) {
							run = false;
						}

						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								runLength = 28;
								new AsyncD_runn_text().execute("");
								run = true;
							}
						}, 1000);

					}
				}
				return false;
			}
		});

		activate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				// text_process();
				// running_text_process();
				textBtnClick = true;
				if (textOff) {
					Log.v("static text clicked.", "static text clicked.");

					run = false;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							textProcess();
						}
					}, 500);

				} else {
					Log.v("running text clicked.", "running text clicked.");
					sendFlush2();
					blankRuntxt = new byte[5376];
					if (run) {
						run = false;
					}

					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							runLength = 28;
							new AsyncD_runn_text().execute("");
							run = true;
						}
					}, 1000);

				}
			}
		});

		/**
		 * On Click event for Single Gridview Item
		 * */

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view1,
					int position, long itemId) {
				run = false;
				if (view != null) {
					view.setAlpha(1.0f);
				}

				if (view == view1) {
					view = null;
					getByteArr(4, 50, "Blank");
				} else {
					Log.v("server img click..", "server img click,,,");
					view = view1;
					view1.setAlpha(0.5f);
					Toast.makeText(getApplicationContext(), "serverSuccess", Toast.LENGTH_LONG)
							.show();
					// new AsyncD().execute(position);
				}
			}
		});

	}

	public void runningTextProcess() {
		try {
			FlurryAgent.logEvent("text activated button clicked.");
			// Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
			// Matcher m = p.matcher(et.getText());
			// boolean b = m.find();

			byteArr = new byte[50][];
			byteArrMid = new byte[50][];
			byteArrRunn = new byte[120][];
			byteArrBot = new byte[50][];

			byte blank[] = new byte[224];

			// if (et.getText().length() > 15 || b)
			// dialog_popup("You can enter only 15 characters.");
			// else {

			String temp = et.getText().toString();
			temp = temp.toUpperCase();
			int tempno = 0;
			// int a = 0, d = 0, loop_val = 0;
			String finalStr = "";
			finalStr = temp;

			byteArrMid = new byte[finalStr.length()][];
			if (finalStr.length() <= 120) {

				for (int i = 0; i < finalStr.length(); i++) {
					tempno = 0;
					for (char c = 'A'; c <= 'Z'; c++) {
						if (finalStr.charAt(i) == c
								|| finalStr.charAt(i) == ' ') {
							try {

								if (finalStr.charAt(i) == ' ') {
									fieldsArray[i] = fields[26];
									byteArrMid[i] = (byte[]) fieldsArray[i]
											.get(obj);
									// byteArr_runn[i] = (byte[])
									// fields_array[i].get(obj);
								} else {
									fieldsArray[i] = fields[tempno];
									byteArrMid[i] = (byte[]) fieldsArray[i].get(obj);
									// byteArr_runn[i] = (byte[])
									// fields_array[i].get(obj);
								}
								byteArr[i] = (byte[]) fields[26].get(obj);
								byteArrBot[i] = (byte[]) fields[26].get(obj);
								// a++;
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							break;
						}
						tempno = tempno + 1;
						// Log.v("length of char ",""+tempno);
					}
				}

				// set load from radio
				data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00,
						0x00, 0x07, 0x00, (byte) 0xBB };
				beforeSend1(data, "images loaded from radio");

				// set to run from radio sync messages broadcast

				data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x00,
						0x00, 0x09, 0x00, (byte) 0x02, 0x00, (byte) 0xBB };
				data[8] = (byte) speedLimit;
				beforeSend1(data, "mdoe:radio");

				byte blankRuntxt[] = new byte[5376];
				int idx = 223;
				int runnLen = 28;

				try {

					for (int i = 0; i < byteArrMid.length; i++) {
						runnLen = runnLen + byteArrMid[i].length / 8;
						Log.v("length", "" + byteArrMid.length + ", " + byteArrMid[i].length);
						for (int j = 0; j < byteArrMid[i].length; j++) {
							blankRuntxt[++idx] = (byte) (byteArrMid[i][j]);
						}
					}

					data = new byte[235];
					for (int j = 0; j < 1; j++) {
						for (int i = 0; i < runnLen; i++) {
							for (int t = 0; t < 224; t++) {
								data[10 + t] = blankRuntxt[t + (i * 8)];
								// data[10 + t] = byteArr_mid[i][t];
							}

							data[0] = (byte) 0xAA;
							data[1] = (byte) 0xAA;
							data[2] = (byte) 0xE6;
							data[3] = (byte) 0x00;
							data[4] = (byte) 0x02;
							data[5] = (byte) 0x00;
							data[6] = (byte) 0x01;
							data[7] = (byte) 0x00;
							data[8] = (byte) 0x02;
							data[9] = (byte) 0x00;
							data[10 + 224] = (byte) 0xBB;

							int lastEntFram = 2;
							byte[] newData = new byte[2];
							newData[0] = (byte) (lastEntFram & 0xFF);
							newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
							data[8] = newData[0];
							data[9] = newData[1];
							String result = "buf:0," + lastEntFram;
							// Log.v("Last result value",""+result);
							beforeSend1(data, result);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				dialogPopup("Please use a shorter message.");
			}

		} catch (Exception e) {
			Log.e("error", "" + e.toString());
			dialogPopup("This string contains I,M,N,Q,R,T,U,V,W,X,Y. They take more space than other characters. It is not possible to be displayed.");
		}
		// new AsyncD_runn_text().cancel(true);
	}

	// *******************

	class AsyncD_runn_text extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Data.showLoadingDialog(ImageActivity.this, "Loading...");
		}

		@Override
		protected String doInBackground(String... params) {

			// Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
			// Matcher m = p.matcher(et.getText());
			// boolean b = m.find();

			byteArrMid = new byte[120][];
			byteArrRunn = new byte[120][];
			String finalStr = "";
			finalStr = et.getText().toString();
			finalStr = finalStr.toUpperCase();
			int tempno = 0;

			byteArrMid = new byte[finalStr.length()][];
			if (finalStr.length() <= 120) {

				for (int i = 0; i < finalStr.length(); i++) {
					tempno = 0;
					try {
						if (finalStr.charAt(i) < 'A'
								|| finalStr.charAt(i) > 'Z') {
							if (finalStr.charAt(i) == ' ') {

								fieldsArray[i] = fields[26];
								byteArrMid[i] = (byte[]) fieldsArray[i]
										.get(obj);
							} else {
								Log.e("ASCII value ",
										"," + (int) finalStr.charAt(i));

								String position = spclCharMap.get(finalStr
										.charAt(i));
								fieldsArraySpl[i] = splChar.getField(position);
								byteArrMid[i] = (byte[]) fieldsArraySpl[i]
										.get(splObj);
							}
						} else {
							for (char c = 'A'; c <= 'Z'; c++) {
								if (finalStr.charAt(i) == c) {
									Log.e("here in else part",
											"here in else part");

									fieldsArray[i] = fields[tempno];
									byteArrMid[i] = (byte[]) fieldsArray[i].get(obj);

									break;
								}
								tempno = tempno + 1;
								// Log.v("length of char ",""+tempno);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// set load from radio
				data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00,
						0x00, 0x07, 0x00, (byte) 0xBB };
				beforeSend1(data, "images loaded from radio");

				// set to run from radio sync messages broadcast

				data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x00,
						0x00, 0x09, 0x00, (byte) 0x02, 0x00, (byte) 0xBB };
				data[8] = (byte) speedLimit;
				beforeSend1(data, "mdoe:radio");

				int idx = 223;

				try {
					for (int i = 0; i < byteArrMid.length; i++) {
						runLength = runLength + byteArrMid[i].length / 8;
						Log.v("length", "" + byteArrMid.length + ", " + byteArrMid[i].length);
						for (int j = 0; j < byteArrMid[i].length; j++) {
							blankRuntxt[++idx] = (byte) (byteArrMid[i][j]);
						}
					}

					data = new byte[235];

					loopRun();
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				dialogPopup("Please use a shorter message.");
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			run = true;
			// Data.dismissLoadingDialog();

		}
	}

	public void textProcess() {
		try {
			FlurryAgent.logEvent("text activated button clicked.");
			// Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
			// Matcher m = p.matcher(et.getText());
			// boolean b = m.find();

			byteArr = new byte[15][];
			byteArrMid = new byte[15][];
			byteArrBot = new byte[15][];

			byte blank[] = new byte[224];

			String temp = et.getText().toString();
			temp = temp.toUpperCase();
			int tempno = 0;
			int midInc = 0, botInc = 0, loopVal = 0;
			String empty = "";
			int tempLength = 0, preLenTop = 0, preLenMid = 0, preLenBot = 0;
			String finalStr = "";

			String[] parts = temp.split("\\s");

			boolean proceed = true;
			for (int z = 0; z < parts.length; z++) {
				// Log.v("parts value", "," + parts[z] + ",");

				if (parts[z].length() > 5) {
					proceed = false;
					break;
				}
			}

			if (!proceed) {
				dialogPopup("Word can't be more than 5 characters.");
			} else {

				// for (int i = 0; i < parts.length; i++) {
				// empty = "";
				// if ((tempLength + parts[i].length()) <= 5) {
				// if ((tempLength + parts[i].length()) != 5) {
				//
				// parts[i] = parts[i] + " ";
				// }
				// tempLength = tempLength + parts[i].length();
				//
				// } else {
				// for (int k = 0; k < (5 - tempLength); k++) {
				// empty = empty + " ";
				// }
				// // if ((tempLength + parts[i].length()) != 5) {
				// // parts[i] = parts[i] + " ";
				// // }
				// parts[i - 1] = parts[i - 1] + empty;
				// tempLength = parts[i].length();
				// }
				// }

				int buffer = 5;
				StringBuilder builder = new StringBuilder();

				for (int i = 0; i < parts.length; i++) {
					if (parts[i].length() <= buffer) {
						builder.append(parts[i]);
						if (parts[i].length() < buffer) {
							buffer -= 1;
							builder.append(" ");
						}
						buffer -= parts[i].length();
					} else {
						for (int j = 0; j < buffer; j++) {
							builder.append(" ");
						}
						buffer = 0;
						i--;
					}
					if (buffer == 0) {
						buffer = 5;
					}
				}

				finalStr = builder.toString();
				// for (int i = 0; i < parts.length; i++) {
				// empty = "";
				//
				// if (parts[i].length() == 4) {
				// for (int k = 0; k < (5 - parts[i].length()); k++) {
				// empty = empty + " ";
				// }
				// parts[i] = parts[i] + empty;
				// }
				//
				// finalStr = finalStr + parts[i];
				// Log.v("parts value",","+parts[i]+",");
				// }
				Log.v("final_str", "." + finalStr + "," + finalStr.length());
				if (finalStr.length() > 15) {
					dialogPopup("You can enter only 15 characters.");
				} else {

					if (finalStr.length() <= 15) {

						for (int i = 0; i < finalStr.length(); i++) {
							tempno = 0;
							for (char c = 'A'; c <= 'Z'; c++) {
								if (finalStr.charAt(i) == c
										|| finalStr.charAt(i) < 'A'
										|| finalStr.charAt(i) > 'Z') {
									try {

										if (i >= 0 && i <= 4) {
											// if (final_str.charAt(i) == ' ')
											// {
											// fields_array[i] = fields[26];
											// byteArr[i] = (byte[])
											// fields_array[i]
											// .get(obj);
											// }
											Log.e("before if char ", ","
													+ finalStr.charAt(i) + ",");
											if (finalStr.charAt(i) < 'A'
													|| finalStr.charAt(i) > 'Z'
													|| finalStr.charAt(i) == ' ') {
												if (finalStr.charAt(i) == ' ') {

													fieldsArrayTop[i] = fields[26];
													byteArr[i] = (byte[]) fieldsArrayTop[i]
															.get(obj);
												} else {

													String position = topspclCharMap.get(finalStr.charAt(i));
													fieldsArrayTop[i] = topSplChar.getField(position);
													byteArr[i] = (byte[]) fieldsArrayTop[i].get(topSplObj);
													Log.e("ASCII value ", ","+ (int) finalStr.charAt(i) + ", " + byteArr[i].length);

													// String position =
													// spclCharMap.get(final_str.charAt(i));
													// fields_array_top[i] =
													// spl_char.getField(position);
													// byteArr[i] = (byte[])
													// fields_array_top[i]
													// .get(spl_obj);
												}
											} else {
												fieldsArrayTop[i] = fieldsTop[tempno];
												byteArr[i] = (byte[]) fieldsArrayTop[i].get(objTop);
											}
											byteArrMid[i] = (byte[]) fields[26].get(obj);
											byteArrBot[i] = (byte[]) fields[26].get(obj);
										}
										if (i > 4 && i <= 9) {
											// if (final_str.charAt(i) == ' ')
											// {
											// fields_array[i] = fields[26];
											// byteArr_mid[a] = (byte[])
											// fields_array[i]
											// .get(obj);
											// }

											if (finalStr.charAt(i) < 'A'
													|| finalStr.charAt(i) > 'Z'
													|| finalStr.charAt(i) == ' ') {
												Log.e("here in special one",
														","
																+ finalStr
																		.charAt(i)
																+ ",");
												if (finalStr.charAt(i) == ' ') {

													fieldsArray[i] = fields[26];
													byteArrMid[midInc] = (byte[]) fieldsArray[i]
															.get(obj);
												} else {
													Log.e("here in special one",
															"here in special one");
													Log.e("ASCII value ",
															","
																	+ (int) finalStr
																			.charAt(i));

													String position = spclCharMap
															.get(finalStr
																	.charAt(i));
													fieldsArray[i] = splChar
															.getField(position);
													byteArrMid[midInc] = (byte[]) fieldsArray[i]
															.get(splObj);
												}
											} else {
												fieldsArray[i] = fields[tempno];
												byteArrMid[midInc] = (byte[]) fieldsArray[i].get(obj);
											}
											byteArr[i] = (byte[]) fields[26].get(obj);
											byteArrBot[i] = (byte[]) fields[26].get(obj);
											midInc++;
										} else if (i > 9) {
											// if (final_str.charAt(i) == ' ')
											// {
											// fields_array[i] = fields[26];
											// byteArr_bot[d] = (byte[])
											// fields_array[i]
											// .get(obj);
											// }
											if (finalStr.charAt(i) < 'A'
													|| finalStr.charAt(i) > 'Z'
													|| finalStr.charAt(i) == ' ') {
												if (finalStr.charAt(i) == ' ') {

													fieldsArrayBot[i] = fields[26];
													byteArrBot[botInc] = (byte[]) fieldsArrayBot[i]
															.get(obj);
												} else {
													Log.e("ASCII value ",
															","
																	+ (int) finalStr
																			.charAt(i));

													String position = botspclCharMap
															.get(finalStr
																	.charAt(i));
													fieldsArrayBot[i] = botSplChar
															.getField(position);
													byteArrBot[botInc] = (byte[]) fieldsArrayBot[i]
															.get(botSplObj);
												}
											} else {
												fieldsArrayBot[i] = fieldsBot[tempno];
												byteArrBot[botInc] = (byte[]) fieldsArrayBot[i]
														.get(objBot);
											}
											byteArr[i] = (byte[]) fields[26]
													.get(obj);
											byteArrMid[i] = (byte[]) fields[26]
													.get(obj);
											botInc++;
										}
									} catch (IllegalAccessException e) {
										e.printStackTrace();
									}
									break;
								}
								// else if (temp.charAt(i) == ' ') {
								// try {
								// fields_array[i] = fields[27];
								// byteArr[i] = (byte[]) fields_array[i]
								// .get(obj);
								//
								// } catch (IllegalAccessException e) {
								// e.printStackTrace();
								// }
								// break;
								// }
								tempno = tempno + 1;
								// Log.v("length of char ",""+tempno);
							}
						}

						// data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04,
						// 0x00,
						// 0x00, 0x00, 0x03, 0x00, (byte) 0xBB };
						// beforeSend1(data, "display:1");

						data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04,
								0x00, 0x00, 0x00, 0x06, 0x00, (byte) 0xBB };
						beforeSend1(data, "mode:flash,looping:1");
						// set last address and looping mode

						data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07,
								0x00, 0x00, 0x00, 0x05, 0x00, (byte) 0x02,
								0x00, 0x01, (byte) 0xBB };

						beforeSend1(data, "last flash address:");

						data = new byte[235];
						if (temp.length() > 5) {
							loopVal = 5;
						} else {
							loopVal = temp.length();
						}

						finalStr = finalStr.trim();
						Log.v("entered value", "," + finalStr.length());
						for (int k = 0; k < finalStr.length(); k++) {

							if (k == 0 || k == 1 || k == 2 || k == 3 || k == 4) {
								Log.v("bytearry value", "," + byteArr[k].length);
								int len = byteArr[k].length;
								if (k != 0) {
									preLenTop = preLenTop + byteArr[k - 1].length;
								}
								Log.v("length of arr top", "," + len + " " + preLenTop);
								for (int i = 0; i < len; i++) {
									blank[preLenTop + i] = (byte) (blank[preLenTop + i] | byteArr[k][i]);
								}
							} else if (k == 5 || k == 6 || k == 7 || k == 8
									|| k == 9) {
								int len = byteArrMid[k - 5].length;
								if (k != 5) {
									preLenMid = preLenMid
											+ byteArrMid[k - 6].length;
								}
								Log.v("length of arr mid", "," + len + " "
										+ preLenMid);
								for (int i = 0; i < len; i++) {
									blank[preLenMid + i] = (byte) (blank[preLenMid
											+ i] | byteArrMid[k - 5][i]);
								}
							} else if (k == 10 || k == 11 || k == 12 || k == 13
									|| k == 14) {
								int len = byteArrBot[k - 10].length;
								if (k != 10) {
									preLenBot = preLenBot
											+ byteArrBot[k - 11].length;
								}
								Log.v("length of arr bot", "," + len + " "
										+ preLenBot);
								for (int i = 0; i < len; i++) {
									blank[preLenBot + i] = (byte) (blank[preLenBot
											+ i] | byteArrBot[k - 10][i]);
								}
							}

							// int j = k * 40;
							// for (int i = 0; i < 40; i++) {
							// data[10 + i + j] = (byte) (byteArr[k][i] |
							// byteArr_mid[k][i] | byteArr_bot[k][i]);
							// }

						}

						for (int i = 0; i < 224; i++) {
							data[10 + i] = blank[i];
						}

						data[0] = (byte) 0xAA;
						data[1] = (byte) 0xAA;
						data[2] = (byte) 0xE6;
						data[3] = (byte) 0x00;
						data[4] = (byte) 0x02;
						data[5] = (byte) 0x00;
						data[6] = (byte) 0x01;
						data[7] = (byte) 0x00;
						data[8] = (byte) 0x02;
						data[9] = (byte) 0x00;
						data[10 + 224] = (byte) 0xBB;

						// lower
						int lowerShift = 0;
						for (int i = 28; i > 1; i--) {
							if (data[10 + (i - 1) * 8 + 1 - 1] == 0x00
									&& data[10 + (i - 1) * 8 + 1 - 1] == 0) {
								// Log.v("in if statement","in if...");
							} else {
								lowerShift = (28 - i) / 2;
								break;
							}
						}
						Log.v("lower shift value = ", "." + lowerShift);

						for (int j = 0; j < lowerShift; j++) {
							for (int i = 28; i > 1; i--) {
								data[10 + (i - 1) * 8 + 1 - 1] = data[10
										+ (i - 2) * 8 + 1 - 1];
								data[10 + (i - 1) * 8 + 2 - 1] = data[10
										+ (i - 2) * 8 + 2 - 1];
							}
							data[10 + 1 - 1] = 0x00;
							data[10 + 2 - 1] = 0x00;
						}

						// middle
						int midShift = 0;
						for (int i = 28; i > 1; i--) {
							if (data[10 + (i - 1) * 8 + 3 - 1] == 0x00
									&& data[10 + (i - 1) * 8 + 4 - 1] == 0x00
									&& data[10 + (i - 1) * 8 + 5 - 1] == 0x00) {
								// Log.v("midShift in if statement","midShift in if...");
							} else {
								midShift = (28 - i) / 2;
								break;
							}
						}
						Log.v("mid shift value = ", "." + midShift);

						for (int j = 0; j < midShift; j++) {
							for (int i = 28; i > 1; i--) {
								data[10 + (i - 1) * 8 + 3 - 1] = data[10
										+ (i - 2) * 8 + 3 - 1];
								data[10 + (i - 1) * 8 + 4 - 1] = data[10
										+ (i - 2) * 8 + 4 - 1];
								data[10 + (i - 1) * 8 + 5 - 1] = data[10
										+ (i - 2) * 8 + 5 - 1];
							}
							data[10 + 3 - 1] = 0x00;
							data[10 + 4 - 1] = 0x00;
							data[10 + 5 - 1] = 0x00;
						}

						// top
						int topShift = 0;
						for (int i = 28; i > 1; i--) {
							if (data[10 + (i - 1) * 8 + 6 - 1] == 0x00
									&& data[10 + (i - 1) * 8 + 7 - 1] == 0x00
									&& data[10 + (i - 1) * 8 + 8 - 1] == 0x00) {
								// Log.v("topShift in if statement","topShift in if...");
							} else {
								topShift = (28 - i) / 2;
								break;
							}
						}
						Log.v("mid shift value = ", "." + topShift);

						for (int j = 0; j < topShift; j++) {
							for (int i = 28; i > 1; i--) {
								data[10 + (i - 1) * 8 + 6 - 1] = data[10
										+ (i - 2) * 8 + 6 - 1];
								data[10 + (i - 1) * 8 + 7 - 1] = data[10
										+ (i - 2) * 8 + 7 - 1];
								data[10 + (i - 1) * 8 + 8 - 1] = data[10
										+ (i - 2) * 8 + 8 - 1];
							}
							data[10 + 6 - 1] = 0x00;
							data[10 + 7 - 1] = 0x00;
							data[10 + 8 - 1] = 0x00;
						}

						int lastEntFram = 2;
						byte[] newData = new byte[2];
						newData[0] = (byte) (lastEntFram & 0xFF);
						newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
						data[8] = newData[0];
						data[9] = newData[1];
						String result = "buf:0," + lastEntFram;
						// Log.v("Last result value",""+result);
						beforeSend1(data, result);
						// set frame rate
						data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06,
								0x00, 0x01, 0x00, 0x01, 0x00, (byte) 0x01,
								0x00, (byte) 0xBB };
						if (!beforeSend1(data, "fps:")) {
							// return false;
						}

						// set last address and looping mode
						data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07,
								0x00, 0x00, 0x00, 0x05, 0x00, (byte) 0x02,
								0x00, 0x01, (byte) 0xBB };

						beforeSend1(data, "last flash address:");
					} else {
						dialogPopup("Please use a shorter message.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			dialogPopup("This string contains I,M,N,Q,R,T,U,V,W,X,Y. They take more space than other characters. It is not possible to be displayed.");
		}
	}

	public void loopRun() {
		// Auto-generated method stub
		data = new byte[235];
		// Log.v("run_length",","+run_length);
		for (int i = 0; i < runLength; i++) {
			for (int t = 0; t < 224; t++) {
				data[10 + t] = blankRuntxt[t + (i * 8)];
				// data[10 + t] = byteArr_mid[i][t];
				if (!run) {
					break;
				}
			}
			if (!run) {
				break;
			}
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0xE6;
			data[3] = (byte) 0x00;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x01;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;
			data[10 + 224] = (byte) 0xBB;

			int lastEntFram = 2;
			byte[] newData = new byte[2];
			newData[0] = (byte) (lastEntFram & 0xFF);
			newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			data[8] = newData[0];
			data[9] = newData[1];
			String result = "buf:0," + lastEntFram;
			// Log.v("Last result value",""+result);
			beforeSend1(data, result);

		}

		if (run) {
			loopRun();
		}
	}

	@Override
	protected void onStart() {
		// Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(this, "69JVCKH5XT2XXQPJH5M5");

	}

	@Override
	protected void onStop() {
		// Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	void getByteArr(int frames, int fps, String className) {
		// load the utils class at runtime
		
//		Log.d("frames","frames = "+frames);
//		Log.d("fps","fps = "+fps);
		
		try {
//			Class cls = Class.forName("com.tshirt_os_utils." + className);
//			Class cls = Class.forName("com.tshirt_os_util.HorseRunning");
			Class cls = Class.forName("com.tshirt_os_util." + className);
			Object obj = cls.newInstance();
			Field fields[] = cls.getDeclaredFields();
			byte[][] byteArr = (byte[][]) fields[0].get(obj);

			boolean result = univProcessData(frames, fps, byteArr);
			while (!result) {
				result = univProcessData(frames, fps, byteArr);
			}
			
//			boolean result = univProcessData(11, 10, byteArr);
//			while (!result) {
//				result = univProcessData(11, 10, byteArr);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void getByteArrGoal(int frames, int fps, String className) {
		// load the utils class at runtime
		Log.v("frames","frames = "+frames);
		Log.v("fps","fps = "+fps);
		
		try {
			Class cls = Class.forName("com.tshirt_os_utils." + className);
			Object obj = cls.newInstance();
			Field fields[] = cls.getDeclaredFields();
			byte[][] byteArr = (byte[][]) fields[0].get(obj);

			boolean result = goalProcessData(frames, fps, byteArr);
			while (!result) {
				result = goalProcessData(frames, fps, byteArr);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		// Auto-generated method stub
		super.onBackPressed();
		run = false;

		finish();
		DataPref.serverOff = false;
		startActivity(new Intent(ImageActivity.this, Splash.class));
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	void getBattery() {
		run = false;
		batteryInfo = true;

		bettaryData = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x03,
				0x00, 0x00, 0x00, (byte) 0xBB };
		beforeSend(bettaryData);

	}

	ImageView img;
	private int pressed = 0;

	/**
	 * Method used for run gif image on tshirt
	 */
	public void selectedState(int view1) {

		pressed = view1;

		// if (img != (ImageView) findViewById(view1)) {
		// if (img != null) {
		// img.setAlpha(1.0f);
		// }
		//
		// img = (ImageView) findViewById(view1);
		// img.setAlpha(0.5f);
		// if (serverSuccess) {
		// Toast.makeText(getApplicationContext(), "serverSuccess",
		// 1000).show();
		// //new AsyncD().execute(view1);
		// } else {
		// Toast.makeText(getApplicationContext(), "serverSuccess false",
		// 1000).show();
		// new AsyncNormal().execute(view1);
		// }
		// } else {
		// img.setAlpha(1.0f);
		// img = null;
		// getByteArr(4, 50, "Blank");
		// }

		if (serverSuccess) {
			Toast.makeText(getApplicationContext(), "serverSuccess", 1000)
					.show();
			// new AsyncD().execute(view1);
		} else {
			if (view1 == 0) {
				getByteArr(4, 50, "Blank");
			} else {
				new AsyncNormal().execute(view1);
			}
		}

	}

	// ***************** Radio mode **************//

	public boolean radioProcessData() {

		// set load from radio
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x07, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "images loaded from radio")) {
			return false;
		}

		// set to run from radio sync messages broadcast
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x00, 0x00,
				0x09, 0x00, (byte) 0x02, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mdoe:radio")) {
			return false;
		}

		// set sync messages
		// data = new byte[]{(byte) 0xAA, (byte) 0xAA, 0x05, 0x00, 0x01, 0x00,
		// 0x03, 0x00, 0x00, (byte) 0xBB};
		// if (!beforeSend1(data, "sync messages:")) {
		// return false;
		// }

		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = images.btn4Img1[i];
			data[236 + i] = images.btn4Img2[i];
			data[462 + i] = images.btn4Img3[i];
			data[688 + i] = images.btn4Img4[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[8];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData[4] = (byte) (entFram + 4 & 0xFF);
		newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData[6] = (byte) (entFram + 6 & 0xFF);
		newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];
		data[460] = newData[4];
		data[461] = newData[5];
		data[686] = newData[6];
		data[687] = newData[7];
		if (!beforeSend1(data, "buf:")) {
			return false;
		}
		if (!beforeSend1(data, "@buf:")) {
			return false;
		}

		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = images.btn4Img5[i];
			data[236 + i] = images.btn4Img6[i];
			data[462 + i] = images.btn4Img7[i];
			data[688 + i] = images.btn4Img8[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 10;
		byte[] newData1 = new byte[8];
		newData1[0] = (byte) (entFram & 0xFF);
		newData1[1] = (byte) ((entFram >> 8) & 0xFF);
		newData1[2] = (byte) (entFram + 2 & 0xFF);
		newData1[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData1[4] = (byte) (entFram + 4 & 0xFF);
		newData1[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData1[6] = (byte) (entFram + 6 & 0xFF);
		newData1[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData1[0];
		data[9] = newData1[1];
		data[234] = newData1[2];
		data[235] = newData1[3];
		data[460] = newData1[4];
		data[461] = newData1[5];
		data[686] = newData1[6];
		data[687] = newData1[7];
		if (!beforeSend1(data, "buf:")) {
			return false;
		}
		if (!beforeSend1(data, "@buf:")) {
			return false;
		}

		// set last address and looping mode
		// data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
		// 0x05, 0x00, (byte) 0x08, 0x00, 0x01, (byte) 0xBB };
		// if (!beforeSend1(data, "last flash address:8")) {
		// return false;
		// }

		return true;
	}

	// ***************** Hi Data ***************//

	public boolean btn2ProcessData() {

		// sendFlush1();

		// set load from flash and enable looping
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mode:flash,looping:1")) {
			return false;
		}

		// set frame rate
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x01, 0x00,
				0x01, 0x00, (byte) 0x04, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "fps:")) {
			return false;
		}

		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = images.btn2Img1[i];
			data[236 + i] = images.btn2Img1[i];
			data[462 + i] = images.btn2Img1[i];
			data[688 + i] = images.btn2Img1[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[8];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData[4] = (byte) (entFram + 4 & 0xFF);
		newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData[6] = (byte) (entFram + 6 & 0xFF);
		newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];
		data[460] = newData[4];
		data[461] = newData[5];
		data[686] = newData[6];
		data[687] = newData[7];
		if (!beforeSend1(data, "buf:0,2,4,6,8,.")) {
			return false;
		}

		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = images.btn2Img1[i];
			data[236 + i] = images.btn2Img2[i];
			data[462 + i] = images.btn2Img3[i];
			data[688 + i] = images.btn2Img4[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 10;
		byte[] newData1 = new byte[8];
		newData1[0] = (byte) (entFram & 0xFF);
		newData1[1] = (byte) ((entFram >> 8) & 0xFF);
		newData1[2] = (byte) (entFram + 2 & 0xFF);
		newData1[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData1[4] = (byte) (entFram + 4 & 0xFF);
		newData1[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData1[6] = (byte) (entFram + 6 & 0xFF);
		newData1[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData1[0];
		data[9] = newData1[1];
		data[234] = newData1[2];
		data[235] = newData1[3];
		data[460] = newData1[4];
		data[461] = newData1[5];
		data[686] = newData1[6];
		data[687] = newData1[7];
		if (!beforeSend1(data, "buf:0,10,12,14,16,.")) {
			return false;
		}

		// Add four frame with 5-8
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x0A;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn2Img5[i];
			data[236 + i] = images.btn2Img6[i];
			data[462 + i] = images.btn2Img7[i];
			data[688 + i] = images.btn2Img7[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 18;
		byte[] newData11 = new byte[8];
		newData11[0] = (byte) (entFram & 0xFF);
		newData11[1] = (byte) ((entFram >> 8) & 0xFF);
		newData11[2] = (byte) (entFram + 2 & 0xFF);
		newData11[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData11[4] = (byte) (entFram + 4 & 0xFF);
		newData11[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData11[6] = (byte) (entFram + 6 & 0xFF);
		newData11[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData11[0];
		data[9] = newData11[1];
		data[234] = newData11[2];
		data[235] = newData11[3];
		data[460] = newData11[4];
		data[461] = newData11[5];
		data[686] = newData11[6];
		data[687] = newData11[7];
		if (!beforeSend1(data, "buf:0,18,20,22,24,.")) {
			return false;
		}

		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = images.btn2Img7[i];
			data[236 + i] = images.btn2Img7[i];
			data[462 + i] = images.btn2Img7[i];
			data[688 + i] = images.btn2Img8[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 26;
		byte[] newData22 = new byte[8];
		newData22[0] = (byte) (entFram & 0xFF);
		newData22[1] = (byte) ((entFram >> 8) & 0xFF);
		newData22[2] = (byte) (entFram + 2 & 0xFF);
		newData22[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData22[4] = (byte) (entFram + 4 & 0xFF);
		newData22[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData22[6] = (byte) (entFram + 6 & 0xFF);
		newData22[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData22[0];
		data[9] = newData22[1];
		data[234] = newData22[2];
		data[235] = newData22[3];
		data[460] = newData22[4];
		data[461] = newData22[5];
		data[686] = newData22[6];
		data[687] = newData22[7];
		if (!beforeSend1(data, "buf:0,26,28,30,32,.")) {
			return false;
		}
		// Add four frame with 9-12
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x12;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn2Img9[i];
			data[236 + i] = images.btn2Img10[i];
			data[462 + i] = images.btn2Img11[i];
			data[688 + i] = images.btn2Img12[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 34;
		byte[] newData2 = new byte[8];
		newData2[0] = (byte) (entFram & 0xFF);
		newData2[1] = (byte) ((entFram >> 8) & 0xFF);
		newData2[2] = (byte) (entFram + 2 & 0xFF);
		newData2[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData2[4] = (byte) (entFram + 4 & 0xFF);
		newData2[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData2[6] = (byte) (entFram + 6 & 0xFF);
		newData2[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData2[0];
		data[9] = newData2[1];
		data[234] = newData2[2];
		data[235] = newData2[3];
		data[460] = newData2[4];
		data[461] = newData2[5];
		data[686] = newData2[6];
		data[687] = newData2[7];
		if (!beforeSend1(data, "buf:0,34,36,38,40,.")) {
			return false;
		}

		// Add four frame with 13-16
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x1A;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn2Img13[i];
			data[236 + i] = images.blank[i];
			data[462 + i] = images.blank[i];
			data[688 + i] = images.blank[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 42;
		byte[] newData3 = new byte[8];
		newData3[0] = (byte) (entFram & 0xFF);
		newData3[1] = (byte) ((entFram >> 8) & 0xFF);
		newData3[2] = (byte) (entFram + 2 & 0xFF);
		newData3[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData3[4] = (byte) (entFram + 4 & 0xFF);
		newData3[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData3[6] = (byte) (entFram + 6 & 0xFF);
		newData3[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData3[0];
		data[9] = newData3[1];
		data[234] = newData3[2];
		data[235] = newData3[3];
		data[460] = newData3[4];
		data[461] = newData3[5];
		data[686] = newData3[6];
		data[687] = newData3[7];
		if (!beforeSend1(data, "buf:0,42,44,46,48,.")) {
			return false;
		}
		// Add four frame with blank
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x52;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.blank[i];
			data[236 + i] = images.blank[i];
			data[462 + i] = images.blank[i];
			data[688 + i] = images.blank[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 50;
		byte[] newData4 = new byte[8];
		newData4[0] = (byte) (entFram & 0xFF);
		newData4[1] = (byte) ((entFram >> 8) & 0xFF);
		newData4[2] = (byte) (entFram + 2 & 0xFF);
		newData4[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData4[4] = (byte) (entFram + 4 & 0xFF);
		newData4[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData4[6] = (byte) (entFram + 6 & 0xFF);
		newData4[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData4[0];
		data[9] = newData4[1];
		data[234] = newData4[2];
		data[235] = newData4[3];
		data[460] = newData4[4];
		data[461] = newData4[5];
		data[686] = newData4[6];
		data[687] = newData4[7];
		if (!beforeSend1(data, "buf:0,50,52,54,56,.")) {
			return false;
		}

		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x38, 0x00, 0x01, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address:56")) {
			return false;
		}

		return true;
	}

	// ****** heart data *************//
	public boolean btn1ProcessData() {

		// set load from flash and enable looping
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mode:flash,looping:1")) {
			return false;
		}

		// Add four frame with 1-3

		data = new byte[687];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0xAA;
		data[3] = (byte) 0x02;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x03;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = images.btn1Img1[i];
			data[236 + i] = images.btn1Img2[i];
			data[462 + i] = images.btn1Img3[i];

		}
		data[10 + 672 + 4] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[6];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData[4] = (byte) (entFram + 4 & 0xFF);
		newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];
		data[460] = newData[4];
		data[461] = newData[5];

		if (!beforeSend1(data, "buf:0,2,4,6,.")) {
			return false;
		}

		// Add four frame with blank
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x52;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.blank[i];
			data[236 + i] = images.blank[i];
			data[462 + i] = images.blank[i];
			data[688 + i] = images.blank[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 8;
		byte[] newData10 = new byte[8];
		newData10[0] = (byte) (entFram & 0xFF);
		newData10[1] = (byte) ((entFram >> 8) & 0xFF);
		newData10[2] = (byte) (entFram + 2 & 0xFF);
		newData10[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData10[4] = (byte) (entFram + 4 & 0xFF);
		newData10[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData10[6] = (byte) (entFram + 6 & 0xFF);
		newData10[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData10[0];
		data[9] = newData10[1];
		data[234] = newData10[2];
		data[235] = newData10[3];
		data[460] = newData10[4];
		data[461] = newData10[5];
		data[686] = newData10[6];
		data[687] = newData10[7];
		if (!beforeSend1(data, "buf:0,8,10,12,14,.")) {
			return false;
		}

		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x0E, 0x00, 0x01, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address:14")) {
			return false;
		}

		// set frame rate
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x01, 0x00,
				0x01, 0x00, (byte) 0x04, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "fps:")) {
			return false;
		}

		return true;
	}

	// ****** Smiley data *************//

	public boolean btn8ProcessData() {

		// set load from flash and enable looping
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mode:flash,looping:1")) {
			return false;
		}

		// set frame rate
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x01, 0x00,
				0x01, 0x00, (byte) 0x08, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "fps:")) {
			return false;
		}

		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x18, 0x00, 0x01, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address:24")) {
			return false;
		}

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x52;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn3Img1[i];
			data[236 + i] = images.btn3Img1[i];
			data[462 + i] = images.btn3Img1[i];
			data[688 + i] = images.btn3Img1[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 2;
		byte[] newData4 = new byte[8];
		newData4[0] = (byte) (entFram & 0xFF);
		newData4[1] = (byte) ((entFram >> 8) & 0xFF);
		newData4[2] = (byte) (entFram + 2 & 0xFF);
		newData4[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData4[4] = (byte) (entFram + 4 & 0xFF);
		newData4[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData4[6] = (byte) (entFram + 6 & 0xFF);
		newData4[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData4[0];
		data[9] = newData4[1];
		data[234] = newData4[2];
		data[235] = newData4[3];
		data[460] = newData4[4];
		data[461] = newData4[5];
		data[686] = newData4[6];
		data[687] = newData4[7];
		if (!beforeSend1(data, "buf:0,2,4,6,8,.")) {
			return false;
		}

		// Add four frame with 13-16
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x1A;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn3Img1[i];
			data[236 + i] = images.btn3Img2[i];
			data[462 + i] = images.btn3Img2[i];
			data[688 + i] = images.btn3Img3[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 10;
		byte[] newData3 = new byte[8];
		newData3[0] = (byte) (entFram & 0xFF);
		newData3[1] = (byte) ((entFram >> 8) & 0xFF);
		newData3[2] = (byte) (entFram + 2 & 0xFF);
		newData3[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData3[4] = (byte) (entFram + 4 & 0xFF);
		newData3[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData3[6] = (byte) (entFram + 6 & 0xFF);
		newData3[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData3[0];
		data[9] = newData3[1];
		data[234] = newData3[2];
		data[235] = newData3[3];
		data[460] = newData3[4];
		data[461] = newData3[5];
		data[686] = newData3[6];
		data[687] = newData3[7];
		if (!beforeSend1(data, "buf:0,10,12,14,16,.")) {
			return false;
		}

		// Add four frame with 13-16
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x1A;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn3Img3[i];
			data[236 + i] = images.btn3Img3[i];
			data[462 + i] = images.btn3Img3[i];
			data[688 + i] = images.btn3Img3[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 18;
		byte[] newData5 = new byte[8];
		newData5[0] = (byte) (entFram & 0xFF);
		newData5[1] = (byte) ((entFram >> 8) & 0xFF);
		newData5[2] = (byte) (entFram + 2 & 0xFF);
		newData5[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData5[4] = (byte) (entFram + 4 & 0xFF);
		newData5[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData5[6] = (byte) (entFram + 6 & 0xFF);
		newData5[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData5[0];
		data[9] = newData5[1];
		data[234] = newData5[2];
		data[235] = newData5[3];
		data[460] = newData5[4];
		data[461] = newData5[5];
		data[686] = newData5[6];
		data[687] = newData5[7];
		if (!beforeSend1(data, "buf:0,18,20,22,24,.")) {
			return false;
		}

		return true;
	}

	// ***************** True Data ***************//

	public boolean btn4ProcessData() {

		// set load from flash and enable looping
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mode:flash,looping:1")) {
			return false;
		}

		// set frame rate
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x01, 0x00,
				0x01, 0x00, (byte) 0x08, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "fps:")) {
			return false;
		}
		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = images.btn4Img4[i];
			data[236 + i] = images.btn4Img4[i];
			data[462 + i] = images.btn4Img4[i];
			data[688 + i] = images.btn4Img4[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[8];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData[4] = (byte) (entFram + 4 & 0xFF);
		newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData[6] = (byte) (entFram + 6 & 0xFF);
		newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];
		data[460] = newData[4];
		data[461] = newData[5];
		data[686] = newData[6];
		data[687] = newData[7];
		if (!beforeSend1(data, "buf:0,2,4,6,8,.")) {
			return false;
		}

		// Add four frame with 5-8
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x0A;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn4Img5[i];
			data[236 + i] = images.btn4Img6[i];
			data[462 + i] = images.btn4Img7[i];
			data[688 + i] = images.btn4Img8[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 10;
		byte[] newData1 = new byte[8];
		newData1[0] = (byte) (entFram & 0xFF);
		newData1[1] = (byte) ((entFram >> 8) & 0xFF);
		newData1[2] = (byte) (entFram + 2 & 0xFF);
		newData1[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData1[4] = (byte) (entFram + 4 & 0xFF);
		newData1[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData1[6] = (byte) (entFram + 6 & 0xFF);
		newData1[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData1[0];
		data[9] = newData1[1];
		data[234] = newData1[2];
		data[235] = newData1[3];
		data[460] = newData1[4];
		data[461] = newData1[5];
		data[686] = newData1[6];
		data[687] = newData1[7];
		if (!beforeSend1(data, "buf:0,10,12,14,16,.")) {
			return false;
		}
		// Add four frame with 5-8
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x0A;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn4Img8[i];
			data[236 + i] = images.btn4Img8[i];
			data[462 + i] = images.btn4Img8[i];
			data[688 + i] = images.btn4Img8[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 18;
		byte[] newData2 = new byte[8];
		newData2[0] = (byte) (entFram & 0xFF);
		newData2[1] = (byte) ((entFram >> 8) & 0xFF);
		newData2[2] = (byte) (entFram + 2 & 0xFF);
		newData2[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData2[4] = (byte) (entFram + 4 & 0xFF);
		newData2[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData2[6] = (byte) (entFram + 6 & 0xFF);
		newData2[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData2[0];
		data[9] = newData2[1];
		data[234] = newData2[2];
		data[235] = newData2[3];
		data[460] = newData2[4];
		data[461] = newData2[5];
		data[686] = newData2[6];
		data[687] = newData2[7];
		if (!beforeSend1(data, "buf:0,18,20,22,24,.")) {
			return false;
		}

		// Add four frame with 5-8
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x0A;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.btn4Img8[i];
			data[236 + i] = images.btn4Img1[i];
			data[462 + i] = images.btn4Img2[i];
			data[688 + i] = images.btn4Img3[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 26;
		byte[] newData3 = new byte[8];
		newData3[0] = (byte) (entFram & 0xFF);
		newData3[1] = (byte) ((entFram >> 8) & 0xFF);
		newData3[2] = (byte) (entFram + 2 & 0xFF);
		newData3[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData3[4] = (byte) (entFram + 4 & 0xFF);
		newData3[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData3[6] = (byte) (entFram + 6 & 0xFF);
		newData3[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData3[0];
		data[9] = newData3[1];
		data[234] = newData3[2];
		data[235] = newData3[3];
		data[460] = newData3[4];
		data[461] = newData3[5];
		data[686] = newData3[6];
		data[687] = newData3[7];
		if (!beforeSend1(data, "buf:0,26,28,30,32,.")) {
			return false;
		}

		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x20, 0x00, 0x01, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address:32")) {
			return false;
		}

		return true;
	}

	// ****** yolo data *************//
	public boolean btn17ProcessData() {

		// set load from flash and enable looping
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mode:flash,looping:1")) {
			return false;
		}

		// set frame rate
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x01, 0x00,
				0x01, 0x00, (byte) 0x18, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "fps:")) {
			return false;
		}

		// Add four frame with 1-2

		data = new byte[461];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0xC8;
		data[3] = (byte) 0x01;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x02;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {
			data[10 + i] = yolo.yoloImg1[i];
			data[236 + i] = yolo.yoloImg2[i];
		}

		data[10 + 448 + 2] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[4];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];

		if (!beforeSend1(data, "buf:0,2,4,.")) {
			return false;
		}

		// Add four frame with blank
		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x52;
		data[9] = (byte) 0x00;
		for (int i = 0; i < 224; i++) {
			data[10 + i] = images.blank[i];
			data[236 + i] = images.blank[i];
			data[462 + i] = images.blank[i];
			data[688 + i] = images.blank[i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 6;
		byte[] newData10 = new byte[8];
		newData10[0] = (byte) (entFram & 0xFF);
		newData10[1] = (byte) ((entFram >> 8) & 0xFF);
		newData10[2] = (byte) (entFram + 2 & 0xFF);
		newData10[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData10[4] = (byte) (entFram + 4 & 0xFF);
		newData10[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData10[6] = (byte) (entFram + 6 & 0xFF);
		newData10[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData10[0];
		data[9] = newData10[1];
		data[234] = newData10[2];
		data[235] = newData10[3];
		data[460] = newData10[4];
		data[461] = newData10[5];
		data[686] = newData10[6];
		data[687] = newData10[7];
		if (!beforeSend1(data, "buf:0,6,8,10,12,.")) {
			return false;
		}

		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x0E, 0x00, 0x01, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address:14")) {
			return false;
		}

		return true;
	}

	// ************** Universal Data **********//

	public boolean univProcessData(int frames, int calFps, byte[][] imgData) {

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mode:flash,looping:1")) {
			return false;
		}

		// set frame rate
		// int cal_fps = 24/10;
		calFps = 100 / calFps;
		if (calFps == 0) {
			calFps = 1;
		}

		byte sendFps = (byte) (24 / calFps);
		Log.v("value of fps", "," + sendFps);
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x01, 0x00,
				0x01, 0x00, (byte) 0x08, 0x00, (byte) 0xBB };
		data[8] = sendFps;
		if (!beforeSend1(data, "fps:")) {
			return false;
		}

		int flashAdd = frames * 2;
		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x50, 0x00, 0x01, (byte) 0xBB };
		data[8] = (byte) flashAdd;
		if (!beforeSend1(data, "last flash address:")) {
			return false;
		}

		// // set load from radio
		// data = new byte[]{(byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
		// 0x07, 0x00, (byte) 0xBB};
		// if (!beforeSend1(data, "images loaded from radio")) {
		// return false;
		// }
		//
		// // set to run from radio sync messages broadcast
		// data = new byte[]{(byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x00, 0x00,
		// 0x09, 0x00, (byte) 0x06, 0x00, (byte) 0xBB};
		// if (!beforeSend1(data, "mdoe:radio")) {
		// return false;
		// }

		// frames = 8;
		int lastEntFram = 0;
		int mod = frames % 4;
		int arrayMod[] = new int[mod];
		for (int k = 0; k < mod; k++) {
			arrayMod[k] = (frames - k);
			Log.v("mod array", "" + arrayMod[k]);
		}

		frames = frames / 4;
		Log.v("no of frames", "" + frames + "," + arrayMod.length);

		for (int j = 0; j < frames; j++) {
			// Add four frame with 1-4

			data = new byte[913];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0x8C;
			data[3] = (byte) 0x03;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x04;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			for (int i = 0; i < 224; i++) {

				data[10 + i] = imgData[j * 4][i];
				data[236 + i] = imgData[j * 4 + 1][i];
				data[462 + i] = imgData[j * 4 + 2][i];
				data[688 + i] = imgData[j * 4 + 3][i];
			}
			data[10 + 896 + 6] = (byte) 0xBB;
			entFram = 2 + (j * 8);
			byte[] newData = new byte[8];
			newData[0] = (byte) (entFram & 0xFF);
			newData[1] = (byte) ((entFram >> 8) & 0xFF);
			newData[2] = (byte) (entFram + 2 & 0xFF);
			newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
			newData[4] = (byte) (entFram + 4 & 0xFF);
			newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
			newData[6] = (byte) (entFram + 6 & 0xFF);
			newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

			data[8] = newData[0];
			data[9] = newData[1];
			data[234] = newData[2];
			data[235] = newData[3];
			data[460] = newData[4];
			data[461] = newData[5];
			data[686] = newData[6];
			data[687] = newData[7];
			String result = "buf:0," + entFram + "," + (entFram + 2) + ","
					+ (entFram + 4) + "," + (entFram + 6);
			lastEntFram = entFram + 8;
			// Log.v("result value",""+result);
			if (!beforeSend1(data, "")) {
				return false;
			}
			// if (!beforeSend1(data, "@buf:")) {
			// return false;
			// }

		}

		if (arrayMod.length == 1) {
			data = new byte[235];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0xE6;
			data[3] = (byte) 0x00;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x01;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			for (int i = 0; i < 224; i++) {
				data[10 + i] = imgData[arrayMod[0] - 1][i];
				data[10 + 224] = (byte) 0xBB;
			}
			if (frames == 0) {
				lastEntFram = 2;
			}
			byte[] newData = new byte[2];
			newData[0] = (byte) (lastEntFram & 0xFF);
			newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			data[8] = newData[0];
			data[9] = newData[1];
			String result = "buf:0," + lastEntFram;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, "")) {
				return false;
			}
			// if (!beforeSend1(data, "@buf:")) {
			// return false;
			// }

		} else if (arrayMod.length == 2) {
			// Add four frame with 1-2

			data = new byte[461];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0xC8;
			data[3] = (byte) 0x01;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x02;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			for (int i = 0; i < 224; i++) {

				data[10 + i] = imgData[arrayMod[0] - 1][i];
				data[236 + i] = imgData[arrayMod[1] - 1][i];
			}
			data[10 + 448 + 2] = (byte) 0xBB;
			// last_ent_fram = 2;
			if (frames == 0) {
				lastEntFram = 2;
			}
			byte[] newData = new byte[4];
			newData[0] = (byte) (lastEntFram & 0xFF);
			newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			newData[2] = (byte) (lastEntFram + 2 & 0xFF);
			newData[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);

			data[8] = newData[0];
			data[9] = newData[1];
			data[234] = newData[2];
			data[235] = newData[3];

			String result = "buf:0," + lastEntFram + "," + (lastEntFram + 2);
			lastEntFram = lastEntFram + 2;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, result)) {
				return false;
			}
		} else if (arrayMod.length == 3) {
			// Add four frame with 1-3

			data = new byte[687];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0xAA;
			data[3] = (byte) 0x02;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x03;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			for (int i = 0; i < 224; i++) {

				data[10 + i] = imgData[arrayMod[0] - 1][i];
				data[236 + i] = imgData[arrayMod[1] - 1][i];
				data[462 + i] = imgData[arrayMod[2] - 1][i];

			}
			data[10 + 672 + 4] = (byte) 0xBB;
			if (frames == 0) {
				lastEntFram = 2;
			}
			byte[] newData = new byte[6];
			newData[0] = (byte) (lastEntFram & 0xFF);
			newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			newData[2] = (byte) (lastEntFram + 2 & 0xFF);
			newData[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);
			newData[4] = (byte) (lastEntFram + 4 & 0xFF);
			newData[5] = (byte) ((lastEntFram + 4 >> 8) & 0xFF);

			data[8] = newData[0];
			data[9] = newData[1];
			data[234] = newData[2];
			data[235] = newData[3];
			data[460] = newData[4];
			data[461] = newData[5];
			String result = "buf:0," + lastEntFram + "," + (lastEntFram + 2)
					+ "," + (lastEntFram + 4);
			lastEntFram = lastEntFram + 4;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, result)) {
				return false;
			}
		}

		if (frames == 0) {
			// Add four frame with blank
			data = new byte[913];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0x8C;
			data[3] = (byte) 0x03;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x04;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x52;
			data[9] = (byte) 0x00;
			for (int i = 0; i < 224; i++) {
				data[10 + i] = images.blank[i];
				data[236 + i] = images.blank[i];
				data[462 + i] = images.blank[i];
				data[688 + i] = images.blank[i];
			}
			data[10 + 896 + 6] = (byte) 0xBB;
			// ent_fram = 8;
			byte[] newData10 = new byte[8];
			newData10[0] = (byte) (lastEntFram & 0xFF);
			newData10[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			newData10[2] = (byte) (lastEntFram + 2 & 0xFF);
			newData10[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);
			newData10[4] = (byte) (lastEntFram + 4 & 0xFF);
			newData10[5] = (byte) ((lastEntFram + 4 >> 8) & 0xFF);
			newData10[6] = (byte) (lastEntFram + 6 & 0xFF);
			newData10[7] = (byte) ((lastEntFram + 6 >> 8) & 0xFF);

			data[8] = newData10[0];
			data[9] = newData10[1];
			data[234] = newData10[2];
			data[235] = newData10[3];
			data[460] = newData10[4];
			data[461] = newData10[5];
			data[686] = newData10[6];
			data[687] = newData10[7];
			String result = "buf:0," + lastEntFram + "," + (lastEntFram + 2)
					+ "," + (lastEntFram + 4) + "," + (lastEntFram + 6);
			lastEntFram = lastEntFram + 6;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, result)) {
				return false;
			}
		}

		// // set last address and looping mode
		// data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
		// 0x05, 0x00, (byte) 0x50, 0x00, 0x01, (byte) 0xBB };
		// data[8] = (byte) last_ent_fram;
		// if (!beforeSend1(data, "last flash address:")) {
		// return false;
		// }

		return true;
	}

	// ************** Goal Universal Data **********//

	public boolean goalProcessData(int frames, int calFps, byte[][] imgData) {

		// set load from radio
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x07, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "images loaded from radio")) {
			return false;
		}

		// set to run from radio sync messages broadcast
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x00, 0x00,
				0x09, 0x00, (byte) 0x06, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mdoe:radio")) {
			return false;
		}

		// frames = 8;
		int lastEntFram = 0;
		int mod = frames % 4;
		int arrayMod[] = new int[mod];
		for (int k = 0; k < mod; k++) {
			arrayMod[k] = (frames - k);
			Log.v("mod array", "" + arrayMod[k]);
		}

		frames = frames / 4;
		Log.v("no of frames", "" + frames + "," + arrayMod.length);

		for (int j = 0; j < frames; j++) {
			// Add four frame with 1-4

			data = new byte[913];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0x8C;
			data[3] = (byte) 0x03;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x04;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			for (int i = 0; i < 224; i++) {

				data[10 + i] = imgData[j * 4][i];
				data[236 + i] = imgData[j * 4 + 1][i];
				data[462 + i] = imgData[j * 4 + 2][i];
				data[688 + i] = imgData[j * 4 + 3][i];
			}
			data[10 + 896 + 6] = (byte) 0xBB;
			entFram = 2 + (j * 8);
			byte[] newData = new byte[8];
			newData[0] = (byte) (entFram & 0xFF);
			newData[1] = (byte) ((entFram >> 8) & 0xFF);
			newData[2] = (byte) (entFram + 2 & 0xFF);
			newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
			newData[4] = (byte) (entFram + 4 & 0xFF);
			newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
			newData[6] = (byte) (entFram + 6 & 0xFF);
			newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

			data[8] = newData[0];
			data[9] = newData[1];
			data[234] = newData[2];
			data[235] = newData[3];
			data[460] = newData[4];
			data[461] = newData[5];
			data[686] = newData[6];
			data[687] = newData[7];
			String result = "buf:0," + entFram + "," + (entFram + 2) + ","
					+ (entFram + 4) + "," + (entFram + 6);
			lastEntFram = entFram + 8;
			// Log.v("result value",""+result);
			if (!beforeSend1(data, "")) {
				return false;
			}
			// if (!beforeSend1(data, "@buf:")) {
			// return false;
			// }

		}

		if (arrayMod.length == 1) {
			data = new byte[235];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0xE6;
			data[3] = (byte) 0x00;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x01;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			for (int i = 0; i < 224; i++) {
				data[10 + i] = imgData[arrayMod[0] - 1][i];
				data[10 + 224] = (byte) 0xBB;
			}
			if (frames == 0) {
				lastEntFram = 2;
			}
			byte[] newData = new byte[2];
			newData[0] = (byte) (lastEntFram & 0xFF);
			newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			data[8] = newData[0];
			data[9] = newData[1];
			String result = "buf:0," + lastEntFram;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, "")) {
				return false;
			}
			// if (!beforeSend1(data, "@buf:")) {
			// return false;
			// }

		} else if (arrayMod.length == 2) {
			// Add four frame with 1-2

			data = new byte[461];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0xC8;
			data[3] = (byte) 0x01;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x02;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			for (int i = 0; i < 224; i++) {

				data[10 + i] = imgData[arrayMod[0] - 1][i];
				data[236 + i] = imgData[arrayMod[1] - 1][i];
			}
			data[10 + 448 + 2] = (byte) 0xBB;
			// last_ent_fram = 2;
			if (frames == 0) {
				lastEntFram = 2;
			}
			byte[] newData = new byte[4];
			newData[0] = (byte) (lastEntFram & 0xFF);
			newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			newData[2] = (byte) (lastEntFram + 2 & 0xFF);
			newData[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);

			data[8] = newData[0];
			data[9] = newData[1];
			data[234] = newData[2];
			data[235] = newData[3];

			String result = "buf:0," + lastEntFram + "," + (lastEntFram + 2);
			lastEntFram = lastEntFram + 2;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, result)) {
				return false;
			}
		} else if (arrayMod.length == 3) {
			// Add four frame with 1-3

			data = new byte[687];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0xAA;
			data[3] = (byte) 0x02;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x03;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			for (int i = 0; i < 224; i++) {

				data[10 + i] = imgData[arrayMod[0] - 1][i];
				data[236 + i] = imgData[arrayMod[1] - 1][i];
				data[462 + i] = imgData[arrayMod[2] - 1][i];

			}
			data[10 + 672 + 4] = (byte) 0xBB;
			if (frames == 0) {
				lastEntFram = 2;
			}
			byte[] newData = new byte[6];
			newData[0] = (byte) (lastEntFram & 0xFF);
			newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			newData[2] = (byte) (lastEntFram + 2 & 0xFF);
			newData[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);
			newData[4] = (byte) (lastEntFram + 4 & 0xFF);
			newData[5] = (byte) ((lastEntFram + 4 >> 8) & 0xFF);

			data[8] = newData[0];
			data[9] = newData[1];
			data[234] = newData[2];
			data[235] = newData[3];
			data[460] = newData[4];
			data[461] = newData[5];
			String result = "buf:0," + lastEntFram + "," + (lastEntFram + 2)
					+ "," + (lastEntFram + 4);
			lastEntFram = lastEntFram + 4;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, result)) {
				return false;
			}
		}

		if (frames == 0) {
			// Add four frame with blank
			data = new byte[913];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0x8C;
			data[3] = (byte) 0x03;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x04;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x52;
			data[9] = (byte) 0x00;
			for (int i = 0; i < 224; i++) {
				data[10 + i] = images.blank[i];
				data[236 + i] = images.blank[i];
				data[462 + i] = images.blank[i];
				data[688 + i] = images.blank[i];
			}
			data[10 + 896 + 6] = (byte) 0xBB;
			// ent_fram = 8;
			byte[] newData10 = new byte[8];
			newData10[0] = (byte) (lastEntFram & 0xFF);
			newData10[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			newData10[2] = (byte) (lastEntFram + 2 & 0xFF);
			newData10[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);
			newData10[4] = (byte) (lastEntFram + 4 & 0xFF);
			newData10[5] = (byte) ((lastEntFram + 4 >> 8) & 0xFF);
			newData10[6] = (byte) (lastEntFram + 6 & 0xFF);
			newData10[7] = (byte) ((lastEntFram + 6 >> 8) & 0xFF);

			data[8] = newData10[0];
			data[9] = newData10[1];
			data[234] = newData10[2];
			data[235] = newData10[3];
			data[460] = newData10[4];
			data[461] = newData10[5];
			data[686] = newData10[6];
			data[687] = newData10[7];
			String result = "buf:0," + lastEntFram + "," + (lastEntFram + 2)
					+ "," + (lastEntFram + 4) + "," + (lastEntFram + 6);
			lastEntFram = lastEntFram + 6;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, result)) {
				return false;
			}
		}

		// // set last address and looping mode
		// data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
		// 0x05, 0x00, (byte) 0x50, 0x00, 0x01, (byte) 0xBB };
		// data[8] = (byte) last_ent_fram;
		// if (!beforeSend1(data, "last flash address:")) {
		// return false;
		// }

		return true;
	}

	boolean beforeSend1(byte[] data, String actualVal) {
		//writeToFile(data);
		try {

			//writeToFile(data);
			
			DataPref.mmOutputStream.write(data);
			if (beginListenForData1(actualVal)) {
				DataPref.mmInputStream = DataPref.mmSocket.getInputStream();
				// Log.v("recieved data", "" + Data.mmInputStream.available());
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
		// character

		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];

		// Thread workerThread = new Thread(new Runnable() {
		// public void run() {
		// // Log.e("hello outer", "demo thread outer");
		while (!stopWorker) {
			// Log.v("hello ", "demo thread");
			try {
				int bytesAvailable = DataPref.mmInputStream.available();
				// Log.i("bytesAvailabe",","+bytesAvailable);
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

							// Log.i("data value", "," + data);

							if (data != null && data.startsWith(actualValue)) {
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

	void beforeSend(byte[] data) {
		try {
			DataPref.mmOutputStream.write(data);
			beginListenForData();
			// Log.v("recieved data",""+Data.mmInputStream.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void beginListenForData() {

		final byte delimiter = 10; // This is the ASCII code for a newline
		// character

		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];

		while (!stopWorker) {
			try {
				int bytesAvailable = DataPref.mmInputStream.available();
				// Log.i("bytesAvailabe",","+bytesAvailable);
				if (bytesAvailable > 0) {
					byte[] packetBytes = new byte[bytesAvailable];
					DataPref.mmInputStream.read(packetBytes);
					for (int i = 0; i < bytesAvailable; i++) {
						byte dataByte = packetBytes[i];
						if (dataByte == delimiter) {
							byte[] encodedBytes = new byte[readBufferPosition];
							System.arraycopy(readBuffer, 0, encodedBytes, 0,
									encodedBytes.length);
							String data = new String(encodedBytes, "US-ASCII");
							readBufferPosition = 0;
							Log.i("data string", "" + data);
							stopWorker = true;
							if (batteryInfo) {
								try {
									batteryInfo = false;
									data = data.substring(4, 7);
									Log.i("Battery info ", ", " + data);
									float bat = Float.parseFloat(data);
									int batInfo = (int) ((bat - 3.7) / 0.5 * 100);
									data = String.valueOf(batInfo);
									data = data + "%";
									DataPref.batteryStatus = data;

									Log.e("Battery status", ","
											+ DataPref.batteryStatus);
									// count.setText(data);
								} catch (Exception e) {
									e.printStackTrace();
									Log.e("error", "error at battery life");
								}
							}
						} else {
							readBuffer[readBufferPosition++] = dataByte;
						}
					}
				}
			} catch (IOException ex) {
				stopWorker = true;
			}
		}
	}

	class AsyncD extends AsyncTask<Integer, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			DataPref.showLoadingDialog(ImageActivity.this, "Loading...");
		}

		@Override
		protected String doInBackground(Integer... params) {

			int caseInt = params[0];

			
			switch (caseInt) {
			case R.id.img1: {
				if (serverSuccess && successImg[0]) {

					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[0]),
							Integer.parseInt(DataPref.min[0]), "0");
					while (!result) {
						result = serverProcessData(9,
								Integer.parseInt(DataPref.min[0]), "0");
					}
				}
				// dialog.dismiss();
				break;
			}
			case R.id.img2: {
				if (serverSuccess && successImg[1]) {

					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[1]),
							Integer.parseInt(DataPref.min[1]), "1");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[1]),
								Integer.parseInt(DataPref.min[1]), "1");
					}
				}
				break;
			}
			case R.id.img3: {
				if (serverSuccess && successImg[2]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[2]),
							Integer.parseInt(DataPref.min[2]), "2");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[2]),
								Integer.parseInt(DataPref.min[2]), "2");
					}
				}
				break;
			}
			case R.id.img4: {
				if (serverSuccess && successImg[3]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[3]),
							Integer.parseInt(DataPref.min[3]), "3");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[3]),
								Integer.parseInt(DataPref.min[3]), "3");
					}
				}
				// else {
				// // getByteArr(56, 10, "Beliebe");
				// }
				break;
			}
			case R.id.img5: {
				if (serverSuccess && successImg[4]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[4]),
							Integer.parseInt(DataPref.min[4]), "4");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[4]),
								Integer.parseInt(DataPref.min[4]), "4");
					}
				}
				break;
			}
			case R.id.img6: {
				if (serverSuccess && successImg[5]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[5]),
							Integer.parseInt(DataPref.min[5]), "5");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[5]),
								Integer.parseInt(DataPref.min[5]), "5");
					}
				}
				break;
			}
			case R.id.img7: {
				if (serverSuccess && successImg[6]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[6]),
							Integer.parseInt(DataPref.min[6]), "6");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[6]),
								Integer.parseInt(DataPref.min[6]), "6");
					}

				}

				break;
			}
			case R.id.img8: {
				if (serverSuccess && successImg[7]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[7]),
							Integer.parseInt(DataPref.min[7]), "7");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[7]),
								Integer.parseInt(DataPref.min[7]), "7");
					}

				}
				break;
			}
			case R.id.img9: {
				if (serverSuccess && successImg[8]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[8]),
							Integer.parseInt(DataPref.min[8]), "8");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[8]),
								Integer.parseInt(DataPref.min[8]), "8");
					}
				}

				break;
			}
			case R.id.img10: {
				if (serverSuccess && successImg[9]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[9]),
							Integer.parseInt(DataPref.min[9]), "9");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[9]),
								Integer.parseInt(DataPref.min[9]), "9");
					}
				}
				break;
			}
			case R.id.img11: {
				if (serverSuccess && successImg[10]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[10]),
							Integer.parseInt(DataPref.min[10]), "10");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[10]),
								Integer.parseInt(DataPref.min[10]), "10");
					}
				}
				break;
			}
			case R.id.img12: {
				if (serverSuccess && successImg[11]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[11]),
							Integer.parseInt(DataPref.min[11]), "11");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[11]),
								Integer.parseInt(DataPref.min[11]), "11");
					}
				}
				break;
			}
			case R.id.img13: {
				if (serverSuccess && successImg[12]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[12]),
							Integer.parseInt(DataPref.min[12]), "12");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[12]),
								Integer.parseInt(DataPref.min[12]), "12");
					}
				}
				break;
			}
			case R.id.img14: {
				if (serverSuccess && successImg[13]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[13]),
							Integer.parseInt(DataPref.min[13]), "13");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[13]),
								Integer.parseInt(DataPref.min[13]), "13");
					}
				}
				break;
			}
			case R.id.img15: {
				if (serverSuccess && successImg[14]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[14]),
							Integer.parseInt(DataPref.min[14]), "14");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[14]),
								Integer.parseInt(DataPref.min[14]), "14");
					}
				}
				break;
			}
			case R.id.img16: {
				if (serverSuccess && successImg[15]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[15]),
							Integer.parseInt(DataPref.min[15]), "15");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[15]),
								Integer.parseInt(DataPref.min[15]), "15");
					}
				}
				break;
			}
			case R.id.img17: {
				if (serverSuccess && successImg[16]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[16]),
							Integer.parseInt(DataPref.min[16]), "16");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[16]),
								Integer.parseInt(DataPref.min[16]), "16");
					}

				}

				break;
			}
			case R.id.img18: {
				if (serverSuccess && successImg[17]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[17]),
							Integer.parseInt(DataPref.min[17]), "17");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[17]),
								Integer.parseInt(DataPref.min[17]), "17");
					}
				}
				break;
			}
			case R.id.img19: {
				if (serverSuccess && successImg[18]) {
					boolean result = serverProcessData(
							Integer.parseInt(DataPref.size[18]),
							Integer.parseInt(DataPref.min[18]), "18");
					while (!result) {
						result = serverProcessData(
								Integer.parseInt(DataPref.size[18]),
								Integer.parseInt(DataPref.min[18]), "18");
					}
				}
				break;
			}
			default: {
				boolean result = serverProcessData(
						Integer.parseInt(DataPref.size[caseInt]),
						Integer.parseInt(DataPref.min[caseInt]), "" + caseInt);
				while (!result) {
					result = serverProcessData(
							Integer.parseInt(DataPref.size[caseInt]),
							Integer.parseInt(DataPref.min[caseInt]), ""
									+ caseInt);
				}
				break;
			}

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			DataPref.dismissLoadingDialog();

		}
	}

	// ************ API for download the images. *************//

	public void showData() {

		// dialog1 = ProgressDialog.show(ImageActivity.this, "", "Loading... ",
		// true);
		if (DataPref.serverOff) {
			DataPref.showLoadingDialog(ImageActivity.this, "Loading...");
		}
		Log.v("url = ", "." + url);
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(120*1000);
		client.get(url + "apifiles/index.php?action=get_bytes_array",
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(
							int statusCode, Header[] headers, byte[] responseBody) {

						// TODO Auto-generated method stub
						String response = new String(responseBody);

						// Log.i("request succesfull", "response = " +
						// response);
						JSONObject res = null;
						try {
							res = new JSONObject(response);
							Log.v("res", "res = " + res.toString());
							JSONArray jsonArr = res.getJSONArray("data");

							Log.v("Length..", ", " + jsonArr.length());

							DataPref.url = new String[jsonArr.length()];
							DataPref.name = new String[jsonArr.length()];
							DataPref.min = new String[jsonArr.length()];
							DataPref.size = new String[jsonArr.length()];
							dataMap = new HashMap<String, byte[]>();
							successImg = new boolean[19];
							// removeImages();

							for (int a = 0; a < jsonArr.length(); a++) {
								try {
									DataPref.url[a] = jsonArr.getJSONObject(a)
											.getString("photo_url");
									DataPref.name[a] = jsonArr.getJSONObject(a)
											.getString("gif_name");
									DataPref.min[a] = jsonArr.getJSONObject(a)
											.getString("min");
									DataPref.size[a] = jsonArr.getJSONObject(a)
											.getString("size");

									String gifData = jsonArr.getJSONObject(a)
											.getString("gif_data");
									Log.v("values",
											",  length" + gifData.length());

									gifData = gifData.substring(1,
											(gifData.length() - 1));
									// Log.v("values", ", " + gif_data);

									int byteIndex = -1;
									int index = -1;
									byte[] bytes = null;
									int num = 0;
									for (int b = 0; b < gifData.length(); b++) {
										char charctr = gifData.charAt(b);
										if (charctr == '[') {
											bytes = new byte[224];
											byteIndex = -1;
											index++;
										} else if (charctr == ']') {
											b++;
											dataMap.put("imgData_" + a + "_"
													+ index, bytes);
											writeToFile1(bytes);

											Log.v(index + "", " = " + bytes);
										} else if (charctr == ',') {
											bytes[++byteIndex] = (byte) num;
											num = 0;
										} else {
											num = num * 10 + charctr - '0';
										}
									}

									save1("file"+a);
									successImg[a] = true;
									serverSuccess = true;
									imageProcess(a);

								} catch (JSONException e) {
									e.printStackTrace();

								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							// parLayout.setVisibility(View.GONE);
							gridView.setVisibility(View.VISIBLE);

							// supported.setMinimumHeight(200 + (int) ((div *
							// 211) * ASSL.Yscale()));v
							// supported.setMinimumHeight((int) (800 *
							// ASSL.Yscale()));
							// container.setMinimumHeight(minHeight)
							// Instance of ImageAdapter Class

							gridView.setAdapter(new ImageAdapter(
									ImageActivity.this));

							try {
								ListAdapter listAdapterTheir = gridView
										.getAdapter();
								int totalHeight = 0;
								Log.v("list length = ",
										listAdapterTheir.getCount() + ",");

								int total = listAdapterTheir.getCount() / 3;
								if (listAdapterTheir.getCount() % 3 != 0) {
									total = total + 1;
								}

								for (int i = 0; i < total; i++) {
									View listItem = listAdapterTheir.getView(i,
											null, gridView);
									listItem.measure(0, 0);
									totalHeight += listItem.getMeasuredHeight();
									ViewGroup.LayoutParams params = gridView
											.getLayoutParams();
									params.height = (int) (totalHeight + ((((int) DataPref.url.length * ASSL
											.Yscale()) - 1)));
									gridView.setLayoutParams(params);
									gridView.requestLayout();
								}
							} catch (Exception e) {
								Log.v("error in expand ", e.toString());
							}

						} catch (JSONException e) {
							e.printStackTrace();
							serverSuccess = false;
							DataPref.serverOff = false;
							serverOnOff
									.setBackgroundResource(R.drawable.off_btn);
						} catch (Exception e) {
							e.printStackTrace();
							serverSuccess = false;
							DataPref.serverOff = false;
							serverOnOff
									.setBackgroundResource(R.drawable.off_btn);
						}
						// if (dialog1 != null)
						// dialog1.cancel();
						DataPref.dismissLoadingDialog();
					}

					@Override
					public void onFailure(
							int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),
								"Some server error occured.", Toast.LENGTH_LONG)
								.show();
						DataPref.dismissLoadingDialog();
						serverSuccess = false;
						DataPref.serverOff = false;
						serverOnOff.setBackgroundResource(R.drawable.off_btn);
						processNormalImages();
					}
				});

	}
	
	StringBuilder sb = new StringBuilder();
	
	public void writeToFile1(byte[] array) 
	{ 
		Log.d("88*****","***********************");
	    try 
	    { 
	        
	        
	        sb.append("{");
	        for (byte b : array) {
	        	sb.append("0x");
	            sb.append(String.format("%02X ", b));
	            sb.append(",");
	        }
	        sb.append("},");
	        sb.append("=");
	        
	        //Log.d("Gif data", sb.toString());
	        
//	        String path = Environment.getExternalStorageDirectory()+"/BirdFlying.txt"; 
//	        File file = new File(path);
//	        if (!file.exists()) {
//	          try {
//				file.createNewFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        }
//	        FileOutputStream stream = new FileOutputStream(path); 
//	        PrintWriter pw = new PrintWriter(stream);
//	        pw.print(sb.toString()); 
//	        pw.flush();
//	        pw.close();
	        
	    } catch (Exception e1) 
	    { 
	        e1.printStackTrace(); 
	    } 
	    
	} 
	
	
	

	public void imageProcess(int view1) throws IOException {

		// switch (view1) {
		// case 0: {
		// imLoader.displayImage(DataPref.url[0], img1);
		// break;
		// }
		// case 1: {
		// imLoader.displayImage(DataPref.url[1], img2);
		// break;
		// }
		// case 2: {
		// imLoader.displayImage(DataPref.url[2], img3);
		// break;
		// }
		// case 3: {
		// imLoader.displayImage(DataPref.url[3], img4);
		// break;
		// }
		// case 4: {
		// imLoader.displayImage(DataPref.url[4], img5);
		// break;
		// }
		// case 5: {
		// imLoader.displayImage(DataPref.url[5], img6);
		// break;
		// }
		// case 6: {
		// imLoader.displayImage(DataPref.url[6], img7);
		// break;
		// }
		// case 7: {
		// imLoader.displayImage(DataPref.url[7], img8);
		// break;
		// }
		// case 8: {
		// imLoader.displayImage(DataPref.url[8], img9);
		// break;
		// }
		// case 9: {
		// imLoader.displayImage(DataPref.url[9], img10);
		// break;
		// }
		// case 10: {
		// imLoader.displayImage(DataPref.url[10], img11);
		// break;
		// }
		// case 11: {
		// imLoader.displayImage(DataPref.url[11], img12);
		// break;
		// }
		// case 12: {
		// imLoader.displayImage(DataPref.url[12], img13);
		// break;
		// }
		// case 13: {
		// imLoader.displayImage(DataPref.url[13], img14);
		// break;
		// }
		// case 14: {
		// imLoader.displayImage(DataPref.url[14], img15);
		// break;
		// }
		// case 15: {
		// imLoader.displayImage(DataPref.url[15], img16);
		// break;
		// }
		// case 16: {
		// imLoader.displayImage(DataPref.url[16], img17);
		// break;
		// }
		// case 17: {
		// imLoader.displayImage(DataPref.url[17], img18);
		// break;
		// }
		// case 18: {
		// imLoader.displayImage(DataPref.url[18], img19);
		// break;
		// }
		// default:
		// break;
		// }
	}

	// ************** server Data **********//

	public boolean serverProcessData(int frames, int calFps, String name) {

		Log.e("name", "" + DataPref.name[Integer.parseInt(name)]);
		FlurryAgent.logEvent("server gif selected " + DataPref.name[Integer.parseInt(name)]);

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00, 0x06, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "mode:flash,looping:1")) {
			return false;
		}

		// frames = 9;
		// set frame rate
		// int cal_fps = 24/10;
		calFps = 100 / calFps;
		if (calFps == 0) {
			calFps = 1;
		}
		float finalFps = (24f / calFps);
		calFps = Math.round(finalFps);
		byte sendFps = (byte) (calFps);
		Log.v("fps at server img", "," + sendFps);
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x01, 0x00, 0x01, 0x00, (byte) 0x08, 0x00, (byte) 0xBB };
		data[8] = sendFps;
		if (!beforeSend1(data, "fps:")) {
			return false;
		}

		int flashAdd = frames * 2;
		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00, 0x05, 0x00, (byte) 0x50, 0x00, 0x01, (byte) 0xBB };
		data[8] = (byte) flashAdd;
		if (!beforeSend1(data, "last flash address:")) {
			return false;
		}

		int lastEntFram = 0;
		int mod = frames % 4;
		int arrayMod[] = new int[mod];
		for (int k = 0; k < mod; k++) {
			arrayMod[k] = (frames - k);
			Log.v("mod array", "" + arrayMod[k]);
		}
		Log.v("frames before 4 ", "," + frames);
		if (frames == 1) {
			data = new byte[235];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0xE6;
			data[3] = (byte) 0x00;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x01;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x02;
			data[9] = (byte) 0x00;

			// data[10 + i] = img_data[array_mod[0] - 1][i];
			for (int i = 0; i < 224; i++) {
				data[10 + i] = dataMap.get("imgData_" + name + "_" + (0))[i];
				data[10 + 224] = (byte) 0xBB;
			}
			if (frames == 1) {
				lastEntFram = 2;
			}
			byte[] newData = new byte[2];
			newData[0] = (byte) (lastEntFram & 0xFF);
			newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			data[8] = newData[0];
			data[9] = newData[1];
			String result = "buf:0," + lastEntFram;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, result)) {
				return false;
			}
		} else {
			frames = frames / 4;
			Log.v("no of frames", "" + frames + "," + arrayMod.length);
			for (int j = 0; j < frames; j++) {
				// Add four frame with 1-4

				data = new byte[913];
				data[0] = (byte) 0xAA;
				data[1] = (byte) 0xAA;
				data[2] = (byte) 0x8C;
				data[3] = (byte) 0x03;
				data[4] = (byte) 0x02;
				data[5] = (byte) 0x00;
				data[6] = (byte) 0x04;
				data[7] = (byte) 0x00;
				data[8] = (byte) 0x02;
				data[9] = (byte) 0x00;

				for (int i = 0; i < 224; i++) {

					data[10 + i] = dataMap.get("imgData_" + name + "_" + (j * 4))[i];
					data[236 + i] = dataMap.get("imgData_" + name + "_" + ((j * 4) + 1))[i];
					data[462 + i] = dataMap.get("imgData_" + name + "_" + ((j * 4) + 2))[i];
					data[688 + i] = dataMap.get("imgData_" + name + "_" + ((j * 4) + 3))[i];
				}
				data[10 + 896 + 6] = (byte) 0xBB;
				entFram = 2 + (j * 8);
				byte[] newData = new byte[8];
				newData[0] = (byte) (entFram & 0xFF);
				newData[1] = (byte) ((entFram >> 8) & 0xFF);
				newData[2] = (byte) (entFram + 2 & 0xFF);
				newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
				newData[4] = (byte) (entFram + 4 & 0xFF);
				newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
				newData[6] = (byte) (entFram + 6 & 0xFF);
				newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

				data[8] = newData[0];
				data[9] = newData[1];
				data[234] = newData[2];
				data[235] = newData[3];
				data[460] = newData[4];
				data[461] = newData[5];
				data[686] = newData[6];
				data[687] = newData[7];
				String result = "buf:0," + entFram + "," + (entFram + 2) + ","
						+ (entFram + 4) + "," + (entFram + 6);
				lastEntFram = entFram + 8;
				// Log.v("result value",""+result);
				
				
				if (!beforeSend1(data, result)) {
					return false;
				}
			}

			if (arrayMod.length == 1) {
				data = new byte[235];
				data[0] = (byte) 0xAA;
				data[1] = (byte) 0xAA;
				data[2] = (byte) 0xE6;
				data[3] = (byte) 0x00;
				data[4] = (byte) 0x02;
				data[5] = (byte) 0x00;
				data[6] = (byte) 0x01;
				data[7] = (byte) 0x00;
				data[8] = (byte) 0x02;
				data[9] = (byte) 0x00;

				// data[10 + i] = img_data[array_mod[0] - 1][i];
				for (int i = 0; i < 224; i++) {
					data[10 + i] = dataMap.get("imgData_" + name + "_"
							+ (arrayMod[0] - 1))[i];
					data[10 + 224] = (byte) 0xBB;
				}
				if (frames == 0) {
					lastEntFram = 2;
				}
				byte[] newData = new byte[2];
				newData[0] = (byte) (lastEntFram & 0xFF);
				newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
				data[8] = newData[0];
				data[9] = newData[1];
				String result = "buf:0," + lastEntFram;
				// Log.v("Last result value",""+result);
				if (!beforeSend1(data, result)) {
					return false;
				}

			} else if (arrayMod.length == 2) {
				// Add four frame with 1-2

				data = new byte[461];
				data[0] = (byte) 0xAA;
				data[1] = (byte) 0xAA;
				data[2] = (byte) 0xC8;
				data[3] = (byte) 0x01;
				data[4] = (byte) 0x02;
				data[5] = (byte) 0x00;
				data[6] = (byte) 0x02;
				data[7] = (byte) 0x00;
				data[8] = (byte) 0x02;
				data[9] = (byte) 0x00;

				for (int i = 0; i < 224; i++) {

					data[10 + i] = dataMap.get("imgData_" + name + "_"
							+ (arrayMod[0] - 1))[i];
					data[236 + i] = dataMap.get("imgData_" + name + "_"
							+ (arrayMod[1] - 1))[i];
				}
				data[10 + 448 + 2] = (byte) 0xBB;
				// last_ent_fram = 2;
				if (frames == 0) {
					lastEntFram = 2;
				}
				byte[] newData = new byte[4];
				newData[0] = (byte) (lastEntFram & 0xFF);
				newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
				newData[2] = (byte) (lastEntFram + 2 & 0xFF);
				newData[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);

				data[8] = newData[0];
				data[9] = newData[1];
				data[234] = newData[2];
				data[235] = newData[3];

				String result = "buf:0," + lastEntFram + ","
						+ (lastEntFram + 2);
				lastEntFram = lastEntFram + 2;
				// Log.v("Last result value",""+result);
				if (!beforeSend1(data, result)) {
					return false;
				}
			} else if (arrayMod.length == 3) {
				// Add four frame with 1-3

				data = new byte[687];
				data[0] = (byte) 0xAA;
				data[1] = (byte) 0xAA;
				data[2] = (byte) 0xAA;
				data[3] = (byte) 0x02;
				data[4] = (byte) 0x02;
				data[5] = (byte) 0x00;
				data[6] = (byte) 0x03;
				data[7] = (byte) 0x00;
				data[8] = (byte) 0x02;
				data[9] = (byte) 0x00;

				for (int i = 0; i < 224; i++) {

					data[10 + i] = dataMap.get("imgData_" + name + "_"
							+ (arrayMod[0] - 1))[i];
					data[236 + i] = dataMap.get("imgData_" + name + "_"
							+ (arrayMod[1] - 1))[i];
					data[462 + i] = dataMap.get("imgData_" + name + "_"
							+ (arrayMod[2] - 1))[i];

				}
				data[10 + 672 + 4] = (byte) 0xBB;
				// ent_fram = 2;
				if (frames == 0) {
					lastEntFram = 2;
				}
				byte[] newData = new byte[6];
				newData[0] = (byte) (lastEntFram & 0xFF);
				newData[1] = (byte) ((lastEntFram >> 8) & 0xFF);
				newData[2] = (byte) (lastEntFram + 2 & 0xFF);
				newData[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);
				newData[4] = (byte) (lastEntFram + 4 & 0xFF);
				newData[5] = (byte) ((lastEntFram + 4 >> 8) & 0xFF);

				data[8] = newData[0];
				data[9] = newData[1];
				data[234] = newData[2];
				data[235] = newData[3];
				data[460] = newData[4];
				data[461] = newData[5];
				String result = "buf:0," + lastEntFram + ","
						+ (lastEntFram + 2) + "," + (lastEntFram + 4);
				lastEntFram = lastEntFram + 4;
				// Log.v("Last result value",""+result);
				if (!beforeSend1(data, result)) {
					return false;
				}
			}
		}
		
		
		

		if (frames == 0) {
			// Add four frame with blank
			data = new byte[913];
			data[0] = (byte) 0xAA;
			data[1] = (byte) 0xAA;
			data[2] = (byte) 0x8C;
			data[3] = (byte) 0x03;
			data[4] = (byte) 0x02;
			data[5] = (byte) 0x00;
			data[6] = (byte) 0x04;
			data[7] = (byte) 0x00;
			data[8] = (byte) 0x52;
			data[9] = (byte) 0x00;
			for (int i = 0; i < 224; i++) {
				data[10 + i] = images.blank[i];
				data[236 + i] = images.blank[i];
				data[462 + i] = images.blank[i];
				data[688 + i] = images.blank[i];
			}
			data[10 + 896 + 6] = (byte) 0xBB;
			// ent_fram = 8;
			byte[] newData10 = new byte[8];
			newData10[0] = (byte) (lastEntFram & 0xFF);
			newData10[1] = (byte) ((lastEntFram >> 8) & 0xFF);
			newData10[2] = (byte) (lastEntFram + 2 & 0xFF);
			newData10[3] = (byte) ((lastEntFram + 2 >> 8) & 0xFF);
			newData10[4] = (byte) (lastEntFram + 4 & 0xFF);
			newData10[5] = (byte) ((lastEntFram + 4 >> 8) & 0xFF);
			newData10[6] = (byte) (lastEntFram + 6 & 0xFF);
			newData10[7] = (byte) ((lastEntFram + 6 >> 8) & 0xFF);

			data[8] = newData10[0];
			data[9] = newData10[1];
			data[234] = newData10[2];
			data[235] = newData10[3];
			data[460] = newData10[4];
			data[461] = newData10[5];
			data[686] = newData10[6];
			data[687] = newData10[7];
			String result = "buf:0," + lastEntFram + "," + (lastEntFram + 2)
					+ "," + (lastEntFram + 4) + "," + (lastEntFram + 6);
			lastEntFram = lastEntFram + 6;
			// Log.v("Last result value",""+result);
			if (!beforeSend1(data, result)) {
				return false;
			}
			
			
		}
		// // set last address and looping mode
		// data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
		// 0x05, 0x00, (byte) 0x50, 0x00, 0x01, (byte) 0xBB };
		// data[8] = (byte) last_ent_fram;
		// if (!beforeSend1(data, "last flash address:")) {
		// return false;
		// }

		return true;
	}
	
	
	public void save1(String filename) {
		String path = Environment.getExternalStorageDirectory()+"/Hike/"+filename+".txt"; 
        File file = new File(path);
        if (!file.exists()) {
          try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        FileOutputStream stream;
		try {
			stream = new FileOutputStream(path);
		
        PrintWriter pw = new PrintWriter(stream);
        pw.print(sb.toString()); 
        pw.flush();
        pw.close();
        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		sb = new StringBuilder();
	}

	// for Blank frams..

	public void sendBlank() {

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		beforeSend1(data, "mode:flash,looping:1");

		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = Blank.blankImg[1][i];
			data[236 + i] = Blank.blankImg[2][i];
			data[462 + i] = Blank.blankImg[3][i];
			data[688 + i] = Blank.blankImg[4][i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[8];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData[4] = (byte) (entFram + 4 & 0xFF);
		newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData[6] = (byte) (entFram + 6 & 0xFF);
		newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];
		data[460] = newData[4];
		data[461] = newData[5];
		data[686] = newData[6];
		data[687] = newData[7];
		String result = "buf:0," + entFram + "," + (entFram + 2) + ","
				+ (entFram + 4) + "," + (entFram + 6);
		// Log.v("result value",""+result);
		beforeSend1(data, "");
		// if (!beforeSend1(data, "@buf:")) {
		// return false;
		// }

	}

	public void sendFlush() {

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		beforeSend1(data, "mode:flash,looping:1");

		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x08, 0x00, 0x01, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address:8")) {
			// return false;
		}

		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = Blank.blankImg[1][i];
			data[236 + i] = Blank.blankImg[2][i];
			data[462 + i] = Blank.blankImg[3][i];
			data[688 + i] = Blank.blankImg[4][i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[8];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData[4] = (byte) (entFram + 4 & 0xFF);
		newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData[6] = (byte) (entFram + 6 & 0xFF);
		newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];
		data[460] = newData[4];
		data[461] = newData[5];
		data[686] = newData[6];
		data[687] = newData[7];
		String result = "buf:0," + entFram + "," + (entFram + 2) + ","
				+ (entFram + 4) + "," + (entFram + 6);
		Log.v("Send Flush", "" + result);
		beforeSend1(data, "");
		// if (!beforeSend1(data, "@buf:")) {
		// return false;
		// }
	}

	public void sendFlush1() {
		Log.v("code run", "sendFlush1 run");
		// set last address and looping mode

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x04, 0x00, 0x00, 0x00,
				0x06, 0x00, (byte) 0xBB };
		beforeSend1(data, "mode:flash,looping:1");

		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x08, 0x00, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address:8")) {
			// return false;
		}

		// set frame rate
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x06, 0x00, 0x01, 0x00,
				0x01, 0x00, (byte) 0x18, 0x00, (byte) 0xBB };
		if (!beforeSend1(data, "fps:")) {
			// return false;
		}
		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = Blank.blankImg[1][i];
			data[236 + i] = Blank.blankImg[2][i];
			data[462 + i] = Blank.blankImg[3][i];
			data[688 + i] = Blank.blankImg[4][i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[8];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData[4] = (byte) (entFram + 4 & 0xFF);
		newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData[6] = (byte) (entFram + 6 & 0xFF);
		newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];
		data[460] = newData[4];
		data[461] = newData[5];
		data[686] = newData[6];
		data[687] = newData[7];
		String result = "buf:0," + entFram + "," + (entFram + 2) + ","
				+ (entFram + 4) + "," + (entFram + 6);
		Log.v("Send Flush", "" + result);
		beforeSend1(data, "");
		// if (!beforeSend1(data, "@buf:")) {
		// return false;
		// }

		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x08, 0x00, 0x01, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address  :8")) {
			// return false;
		}

	}

	public void sendFlush2() {
		Log.v("code run", "sendFlush1 run");
		// set last address and looping mode

		// Add four frame with 1-4

		data = new byte[913];
		data[0] = (byte) 0xAA;
		data[1] = (byte) 0xAA;
		data[2] = (byte) 0x8C;
		data[3] = (byte) 0x03;
		data[4] = (byte) 0x02;
		data[5] = (byte) 0x00;
		data[6] = (byte) 0x04;
		data[7] = (byte) 0x00;
		data[8] = (byte) 0x02;
		data[9] = (byte) 0x00;

		for (int i = 0; i < 224; i++) {

			data[10 + i] = Blank.blankImg[1][i];
			data[236 + i] = Blank.blankImg[2][i];
			data[462 + i] = Blank.blankImg[3][i];
			data[688 + i] = Blank.blankImg[4][i];
		}
		data[10 + 896 + 6] = (byte) 0xBB;
		entFram = 2;
		byte[] newData = new byte[8];
		newData[0] = (byte) (entFram & 0xFF);
		newData[1] = (byte) ((entFram >> 8) & 0xFF);
		newData[2] = (byte) (entFram + 2 & 0xFF);
		newData[3] = (byte) ((entFram + 2 >> 8) & 0xFF);
		newData[4] = (byte) (entFram + 4 & 0xFF);
		newData[5] = (byte) ((entFram + 4 >> 8) & 0xFF);
		newData[6] = (byte) (entFram + 6 & 0xFF);
		newData[7] = (byte) ((entFram + 6 >> 8) & 0xFF);

		data[8] = newData[0];
		data[9] = newData[1];
		data[234] = newData[2];
		data[235] = newData[3];
		data[460] = newData[4];
		data[461] = newData[5];
		data[686] = newData[6];
		data[687] = newData[7];
		String result = "buf:0," + entFram + "," + (entFram + 2) + ","
				+ (entFram + 4) + "," + (entFram + 6);
		Log.v("Send Flush", "" + result);
		beforeSend1(data, "");
		// if (!beforeSend1(data, "@buf:")) {
		// return false;
		// }

		// set last address and looping mode
		data = new byte[] { (byte) 0xAA, (byte) 0xAA, 0x07, 0x00, 0x00, 0x00,
				0x05, 0x00, (byte) 0x02, 0x00, 0x01, (byte) 0xBB };
		if (!beforeSend1(data, "last flash address  :2")) {
			// return false;
		}

	}

	void serverPopup() { // dialog for entering password

		final Dialog dialog = new Dialog(ImageActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.server_popup);
		WindowManager.LayoutParams layParam = dialog.getWindow()
				.getAttributes();
		layParam.dimAmount = 0.7f;
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		FrameLayout framLay = (FrameLayout) dialog.findViewById(R.id.rv);
		radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup1);
		Button okBtn = (Button) dialog.findViewById(R.id.button1);
		Button cancel = (Button) dialog.findViewById(R.id.button2);
		new ASSL(this, framLay, 1134, 720, false);

		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				int selectedId = radioGroup.getCheckedRadioButtonId();
				radioBtn = (RadioButton) dialog.findViewById(selectedId);
				if (radioBtn.getText().equals("Production")) {
					url = "http://192.241.199.47/aop/admin_prod/";
				} else {
					url = "http://192.241.199.47/aop/admin_dev/";
				}

				Log.i("radio btn value", "." + radioBtn.getText());
				dialog.dismiss();
				// Toast.makeText(getApplicationContext(), radiobtn.getText(),
				// 500).show();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view1) {
				dialog.dismiss();
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});

		dialog.show();
	}

	/**
	 * Displays default android dialog for displaying alert message to user
	 * 
	 * @param message
	 *            string
	 */
	void dialogPopup(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ImageActivity.this);
		builder.setMessage("" + message).setTitle("Error");
		// builder.setIcon(R.drawable.tobuy_icon);
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alertDialog = builder.create();
		// alertDialog.getWindow().getAttributes().windowAnimations =
		// R.style.Animations_SmileWindow;
		alertDialog.show();
	}

	// *******************

	class AsyncNormal extends AsyncTask<Integer, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			DataPref.showLoadingDialog(ImageActivity.this, "Loading...");
		}

		@Override
		protected String doInBackground(Integer... params) {

			int position = params[0];
//			caseInt += 1;
			String name = items.get(position).name;
			int frame = items.get(position).frame;
			int fps = items.get(position).fps;
			getByteArr(frame, fps, name);
			
//			switch (caseInt) {
//			case 1: {
//				Log.i("hiiii.. ", "hi press");
//				boolean result = btn2ProcessData();
//				while (!result) {
//					result = btn2ProcessData();
//				}
//				// boolean result = radio_processData();
//				// while (!result) {
//				// result = radio_processData();
//				// }
//				break;
//			}
//			case 2: {
//				boolean result = btn4ProcessData();
//				while (!result) {
//					result = btn4ProcessData();
//				}
//				break;
//			}
//			case 3: {
//				getByteArr(37, 50, "LogoGif");
//				break;
//			}
//			case 4: {
//				getByteArr(56, 10, "Beliebe");
//				break;
//			}
//			case 5: {
//				getByteArr(57, 20, "GoalGif");
//				// getByteArrGoal(57, 20, "GoalGif");
//				break;
//			}
//			case 6: {
//				getByteArr(29, 20, "ABBAGif");
//				break;
//			}
//			case 7: {
//				// boolean result = btn1_processData();
//				// while (!result) {
//				// result = btn1_processData();
//				// }
//				getByteArr(3, 100, "Heart");
//
//				break;
//			}
//			case 8: {
//				boolean result = btn8ProcessData();
//				while (!result) {
//					result = btn8ProcessData();
//				}
//				break;
//			}
//			case 9: {
//				getByteArr(7, 100, "Haterz");
//				break;
//			}
//			case 10: {
//				getByteArr(48, 10, "Hitch");
//				break;
//			}
//			case 11: {
//				getByteArr(22, 50, "Marry");
//				break;
//			}
//			case 12: {
//				getByteArr(14, 10, "Medal");
//				break;
//			}
//			case 13: {
//				getByteArr(41, 10, "Black");
//				break;
//			}
//			case 14: {
//				getByteArr(36, 10, "PongGif");
//				break;
//			}
//			case 15: {
//				getByteArr(34, 10, "Superman");
//				break;
//			}
//			case 16: {
//				getByteArr(64, 10, "TieGif");
//				break;
//			}
//			case 17: {
//				boolean result = btn17ProcessData();
//				while (!result) {
//					result = btn17ProcessData();
//				}
//				break;
//			}
//			case 18: {
//				getByteArr(34, 20, "Smile");
//				break;
//			}
//			case 19: {
//				getByteArr(47, 100, "FilmGif");
//				break;
//			}
//			default:
//				break;
//			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			DataPref.dismissLoadingDialog();

		}
	}

	public void processNormalImages() {
		// img1.setImageBitmap(null);
		// img2.setImageBitmap(null);
		// img3.setImageBitmap(null);
		// img4.setImageBitmap(null);
		// img5.setImageBitmap(null);
		// img6.setImageBitmap(null);
		// img7.setImageBitmap(null);
		// img8.setImageBitmap(null);
		// img9.setImageBitmap(null);
		// img10.setImageBitmap(null);
		// img11.setImageBitmap(null);
		// img12.setImageBitmap(null);
		// img13.setImageBitmap(null);
		// img14.setImageBitmap(null);
		// img15.setImageBitmap(null);
		// img16.setImageBitmap(null);
		// img17.setImageBitmap(null);
		// img18.setImageBitmap(null);
		// img19.setImageBitmap(null);
		//
		// img1.setBackgroundResource(R.drawable.hi_img);
		// img2.setBackgroundResource(R.drawable.true_img);
		// img3.setBackgroundResource(R.drawable.logo_img);
		// img4.setBackgroundResource(R.drawable.image4);
		// img5.setBackgroundResource(R.drawable.image5);
		// img6.setBackgroundResource(R.drawable.image6);
		// img7.setBackgroundResource(R.drawable.image7);
		// img8.setBackgroundResource(R.drawable.image8);
		// img9.setBackgroundResource(R.drawable.image9);
		// img10.setBackgroundResource(R.drawable.image10);
		// img11.setBackgroundResource(R.drawable.image11);
		// img12.setBackgroundResource(R.drawable.image12);
		// img13.setBackgroundResource(R.drawable.image13);
		// img14.setBackgroundResource(R.drawable.image14);
		// img15.setBackgroundResource(R.drawable.image15);
		// img16.setBackgroundResource(R.drawable.image16);
		// img17.setBackgroundResource(R.drawable.image17);
		// img18.setBackgroundResource(R.drawable.image18);
		// img19.setBackgroundResource(R.drawable.image19);

		flag = new int[19];
		for (int i = 0; i < 19; i++) {
			flag[i] = 0;
		}

		adapter = new GifAdapter(ImageActivity.this);
		gridView.setAdapter(adapter);

		//getGridViewSize(gridView);

		try {
			ListAdapter listAdapterTheir = gridView.getAdapter();
			int totalHeight = 0;
			Log.v("list length = ", listAdapterTheir.getCount() + ",");

			int total = listAdapterTheir.getCount() / 3;
			if (listAdapterTheir.getCount() % 3 != 0) {
				total = total + 1;
			}

			for (int i = 0; i < total; i++) {
				View listItem = listAdapterTheir.getView(i, null, gridView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
				ViewGroup.LayoutParams params = gridView.getLayoutParams();
				params.height = (int) (totalHeight
						+ ((((int) 18 * ASSL.Yscale()) - 1)));
				gridView.setLayoutParams(params);
				gridView.requestLayout();
			}
		} catch (Exception e) {
			Log.v("error in expand ", e.toString());
		}

		// try {
		// ListAdapter listAdapterTheir = gridView.getAdapter();
		// Log.v("listAdapterTheir", "" + (listAdapterTheir.getCount() + 2) /
		// 3);
		// num = (listAdapterTheir.getCount() + 3) / 3;
		//
		// ViewGroup.LayoutParams params = gridView.getLayoutParams();
		// int min = (int) Math.min(ASSL.Xscale(), ASSL.Yscale());
		// params.height = (num * (220*min));//ASSL
		// Log.v("params1 ", "" + params.height);
		// gridView.setLayoutParams(params);
		//
		//
		//
		// } catch (Exception e) {
		// Log.e("error in expand ", e.toString());
		// }
	}

	public void removeImages() {
		// img1.setBackgroundColor(Color.WHITE);
		// img2.setBackgroundColor(Color.WHITE);
		// img3.setBackgroundColor(Color.WHITE);
		// img4.setBackgroundColor(Color.WHITE);
		// img5.setBackgroundColor(Color.WHITE);
		// img6.setBackgroundColor(Color.WHITE);
		// img7.setBackgroundColor(Color.WHITE);
		// img8.setBackgroundColor(Color.WHITE);
		// img9.setBackgroundColor(Color.WHITE);
		// img10.setBackgroundColor(Color.WHITE);
		// img11.setBackgroundColor(Color.WHITE);
		// img12.setBackgroundColor(Color.WHITE);
		// img13.setBackgroundColor(Color.WHITE);
		// img14.setBackgroundColor(Color.WHITE);
		// img15.setBackgroundColor(Color.WHITE);
		// img16.setBackgroundColor(Color.WHITE);
		// img17.setBackgroundColor(Color.WHITE);
		// img18.setBackgroundColor(Color.WHITE);
		// img19.setBackgroundColor(Color.WHITE);
	}

	/**
	 * Method used to hide keyboard if outside touched.
	 * 
	 * @param view
	 */
	private void setupUI(View view) {
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					InputMethodManager inputMethodManager = (InputMethodManager) ImageActivity.this
							.getSystemService(Activity.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(
							ImageActivity.this.getCurrentFocus()
									.getWindowToken(), 0);
					return false;
				}

			});
		}
		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				setupUI(innerView);
			}
		}
	}

	// *************ADAPTER Fitmoji************************??//

	/**
	 * 
	 * BaseAdapter for add 41 images from drawable to gridview adapter
	 */

	private int flag[];

	private class GifAdapter extends BaseAdapter {

		
		private LayoutInflater inflater;
		ViewHolder holder;
		private Bitmap fillBMP;

		public GifAdapter(Context context) {
			inflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {

			return items.size();

		}

		@Override
		public Object getItem(int i) {

			return items.get(i);

		}

		@Override
		public long getItemId(int i) {

			return i;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {

				convertView = getLayoutInflater().inflate(R.layout.grid_item,

				null);

				holder = new ViewHolder();

				holder.picture = (ImageView) convertView
						.findViewById(R.id.image);
				holder.picture1 = (ImageView) convertView
						.findViewById(R.id.image1);

				holder.gridLL = (RelativeLayout) convertView
						.findViewById(R.id.rv);
				holder.gridLL.setLayoutParams(new GridView.LayoutParams(200,
						200));
				ASSL.DoMagic(holder.gridLL);
				holder.picture.setTag(holder);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();

			}

			holder.id = position;

			Picasso.with(ImageActivity.this)
					.load(items.get(position).drawableId).into(holder.picture);

			holder.picture.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					holder = (ViewHolder) v.getTag();
					int position = holder.id;

//					String name = items.get(position).name;
//					int frame = items.get(position).frame;
//					int fps = items.get(position).fps;
					
					
					run = false;
					if (view != null) {
						view.setAlpha(1.0f);
					}

					if (view == v) {
						view = null;
						getByteArr(4, 50, "Blank");
					} else {
						Log.v("server img click..", "server img click,,,");
						view = v;
						v.setAlpha(0.5f);
						
						//getByteArr(frame, fps, name);
						
						new AsyncNormal().execute(position);
					}

				}

			});

			// if (flag[position] == 1) {
			// holder.picture1.setVisibility(View.VISIBLE);
			// } else {
			// holder.picture1.setVisibility(View.GONE);
			// }

			return convertView;

		}

		

	}

	class ViewHolder {

		ImageView picture, picture1;
		RelativeLayout gridLL;
		int id;
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		ImageLoader imLoader;
		ViewHolder holder;

		// Keep all Images in array

		// Constructor
		public ImageAdapter(Context contxt) {
			mContext = contxt;
			imLoader = new ImageLoader(mContext);
		}

		@Override
		public int getCount() {
			return DataPref.url.length;
		}

		@Override
		public Object getItem(int position) {
			return DataPref.url[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.grid_item, null);
				holder = new ViewHolder();

				holder.picture = (ImageView) convertView.findViewById(R.id.image);
				holder.picture1 = (ImageView) convertView.findViewById(R.id.image1);

				holder.gridLL = (RelativeLayout) convertView.findViewById(R.id.rv);
				holder.gridLL.setLayoutParams(new GridView.LayoutParams(200, 200));
				ASSL.DoMagic(holder.gridLL);
				holder.picture.setTag(holder);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();

			}
			holder.id = position;
			Picasso.with(ImageActivity.this).load(DataPref.url[position]).into(holder.picture);

			holder.picture.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					holder = (ViewHolder) v.getTag();
					int position = holder.id;

					run = false;
					if (view != null) {
						view.setAlpha(1.0f);
					}

					if (view == v) {
						view = null;
						getByteArr(4, 50, "Blank");
					} else {
						Log.v("server img click..", "server img click,,,");
						view = v;
						v.setAlpha(0.5f);
						new AsyncD().execute(position);
					}

				}

			});

			return convertView;

		}

	}
	
	

}
