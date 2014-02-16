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
		// ����Դ��ʶ��תΪ�ַ���
		String url = activity.getResources().getString(R.string.feeds_speakers);
		// �洢��õ�Speaker�����ڽ����ݷ���
		List<Speaker> speakerArrayList = new ArrayList<Speaker>();
		// ָ����������URL
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();

		HttpResponse response;
		try {
			response = client.execute(httpGet);

			// ��ȡ��Ӧʵ��
			HttpEntity httpEntity = response.getEntity();
			// ����Ӧʵ��ת��Ϊ�ַ���
			String jsonString = EntityUtils.toString(httpEntity);

			// ���ַ���ת��ΪJSONArray
			JSONArray jsonArray = new JSONArray(jsonString.replace('\n', ' '));
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
		// ����Դ��ʶ��תΪ�ַ���
		String url = activity.getResources().getString(R.string.bling);
		// �洢��õ�Blingcoord�����ڽ����ݷ���
		List<Blingcoord> blingArrayList = null;
		// ָ����������URL
		HttpGet httpGet = new HttpGet(url);

		HttpClient httpClient = new DefaultHttpClient();

		try {

			HttpResponse httpResponse = httpClient.execute(httpGet);
			// ��ȡ��Ӧʵ��
			HttpEntity httpEntity = httpResponse.getEntity();
			// ����Ӧʵ��ת��Ϊ�ַ���
			String jsonString = EntityUtils.toString(httpEntity);

			// ���ַ���תΪJSONArray
			JSONArray jsonArray = new JSONArray(jsonString);
			Blingcoord blingcoord;

			// ���ѻ�ȡ�������ݺ�Ŵ�������ʵ��
			blingArrayList = new ArrayList<Blingcoord>();

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
