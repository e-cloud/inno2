/**
 * 
 */
package com.topcoder.innovate.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.topcoder.innovate.R;
import com.topcoder.innovate.model.Blingcoord;
import com.topcoder.innovate.model.Speaker;

/**
 * @author ScottSaint
 * 
 */
public class DataRetriever {
	private final static String TAG = "Dataretriever";

	private static void retrieveDataAsCache(String url, String filename,
			String targetDir) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		response = client.execute(httpGet);
		HttpEntity httpEntity = response.getEntity();

		// �����ļ����������õ��������ݴ洢��ָ��Ŀ¼��
		File cacheFile = new File(targetDir, filename);
		Log.i(TAG, cacheFile.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(cacheFile);
		httpEntity.writeTo(fos);
		fos.flush();
		fos.close();
		Log.i(TAG, "stored the file " + filename
				+ " retrieve from the Internet.");
	}

	private static JSONArray retrieveData(Context context, String url,
			String filename) throws JSONException, IOException {
		File cachedir = context.getCacheDir();
		File readFile = new File(cachedir.getAbsolutePath(), filename);

		// ��������ļ������ڣ�������
		if (!readFile.exists()) {
			retrieveDataAsCache(url, filename, context.getCacheDir()
					.getAbsolutePath());
			Log.i(TAG, "download a file as cache.");
		}
		JSONArray jsonArray = null;

		// ��ȡ�����ļ�
		InputStream is = new FileInputStream(readFile);
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = is.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		is.close();

		// ת����json���ݶ���
		jsonArray = new JSONArray(out.toString().replace('\n', ' '));
		return jsonArray;
	}

	public static List<Speaker> retrieveAllSpeakers(Activity activity) {

		// �洢��õ�Speaker�����ڽ����ݷ���
		List<Speaker> speakerArrayList = null;
		try {
			// ��ȡ��������
			JSONArray jsonArray = retrieveData(activity, activity
					.getResources().getString(R.string.feeds_speakers),
					activity.getResources()
							.getString(R.string.speakersfilename));

			speakerArrayList = new ArrayList<Speaker>();
			Speaker speaker;

			for (int i = 0; i < jsonArray.length(); i++) {
				/**
				 * ���������ص�������JSONArray����JSONArray�����в�ͬ��JSONObject��
				 * ��JSONObject�С�fields�����ƺ����ֵ��JSONObject�� ������Ҫ�ľ������JSONObject
				 */
				JSONObject jsonObject = jsonArray.getJSONObject(i)
						.getJSONObject("fields");
				// ��ʼ��Speaker
				speaker = new Speaker();
				// ���jsonObj�е���Ӧ���ƺ����ֵ����������speaker�е���Ӧ����
				speaker.setName(jsonObject.getString("name"));
				speaker.setDetails(jsonObject.getString("details"));
				speaker.setPicture(jsonObject.getString("picture"));
				/**
				 * ����sessions�����ֵ��һ��JSONArray���� ���Ե��Ȼ��������飬Ȼ�����������ֵ��� ��List��
				 */
				JSONArray jsonArrayTemp = jsonObject.getJSONArray("sessions");
				List<String> tmp = new ArrayList<String>();
				for (int j = 0; j < jsonArrayTemp.length(); j++) {
					tmp.add(jsonArrayTemp.getString(j));
				}
				speaker.setSessionIds(tmp);
				// ���jsonObj�е�"title"���ƺ����ֵ����������speaker�е�title����
				speaker.setTitle(jsonObject.getString("title"));
				// ��speaker�ŵ�speakerArrayList��ȥ
				speakerArrayList.add(speaker);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return speakerArrayList;
	}

	public static List<Blingcoord> retrieveAllBlingcoords(Activity activity) {

		// �洢��õ�Blingcoord�����ڽ����ݷ���
		List<Blingcoord> blingArrayList = null;
		try {

			// ��ȡ��������
			JSONArray jsonArray = retrieveData(activity, activity
					.getResources().getString(R.string.bling), activity
					.getResources().getString(R.string.blingfilename));

			// ���ѻ�ȡ�������ݺ�Ŵ�������ʵ��
			blingArrayList = new ArrayList<Blingcoord>();
			Blingcoord blingcoord;

			for (int i = 0; i < jsonArray.length(); i++) {
				/**
				 * ���������ص�������JSONArray����JSONArray�����в�ͬ��JSONObject��
				 * ��JSONObject�С�fields�����ƺ����ֵ��JSONObject�� ������Ҫ�ľ������JSONObject
				 */
				JSONObject jsonObj = jsonArray.getJSONObject(i).getJSONObject(
						"fields");
				// ��ʼ��Blingcoord
				blingcoord = new Blingcoord();

				// ���jsonObj�е�"name"���ƺ����ֵ����������blingcoord�е�name����
				blingcoord.setCity(jsonObj.getString("city"));
				blingcoord.setName(jsonObj.getString("name"));
				blingcoord.setZipCode(jsonObj.getString("zip"));
				blingcoord.setLongitude(Double.parseDouble(jsonObj
						.getString("longitude")));
				blingcoord.setState(jsonObj.getString("state"));
				blingcoord.setAddress(jsonObj.getString("address"));
				blingcoord.setLatitude(Double.parseDouble(jsonObj
						.getString("latitude")));
				blingcoord.setBling_id(jsonObj.getString("bling_id"));

				// ��blingcoord�ŵ�blingArrayList��ȥ
				blingArrayList.add(blingcoord);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blingArrayList;
	}

}
