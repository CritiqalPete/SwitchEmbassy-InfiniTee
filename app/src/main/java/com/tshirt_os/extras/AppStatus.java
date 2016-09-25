package com.tshirt_os.extras;

/**
 * This file is used by all the other classes to check Internet connection 
 * 
 * Developed by ClickLabs. Developer: Gurmail Singh
 * Link: http://www.click-labs.com/
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppStatus {
	
	private static AppStatus instance = new AppStatus();
	static Context context;
	private ConnectivityManager connectManager;
	private boolean connected = false;

	/**
	 * Method used to return then instance of the class.
	 * @param ctx Context
	 * @return class instance.
	 */
	public static AppStatus getInstance(Context ctx) {
		context = ctx;
		return instance;
	}

	/**
	 * Method used to check is device connected or not
	 * @param con Application Context
	 * @return true is connected
	 */
	public boolean isOnline(Context con) {
		try {
			connectManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
			connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
			return connected;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return connected;
	}
}
