package com.yobaprojects.weatherwear;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.yobaprojects.weatherwear.wear_item.WearDataBase;
import com.yobaprojects.weatherwear.wear_item.WearItem;

import java.util.ArrayList;
import java.util.Arrays;

public class MyService extends Service {

	private static WearDataBase wearDataBase;
	MyBinder binder = new MyBinder ();

	private ArrayList <WearItem> activeWearItems;

	public final static String LOG_TAG = MyService.class.getSimpleName () + "_LOG";
	public IBinder onBind (Intent intent) {
		Log.i (LOG_TAG, "onBind");
		return binder;
	}

	@Override
	public void onCreate () {
		super.onCreate ();

		Log.i (LOG_TAG, "onCreate");
		activeWearItems = new ArrayList<> ();
		wearDataBase = new WearDataBase (getApplicationContext ());
	}



	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		super.onStartCommand (intent, flags, startId);

		Log.i (LOG_TAG, "onStartCommand");
		return START_STICKY;
	}

	@Override
	public void onDestroy () {
		Log.i (LOG_TAG, "onDestroy");
		super.onDestroy ();
	}

	@Override
	public boolean onUnbind (Intent intent) {
//		return super.onUnbind (intent);
		Log.i (LOG_TAG, "onUnbind");

		return true;
	}

	@Override
	public void onRebind (Intent intent) {
		super.onRebind (intent);
		Log.i (LOG_TAG, "onRebind");
	}


	public ArrayList<WearItem> getWearItems () {
		return wearDataBase.getAll ();
	}

	public ArrayList<WearItem> getActiveWearItems () {
		return activeWearItems;
	}

	void addWearItem (WearItem item) {
		wearDataBase.add (item);
	}

	WearItem addActiveWearItem (byte[] NFCID) {
		for (WearItem item : wearDataBase.getAll ())
			if (Arrays.equals (item.getNFCID (), NFCID)) {
				for (WearItem item1 : activeWearItems)
					if (Arrays.equals (item.getNFCID (), item1.getNFCID ()))
						return null;
				activeWearItems.add (item);
				return item;
			}
		return null;
	}

	public class MyBinder extends Binder {
		MyService getService () {
			return MyService.this;
		}
	}

}
