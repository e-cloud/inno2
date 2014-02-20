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
	 *            要转换的内容
	 * @return JSONArray
	 * @throws JSONException
	 */
	public static JSONArray strToJsonArray(String content) throws JSONException {
		return new JSONArray(content.replace('\n', ' '));
	}

	public static List<Speaker> retrieveAllSpeakers(Context context) {

		// 存储Speakers，用于将数据返回
		List<Speaker> speakerArrayList = null;

		try {
			CacheManager cManager = new CacheManager(context);

			String url = context.getString(R.string.feeds_speakers);
			String fileName = context.getResources().getString(
					R.string.speakersfilename);
			if (!cManager.checkCache(fileName)) {
				cManager.downloadCache(url, fileName);
			}

			// 获取网络数据
			JSONArray jsonArray = strToJsonArray(cManager.readCache(fileName));

			speakerArrayList = new ArrayList<Speaker>();
			Speaker speaker;

			for (int i = 0; i < jsonArray.length(); i++) {
				/**
				 * 服务器返回的数据是JSONArray，在JSONArray里面有不同的JSONObject，
				 * 在JSONObject中“fields“名称后面得值是JSONObject， 我们需要的就是这个JSONObject
				 */
				JSONObject jsonObject = jsonArray.getJSONObject(i)
						.getJSONObject("fields");
				// 初始化Speaker
				speaker = new Speaker();
				// 获得jsonObj中的相应名称后面的值，并保存在speaker中的相应域中
				speaker.setName(jsonObject.getString("name"));
				speaker.setDetails(jsonObject.getString("details"));
				speaker.setPicture(jsonObject.getString("picture"));
				/**
				 * 由于sessions后面的值是一个JSONArray数组 所以得先获得这个数组，然后将数组里面得值存放 在List中
				 */
				JSONArray jsonArrayTemp = jsonObject.getJSONArray("sessions");
				List<String> tmp = new ArrayList<String>();
				for (int j = 0; j < jsonArrayTemp.length(); j++) {
					tmp.add(jsonArrayTemp.getString(j));
				}
				speaker.setSessionIds(tmp);
				// 获得jsonObj中的"title"名称后面得值，并保存在speaker中的title域中
				speaker.setTitle(jsonObject.getString("title"));
				// 将speaker放到speakerArrayList中去
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

		// 存储获得得Blingcoord，用于将数据返回
		List<Blingcoord> blingArrayList = null;
		try {
			CacheManager cManager = new CacheManager(context);

			String url = context.getString(R.string.bling);
			String fileName = context.getResources().getString(
					R.string.blingfilename);
			if (!cManager.checkCache(fileName)) {
				cManager.downloadCache(url, fileName);
			}

			// 获取网络数据
			JSONArray jsonArray = strToJsonArray(cManager.readCache(fileName));

			// 在已获取网络数据后才创建数组实例
			blingArrayList = new ArrayList<Blingcoord>();
			Blingcoord blingcoord;

			for (int i = 0; i < jsonArray.length(); i++) {
				/**
				 * 服务器返回的数据是JSONArray，在JSONArray里面有不同的JSONObject，
				 * 在JSONObject中“fields“名称后面得值是JSONObject， 我们需要的就是这个JSONObject
				 */
				JSONObject jsonObj = jsonArray.getJSONObject(i).getJSONObject(
						"fields");
				// 初始化Blingcoord
				blingcoord = new Blingcoord();

				// 获得jsonObj中的"name"名称后面得值，并保存在blingcoord中的name域中
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

				// 将blingcoord放到blingArrayList中去
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
