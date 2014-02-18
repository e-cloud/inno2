package com.topcoder.innovate;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

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
	}

	private void init(Bundle savedInstanceState) {

		mProgressDialog = new ProgressDialog(HomeActivity.this);
		mProgressDialog.setMessage("A message");
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
		clearCache(this);
		return super.onOptionsItemSelected(item);
	}

	public static void clearCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
			Log.i(TAG, "cache was cleaned.");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}
}
