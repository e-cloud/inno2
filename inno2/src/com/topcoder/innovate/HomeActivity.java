package com.topcoder.innovate;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.topcoder.innovate.model.Speaker;
import com.topcoder.innovate.util.CacheManager;
import com.topcoder.innovate.util.DataRetriever;
import com.topcoder.innovate.util.NetworkDetector;

public class HomeActivity extends Activity {

	private ImageView infoButton;
	private ImageView speakerButton;
	private ImageView mapButton;
	private ProgressDialog mProgressDialog;
	private static final String TAG = "Innovate";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "create home activity.");
		setContentView(R.layout.activity_home);
		init(savedInstanceState);
		if (NetworkDetector.isNetworkConnected(getApplicationContext())) {
			final LoadDataTask ldt = new LoadDataTask(HomeActivity.this);
			ldt.execute();
		} else {
			Toast.makeText(getApplicationContext(), R.string.message,
					Toast.LENGTH_LONG).show();
		}
	}

	private void init(Bundle savedInstanceState) {

		mProgressDialog = new ProgressDialog(HomeActivity.this);
		mProgressDialog.setMessage("Loading Data");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);

		infoButton = (ImageView) findViewById(R.id.info_btn);

		infoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "clicked on info button");
				// Start game here...
				Intent intent = new Intent(HomeActivity.this,
						WebViewActivity.class);

				startActivity(intent);
			}
		});

		speakerButton = (ImageView) findViewById(R.id.speakers_btn);

		speakerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "clicked on speakers button");
				Intent intent = new Intent(HomeActivity.this,
						SpeakerListAcitivity.class);
				intent.putExtras(HomeActivity.this.getIntent());
				startActivity(intent);
			}
		});
		mapButton = (ImageView) findViewById(R.id.map_btn);
		mapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "clicked on speakers button");
				Intent intent = new Intent(HomeActivity.this, MapActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// 清空缓存
		CacheManager cm = new CacheManager(this);
		cm.clearCache();
		return super.onOptionsItemSelected(item);
	}

	private class LoadDataTask extends AsyncTask<Void, Integer, Integer> {

		private Context mContext = null;

		public LoadDataTask(Context context) {
			this.mContext = context;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			mProgressDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// 存储获得得Speaker，用于将数据返回
			List<Speaker> speakerArrayList = null;
			try {
				CacheManager cManager = new CacheManager(mContext);

				String url = mContext.getString(R.string.feeds_speakers);
				String fileName = mContext.getResources().getString(
						R.string.speakersfilename);
				if (!cManager.checkCache(fileName)) {
					cManager.downloadCache(url, fileName);
				}

				// 获取网络数据
				JSONArray jsonArray = DataRetriever.strToJsonArray(cManager
						.readCache(fileName));

				speakerArrayList = new ArrayList<Speaker>();
				Speaker speaker;

				for (int i = 0; i < jsonArray.length(); i++) {
					/**
					 * 服务器返回的数据是JSONArray，在JSONArray里面有不同的JSONObject，
					 * 在JSONObject中“fields“名称后面得值是JSONObject，
					 * 我们需要的就是这个JSONObject
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
					 * 由于sessions后面的值是一个JSONArray数组 所以得先获得这个数组，然后将数组里面得值存放
					 * 在List中
					 */
					JSONArray jsonArrayTemp = jsonObject
							.getJSONArray("sessions");
					List<String> tmp = new ArrayList<String>();
					for (int j = 0; j < jsonArrayTemp.length(); j++) {
						tmp.add(jsonArrayTemp.getString(j));
					}
					speaker.setSessionIds(tmp);
					// 获得jsonObj中的"title"名称后面得值，并保存在speaker中的title域中
					speaker.setTitle(jsonObject.getString("title"));
					// 将speaker放到speakerArrayList中去
					speakerArrayList.add(speaker);
					if (jsonArray.length() > 0) {
						publishProgress((int) (i * 100 / jsonArray.length()));
					}
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
			// 将Speaker list序列化后保存在Intent，以便传给SpeakerListActivity
			Intent intent = ((HomeActivity) mContext).getIntent();
			intent.putExtra("speakers", (Serializable) speakerArrayList);
			return speakerArrayList.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(values[0]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			Toast.makeText(mContext, "Loaded " + result + " speakers.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
