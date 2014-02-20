/**
 * 
 */
package com.topcoder.innovate.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	private final static String TAG = "Innovate.DataRetriever";

	/**
	 * @param content
	 *            Ҫת��������
	 * @return JSONArray
	 * @throws JSONException
	 */
	public static JSONArray strToJsonArray(String content) throws JSONException {
		return new JSONArray(content.replace('\n', ' '));
	}

	public static List<Speaker> retrieveAllSpeakers(Context context) {

		// �洢Speakers�����ڽ����ݷ���
		List<Speaker> speakerArrayList = null;

		try {
			CacheManager cManager = new CacheManager(context);

			String url = context.getString(R.string.feeds_speakers);
			String fileName = context.getResources().getString(
					R.string.speakersfilename);
			if (!cManager.checkCache(fileName)) {
				cManager.downloadCache(url, fileName);
			}

			// ��ȡ��������
			JSONArray jsonArray = strToJsonArray(cManager.readCache(fileName));

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
		Log.i(TAG, "retrieve speakers");
		return speakerArrayList;
	}

	public static List<Blingcoord> retrieveAllBlingcoords(Context context) {

		// �洢��õ�Blingcoord�����ڽ����ݷ���
		List<Blingcoord> blingArrayList = null;
		try {
			CacheManager cManager = new CacheManager(context);

			String url = context.getString(R.string.bling);
			String fileName = context.getResources().getString(
					R.string.blingfilename);
			if (!cManager.checkCache(fileName)) {
				cManager.downloadCache(url, fileName);
			}

			// ��ȡ��������
			JSONArray jsonArray = strToJsonArray(cManager.readCache(fileName));

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
		Log.i(TAG, "retrieve blingcoords");
		return blingArrayList;
	}

}
