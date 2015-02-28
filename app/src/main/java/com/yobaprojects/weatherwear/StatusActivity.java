package com.yobaprojects.weatherwear;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yobaprojects.weatherwear.wear_item.WearItem;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;


public class StatusActivity extends ActionBarActivity {

	private ExpandableListView listView;
	private ArrayList <WearItem> wearingItems;
	private WearExpandableListAdapter listAdapter;
	private static Queue<Runnable> tasksPool = new ArrayDeque<> ();
	private class AddActiveWearItemTask implements Runnable {
		byte [] NFCID;

		private AddActiveWearItemTask (byte[] NFCID) {
			this.NFCID = NFCID;
		}

		@Override
		public void run () {
			myService.addActiveWearItem (NFCID);
			handler.sendEmptyMessage (NOTIFY_LISTVIEW_HANDLER_TASK);
		}
	}

	private Runnable drawListView = new Runnable () {
		@Override
		public void run () {
			wearingItems = myService.getActiveWearItems ();
			handler.sendEmptyMessage (DRAW_LISTVIEW_HANDLER_TASK);
		}
	};

	public static final int DRAW_LISTVIEW_HANDLER_TASK = 1;
	public static final int NOTIFY_LISTVIEW_HANDLER_TASK = 2;

	Handler handler = new Handler () {
		@Override
		public void handleMessage (Message msg) {
			switch (msg.what) {
				case DRAW_LISTVIEW_HANDLER_TASK:
					((ProgressBar) findViewById (R.id.progressBar)).setVisibility (View.GONE);

					listAdapter = new WearExpandableListAdapter (getApplicationContext (), wearingItems);
					listView.setVisibility (View.VISIBLE);
					listView.setAdapter (listAdapter);
					for (int i = 0; i < listAdapter.getGroupCount (); i++)
						listView.expandGroup (i);
					break;
				case NOTIFY_LISTVIEW_HANDLER_TASK:
					listAdapter.notifyDataSetChanged ();
					break;
			}
		}
	};

	public final static String LOG_TAG = StatusActivity.class.getSimpleName () + "_LOG";

	private Intent myServiceIntent;
	private static MyService myService;
	private static boolean bound = false;
	private static ServiceConnection sConnection = new ServiceConnection () {
		@Override
		public void onServiceConnected (ComponentName name, IBinder service) {
			myService = ((MyService.MyBinder) service).getService ();
			bound = true;
			Log.i (LOG_TAG, "onServiceConnected");
			MainActivity.executeServiceTasks (tasksPool);
		}

		@Override
		public void onServiceDisconnected (ComponentName name) {
			bound = false;
			Log.i (LOG_TAG, "onServiceDisconnected");
		}
	};

	private NfcAdapter mAdapter;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		setContentView (R.layout.activity_status);

		myServiceIntent = new Intent (this, MyService.class);

		startService (myServiceIntent);
		bindService (myServiceIntent, sConnection, 0);

		addNewTask (drawListView);

		wearingItems = new ArrayList<> ();
		listView = (ExpandableListView) findViewById (R.id.expandableListView);


		GridView gridview = (GridView) findViewById(R.id.weather_grid);

		ArrayList <Bundle> weatherForecast = new ArrayList<> ();

		long [] times = {100500L, 200300L, 900701L, 5000000L, 10000000L, 234523462L, 987654L};

		for (long time : times) {
			Bundle bundle = new Bundle ();
			bundle.putLong ("time", time);
			weatherForecast.add (bundle);
		}
		gridview.setNumColumns (weatherForecast.size ());
		gridview.setAdapter(new WeatherGridAdapter(this, weatherForecast));

		resolveIntent (getIntent ());

		Log.i (LOG_TAG, "onCreate");
		//SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter (this, )
	}


	@Override
	protected void onNewIntent (Intent intent) {
		resolveIntent (intent);
		Log.i (LOG_TAG, "onNewIntent");
	}

	@Override
	protected void onPause () {
		super.onPause ();
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch (this);
		}
		Log.i (LOG_TAG, "onPause");
	}

	@Override
	protected void onResume () {
		super.onResume ();

		PendingIntent mPendingIntent = PendingIntent.getActivity (this, 0,
				new Intent (this, getClass ()).addFlags (Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter == null) {
			Toast.makeText (getApplicationContext (), "NFC is disable!", Toast.LENGTH_SHORT).show ();
		} else
			mAdapter.enableForegroundDispatch (this, mPendingIntent, null, null);

		Log.i (LOG_TAG, "onResume");
	}

	private void addNewTask (Runnable runnable) {
		tasksPool.add (runnable);
		if (bound)
			MainActivity.executeServiceTasks (tasksPool);
	}


	private void resolveIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Toast.makeText (getApplicationContext (), "READING NFC!", Toast.LENGTH_SHORT).show ();

			byte [] NFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

			addNewTask (new AddActiveWearItemTask (NFCID));
			//fillNFCField ();

		}
		//fillField (intent.getExtras ());
	}

	@Override
	protected void onStart () {
		super.onStart ();
		Log.i (LOG_TAG, "onStart");
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult (requestCode, resultCode, data);
		Log.i (LOG_TAG, "onActivityResult");
	}

	@Override
	protected void onStop () {
		super.onStop ();
		Log.i (LOG_TAG, "onStop");
	}

	@Override
	protected void onDestroy () {
		super.onDestroy ();
		Log.i (LOG_TAG, "onDestroy");
	}

	@Override
	protected void onSaveInstanceState (Bundle outState) {
		super.onSaveInstanceState (outState);
		Log.i (LOG_TAG, "onSaveInstanceState");
	}

	@Override
	protected void onRestart () {
		super.onRestart ();
		Log.i (LOG_TAG, "onRestart");
	}

	@Override
	protected void onRestoreInstanceState (Bundle savedInstanceState) {
		super.onRestoreInstanceState (savedInstanceState);
		Log.i (LOG_TAG, "onRestoreInstanceState");
	}
}
