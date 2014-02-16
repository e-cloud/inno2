/**
 * 
 */
package com.topcoder.innovate.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.topcoder.innovate.R;
import com.topcoder.innovate.model.Blingcoord;
import com.topcoder.innovate.model.Speaker;

/**
 * @author ScottSaint
 * 
 */
public class DataRetriever {
	public List<Speaker> retrieveAllSpeakers(Activity activity) {
		// 将资源标识符转为字符串
		String url = activity.getResources().getString(R.string.feeds_speakers);
		// 存储获得得Speaker，用于将数据返回
		List<Speaker> speakerArrayList = new ArrayList<Speaker>();
		// 指定服务器端URL
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();

		HttpResponse response;
		try {
			response = client.execute(httpGet);

			// 获取响应实体
			HttpEntity httpEntity = response.getEntity();
			// 将响应实体转换为字符串
			String jsonString = EntityUtils.toString(httpEntity);

			// 将字符串转换为JSONArray
			JSONArray jsonArray = new JSONArray(jsonString.replace('\n', ' '));
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
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return speakerArrayList;
	}

	public static List<Blingcoord> retrieveAllBlingcoords(Activity activity) {
		// 将资源标识符转为字符串
		String url = activity.getResources().getString(R.string.bling);
		// 存储获得得Blingcoord，用于将数据返回
		List<Blingcoord> blingArrayList = null;
		// 指定服务器端URL
		HttpGet httpGet = new HttpGet(url);

		HttpClient httpClient = new DefaultHttpClient();

		try {

			HttpResponse httpResponse = httpClient.execute(httpGet);
			// 获取响应实体
			HttpEntity httpEntity = httpResponse.getEntity();
			// 将响应实体转换为字符串
			String jsonString = EntityUtils.toString(httpEntity);

			// 将字符串转为JSONArray
			JSONArray jsonArray = new JSONArray(jsonString);
			Blingcoord blingcoord;

			// 在已获取网络数据后才创建数组实例
			blingArrayList = new ArrayList<Blingcoord>();

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

		return blingArrayList;
	}
}
