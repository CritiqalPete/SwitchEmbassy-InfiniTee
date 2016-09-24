package com.tshirt_os_server;

import java.io.InputStream;
import java.io.OutputStream;
import com.tshirt_os_server.R;

import rmn.androidscreenlibrary.ASSL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Typeface;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DataPref {
	public static OutputStream mmOutputStream;
	public static InputStream mmInputStream;
	public static BluetoothSocket mmSocket;
	static Typeface mediumText, liteText;

	public static String[] url;
	public static String[] name;
	public static String[] min;
	public static String[] size;
	public static String[] gifData;
	public static boolean off;
	public static boolean serverOff;
	public static String batteryStatus;
	private static ProgressDialog progressDial;

	public static Typeface dinMedium(Context appContext) {	 // accessing fonts functions
		if (mediumText == null) {
			mediumText = Typeface.createFromAsset(appContext.getAssets(),
					"DINMedium.ttf");
		}
		return mediumText;
	}

	public static Typeface dinLite(Context appContext) {	 // accessing fonts functions
		if (liteText == null) {
			liteText = Typeface.createFromAsset(appContext.getAssets(),
					"DINLight.ttf");
		}
		return liteText;
	}

	


	/**

	 * Displays custom loading dialog

	 * @param contxt application context

	 * @param msg string message to show in dialog

	 */

	public static void showLoadingDialog(Context contxt, String msg) {
		progressDial = new ProgressDialog(contxt, android.R.style.Theme_Translucent_NoTitleBar);
		//pd_st.getWindow().getAttributes().windowAnimations = R.style.Animations_LoadingDialogFade;
		progressDial.show();
		WindowManager.LayoutParams layoutParams = progressDial.getWindow().getAttributes();
		layoutParams.dimAmount = 0.6f;
		progressDial.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		progressDial.setCancelable(false);
		progressDial.setContentView(R.layout.loading_box);
		FrameLayout framLay = (FrameLayout) progressDial.findViewById(R.id.rv);
		//new ASSL((Activity)c, rv, 1134, 720, false);
		TextView dialogTxt = (TextView) progressDial.findViewById(R.id.textView1);
		dialogTxt.setText(msg);
	}

	/**

	 * Dismisses above loading dialog

	 */

	public static void dismissLoadingDialog() {
		if(progressDial != null){
			progressDial.dismiss();
			progressDial = null;
		}

	}

	public static byte blankRunTxt[] = new byte[5376];

}
