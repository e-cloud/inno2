package com.topcoder.innovate.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.CharArrayBuffer;

import android.content.Context;
import android.util.Log;

public class CacheManager {

	private final static String TAG = "Innovate.CacheManager";

	private Context mContext;

	public CacheManager(Context context) {
		mContext = context;
	}

	/**
	 * @param url
	 *            下载文件的网址
	 * @param fileName
	 *            下载文件保存时的文件名
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void downloadCache(String url, String fileName)
			throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		response = client.execute(httpGet);
		HttpEntity httpEntity = response.getEntity();

		// 创建文件输出流将获得的网络数据存储到指定目录中
		File cacheFile = new File(getCacheDir(), fileName);
		FileOutputStream fos = new FileOutputStream(cacheFile);
		httpEntity.writeTo(fos);
		fos.flush();
		fos.close();

		Log.i(TAG, cacheFile.getAbsolutePath());
		Log.i(TAG, "stored the file " + fileName
				+ " downloaded from the Internet.");
	}

	/**
	 * @return 指定上下文缓存目录的绝对路径
	 */
	private String getCacheDir() {
		return mContext.getCacheDir().getAbsolutePath();
	}

	/**
	 * @param fileName
	 *            需读取的文件名
	 * @return 字符串形式的文件内容
	 * @throws IOException
	 */
	public String readCache(String fileName) throws IOException {

		File readFile = new File(getCacheDir(), fileName);

		if (!readFile.exists()) {
			Log.e(TAG, "Cache file " + fileName + " not found!");
			return null;
		}

		InputStream is = new FileInputStream(readFile);
		Reader reader = new InputStreamReader(is, "utf-8");
		CharArrayBuffer buffer = new CharArrayBuffer(is.available());
		try {
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}

	/**
	 * @param fileName
	 *            要检查的文件的文件名
	 * @return
	 */
	public boolean checkCache(String fileName) {
		File readFile = new File(getCacheDir(), fileName);
		if (readFile.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * TODO: 清理缓存目录下所有文件
	 */
	public void clearCache() {
		try {
			File dir = mContext.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				File[] files = dir.listFiles();
				for (File f : files)
					f.delete();
			}
			Log.i(TAG, "cache was cleaned.");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
