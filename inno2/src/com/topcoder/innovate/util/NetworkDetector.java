package com.topcoder.innovate.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkDetector {

	/*
	 * 判断是否有网络连接
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				Log.i("network", mNetworkInfo.toString());
				return mNetworkInfo.isConnected();
			}
		}
		return false;
	}

	public static boolean isReachable() {
		URL url;
		try {
			url = new URL("http://www.jiaozuoye.com");

			// open a connection to that source
			HttpURLConnection urlConnect = (HttpURLConnection) url
					.openConnection();
			urlConnect.setConnectTimeout(500);

			// trying to retrieve data from the source. If there
			// is no connection, this line will fail
			InputStream objData = (InputStream) urlConnect.getContent();
			Log.i("network", streamtostring(objData));
			return true;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.w("network", "catch eception.");
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public static String streamtostring(InputStream inputStream) {
		Reader reader;
		StringBuffer buffer = null;
		try {
			reader = new InputStreamReader(inputStream, "GBK");

			buffer = new StringBuffer(inputStream.available());
			try {
				char[] tmp = new char[1024];
				int l;
				while ((l = reader.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
			} finally {
				reader.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public static boolean pingIpAddr() {
		String mPingIpAddrResult = null;
		try {
			// This is hardcoded IP addr. This is for testing purposes.
			// We would need to get rid of this before release.
			String ipAddress = "www.jiaozuoye.com";

			Process p = Runtime.getRuntime().exec("ping -c 1" + ipAddress);

			int status = p.waitFor();
			if (status == 0) {
				mPingIpAddrResult = "Pass";
				Log.i("network", mPingIpAddrResult);
				return true;
			} else {
				mPingIpAddrResult = "Fail: IP addr not reachable";
			}
		} catch (IOException e) {
			mPingIpAddrResult = "Fail: IOException";
		} catch (InterruptedException e) {
			mPingIpAddrResult = "Fail: InterruptedException";
		}
		Log.i("network", mPingIpAddrResult);
		return false;
	}

	/*
	 * 判断WIFI网络是否可用
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/*
	 * 判断MOBILE网络是否可用
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/*
	 * 获取当前网络连接的类型信息
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

}
