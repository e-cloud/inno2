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

		// 设置home按钮的监听器
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

		// 设置info按钮的监听器
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

		// 设置列表视图的监听器
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

		// 获取从HomeActivity传来的数据，并转换成所需的数据
		mSpeakers = (List<Speaker>) getIntent()
				.getSerializableExtra("speakers");

		// 创建相应的适配器，使得列表能正确显示数据
		SpeakerAdapter speakerAdapter = new SpeakerAdapter(this, mSpeakers);
		mListView.setAdapter(speakerAdapter);

	}

	// 用于缓存视图控件的引用
	private static class ViewHolder {
		ImageView mImage = null;
		TextView mName = null;
		TextView mTitle = null;
	}

	private class SpeakerAdapter extends BaseAdapter {

		@SuppressWarnings("unused")
		private Context mContext; // 运行上下文
		private List<Speaker> mListItems; // 发言人信息集合
		private LayoutInflater mLayoutInflater; // 视图容器

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
				// 关联数据与UI
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

			// 利用反射机制寻找并绑定相应图片
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
