package com.topcoder.innovate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class HomeActivity extends Activity {

	private ImageView infoButton;
	private ImageView speakerButton;
	private static final String TAG = "Innovate";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "create home activity.");
		setContentView(R.layout.activity_home);

		initButtons(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	private void initButtons(Bundle savedInstanceState) {
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
				startActivity(intent);
			}
		});
	}

}
