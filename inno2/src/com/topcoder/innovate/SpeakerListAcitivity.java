/**
 * 
 */
package com.topcoder.innovate;

import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_speakerlist);

		// mSpeakers = loadDataFromAsset();

		mListView = (ListView) findViewById(R.id.listview);
		homeButton = (ImageView) findViewById(R.id.home_btn);
		infoButton = (ImageView) findViewById(R.id.info_btn);

		// ����home��ť�ļ�����
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

		// ����info��ť�ļ�����
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

		// �����б���ͼ�ļ�����
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

		// ��ȡ��HomeActivity���������ݣ���ת�������������
		mSpeakers = (List<Speaker>) getIntent()
				.getSerializableExtra("speakers");

		// ������Ӧ����������ʹ���б�����ȷ��ʾ����
		SpeakerAdapter speakerAdapter = new SpeakerAdapter(this, mSpeakers);
		mListView.setAdapter(speakerAdapter);

	}

	// ���ڻ�����ͼ�ؼ�������
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

			// ���÷������Ѱ�Ҳ�����ӦͼƬ
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

}
