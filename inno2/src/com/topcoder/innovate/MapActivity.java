package com.topcoder.innovate;

import java.lang.ref.WeakReference;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.topcoder.innovate.model.Blingcoord;
import com.topcoder.innovate.util.DataRetriever;

public class MapActivity extends Activity {

	private final static String TAG = "Innovate";
	private WeakReference<MapActivity> mWeakReference = new WeakReference<MapActivity>(
			this);
	private List<Blingcoord> mBlingcoords = null;
	private Handler mHandler = new MapHandler(mWeakReference);
	private GoogleMap mGoogleMap;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Get a handle to the Map Fragment
		mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		LatLng center = new LatLng(37.783753, -122.401192);

		mGoogleMap.setMyLocationEnabled(true);
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15));

		RtrieveData();

	}

	private void setMarkers() {
		if (mBlingcoords != null) {
			LatLng place;
			Blingcoord tmpbling;
			for (int i = 0; i < mBlingcoords.size(); i++) {
				tmpbling = mBlingcoords.get(i);
				place = new LatLng(tmpbling.getLatitude(),
						tmpbling.getLongitude());
				mGoogleMap.addMarker(
						new MarkerOptions()
								.title(tmpbling.getName())
								.snippet(
										tmpbling.getName() + ". "
												+ tmpbling.getAddress())
								.position(place)).setIcon(
						BitmapDescriptorFactory.fromResource(R.drawable.u));

			}
			mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker arg0) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), arg0.getSnippet(),
							Toast.LENGTH_SHORT).show();
					return false;
				}
			});
		}
	}

	private void RtrieveData() {
		new Thread(new Runnable() {
			public void run() {
				mBlingcoords = DataRetriever
						.retrieveAllBlingcoords(MapActivity.this);

				Message msg = new Message();
				Bundle data = new Bundle();
				if (mBlingcoords != null) {
					data.putString("Download", "successful");
				} else {
					data.putString("Download", "failed");
				}
				msg.setData(data);
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private static class MapHandler extends Handler {
		WeakReference<MapActivity> weakReference = null;

		public MapHandler(WeakReference<MapActivity> ref) {
			this.weakReference = ref;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Bundle data = msg.getData();
			Log.i(TAG, data.getString("Download"));
			if (weakReference != null
					&& data.getString("Download") == "successful") {
				weakReference.get().setMarkers();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
