/**
 * 
 */
package com.topcoder.innovate;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.topcoder.innovate.model.Speaker;

/**
 * @author ScottSaint
 * 
 */
public class SpeakerDetailActivtiy extends Activity {
	private ImageView mHomeButton;
	private ImageView mImageView;
	private Speaker mSpeaker;
	private ImageView mSpeakerImage;
	private ImageView mHideArrow;
	private TextView mSpeakerName;
	private TextView mSpeakerTitle;
	private TextView mSpeakerDetail;
	private static final String TAG = "Innovate";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speakerdetail);
		mSpeaker = (Speaker) getIntent().getExtras().getSerializable("speaker");

		mSpeakerImage = (ImageView) findViewById(R.id.detail_speaker_picture);
		mSpeakerName = (TextView) findViewById(R.id.detail_speaker_name);
		mSpeakerTitle = (TextView) findViewById(R.id.detail_speaker_title);
		mSpeakerDetail = (TextView) findViewById(R.id.speaker_detail);
		mHideArrow = (ImageView) findViewById(R.id.hide_arrow);
		mHomeButton = (ImageView) findViewById(R.id.home_btn);
		mImageView = (ImageView) findViewById(R.id.info_btn);

		Field field;
		try {
			Class<com.topcoder.innovate.R.drawable> resource = R.drawable.class;
			field = resource.getField(mSpeaker.getPictureName());
			mSpeakerImage.setImageResource(field.getInt(null));
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			Log.w(TAG, "particular image not found and use default image.");
			mSpeakerImage.setImageResource(R.drawable.default_speaker);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mSpeakerName.setText(mSpeaker.getName());
		mSpeakerTitle.setText(mSpeaker.getTitle());
		mSpeakerDetail.setText(mSpeaker.getDetails());
		mSpeakerDetail.setMovementMethod(ScrollingMovementMethod.getInstance());

		mHomeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "clicked on home button");
				Intent intent = new Intent(SpeakerDetailActivtiy.this,
						HomeActivity.class);
				startActivity(intent);
			}
		});

		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "clicked on info button");
				// Start game here...
				Intent intent = new Intent(SpeakerDetailActivtiy.this,
						WebViewActivity.class);
				startActivity(intent);
			}
		});

		mHideArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "clicked on arrow button");
				finish();
			}
		});
	}

}
