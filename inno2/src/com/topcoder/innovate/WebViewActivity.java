/**
 * 
 */
package com.topcoder.innovate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

/**
 * @author ScottSaint
 * 
 */
public class WebViewActivity extends Activity {

	private ImageView mHomeButton;
	private WebView mWebView;
	private static final String TAG = "Innovate";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		mHomeButton = (ImageView) findViewById(R.id.home_btn);
		mWebView = (WebView) findViewById(R.id.web_view);
		mHomeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(TAG, "clicked on home button");
				Intent intent = new Intent(WebViewActivity.this,
						HomeActivity.class);
				startActivity(intent);
				/*
				 * another way to stimulate "back" button
				 * 
				 * int keyCode = KeyEvent.KEYCODE_BACK; KeyEvent event = new
				 * KeyEvent(KeyEvent.ACTION_DOWN, keyCode); onKeyDown(keyCode,
				 * event);
				 */
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {

			/*
			 * let the hyperlinks jump in the current view
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				Log.d(TAG, "load url" + url);
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(getResources().getString(R.string.url));
		Log.d(TAG, "loaded the url.");
	}

	// To handle the back button key press
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
