/**
 * 
 */
package com.topcoder.innovate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.CharArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.topcoder.innovate.model.Speaker;
import com.topcoder.innovate.util.DataRetriever;
import com.topcoder.innovate.util.NetworkDetector;

/**
 * @author ScottSaint
 * 
 */
public class SpeakerListAcitivity extends Activity {
	private ImageView homeButton;
	private ImageView infoButton;
	private ListView mListView;
	private static final String TAG = "Innovate";
	private List<Speaker> mSpeakers;

	private WeakReference<SpeakerListAcitivity> wfsla = new WeakReference<SpeakerListAcitivity>(
			this);
	private Handler handler = new MyHandler(wfsla);

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_speakerlist);

		// mSpeakers = loadDataFromAsset();

		if (NetworkDetector.isNetworkConnected(getApplicationContext())) {

			createDownloadThread();

			mListView = (ListView) findViewById(R.id.listview);
			homeButton = (ImageView) findViewById(R.id.home_btn);
			infoButton = (ImageView) findViewById(R.id.info_btn);

			homeButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG, "clicked on home button");
					Intent intent = new Intent(SpeakerListAcitivity.this,
							HomeActivity.class);
					startActivity(intent);
				}
			});

			infoButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG, "clicked on info button");
					// Start game here...
					Intent intent = new Intent(SpeakerListAcitivity.this,
							WebViewActivity.class);
					startActivity(intent);
				}
			});

			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(SpeakerListAcitivity.this,
							SpeakerDetailActivtiy.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("speaker", mSpeakers.get(position));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
		} else {
			new AlertDialog.Builder(SpeakerListAcitivity.this)
					.setTitle(R.string.head)
					.setMessage(R.string.message)
					.setPositiveButton(
							getResources().getString(R.string.ok),
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							}).show();
		}
	}

	private static class MyHandler extends Handler {
		WeakReference<SpeakerListAcitivity> weakref = null;

		public MyHandler(WeakReference<SpeakerListAcitivity> weakref) {
			this.weakref = weakref;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String val = data.getString("download");
			Log.i(TAG, "message-->" + val);
			if (weakref != null && val == "succeed") {
				weakref.get().setAdapter();
			}
		}
	}

	private void setAdapter() {
		SpeakerAdapter speakerAdapter = new SpeakerAdapter(this, mSpeakers);
		mListView.setAdapter(speakerAdapter);
	}

	@SuppressLint("NewApi")
	private void createDownloadThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO: http request.
				mSpeakers = DataRetriever
						.retrieveAllSpeakers(SpeakerListAcitivity.this);

				Message msg = new Message();
				Bundle data = new Bundle();
				if (mSpeakers != null) {
					Log.i(TAG, "list size = " + mSpeakers.size());
					data.putString("download", "succeed");
				} else {
					data.putString("download", "failed");
				}
				msg.setData(data);
				handler.sendMessage(msg);
			}
		}).start();
	}

	private static class ViewHolder {
		ImageView mImage = null;
		TextView mName = null;
		TextView mTitle = null;
	}

	private class SpeakerAdapter extends BaseAdapter {

		@SuppressWarnings("unused")
		private Context mContext; // ����������
		private List<Speaker> mListItems; // ��������Ϣ����
		private LayoutInflater mLayoutInflater; // ��ͼ����

		public SpeakerAdapter(Context context, List<Speaker> listItems) {
			this.mContext = context;
			mLayoutInflater = LayoutInflater.from(context);
			this.mListItems = listItems;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = mLayoutInflater.inflate(R.layout.speaker, parent,
						false);
				// ����������UI
				viewHolder.mImage = (ImageView) convertView
						.findViewById(R.id.speaker_picture);
				viewHolder.mName = (TextView) convertView
						.findViewById(R.id.speaker_name);
				viewHolder.mTitle = (TextView) convertView
						.findViewById(R.id.speaker_title);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			Speaker speaker = mListItems.get(position);
			Field field;
			try {
				Class<com.topcoder.innovate.R.drawable> resource = R.drawable.class;
				field = resource.getField(speaker.getPictureName());
				viewHolder.mImage.setImageResource(field.getInt(null));
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				Log.w(TAG, "particular image not found and use default image.");
				viewHolder.mImage.setImageResource(R.drawable.default_speaker);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			viewHolder.mName.setText(speaker.getName());
			viewHolder.mTitle.setText(speaker.getTitle());
			return convertView;
		}
	}

	@SuppressWarnings("unused")
	private List<Speaker> loadDataFromAsset() {
		AssetManager assetManager = this.getResources().getAssets();
		try {
			return retrieveAllSpeakers(assetManager.open("speakers.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "file not found");
			return null;
		}
	}

	private List<Speaker> retrieveAllSpeakers(InputStream inputStream) {
		// �洢��õ�Speaker�����ڽ����ݷ���
		List<Speaker> speakerArrayList = new ArrayList<Speaker>();
		try {
			// ��������Ҫ���������е��ֽ���ת��Ϊ�ַ������Ա�json����������ȷ����
			Reader reader = new InputStreamReader(inputStream, "utf-8");
			CharArrayBuffer buffer = new CharArrayBuffer(
					inputStream.available());
			try {
				char[] tmp = new char[1024];
				int l;
				while ((l = reader.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
			} finally {
				reader.close();
			}

			// ���ַ���ת��ΪJSONArray
			JSONArray jsonArray = new JSONArray(buffer.toString().replace('\n',
					' '));
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

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return speakerArrayList;
	}

}
