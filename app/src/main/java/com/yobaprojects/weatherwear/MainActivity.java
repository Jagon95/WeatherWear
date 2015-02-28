package com.yobaprojects.weatherwear;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.yobaprojects.weatherwear.city_settings.CityListActivity;
import com.yobaprojects.weatherwear.wear_adder.ChooseIconActivity;
import com.yobaprojects.weatherwear.wear_item.WearItem;
import com.yobaprojects.weatherwear.wear_list.WearListActivity;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

	private static MyService myService;
	private static boolean bound = false;
	private static final Queue <Runnable> tasksPool = new ArrayDeque<> ();
	private static final ServiceConnection sConnection = new ServiceConnection () {
		@Override
		public void onServiceConnected (ComponentName name, IBinder service) {
			myService = ((MyService.MyBinder) service).getService ();
			Log.i (LOG_TAG, "onServiceConnected");
			bound = true;
			executeServiceTasks (tasksPool);
		}
		@Override
		public void onServiceDisconnected (ComponentName name) {
			Log.i (LOG_TAG, "onServiceDisconnected");
			bound = false;
		}
	};

	public final static String LOG_TAG = MainActivity.class.getSimpleName () + "_LOG";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

		int [] buttonsIds = {R.id.addNewItem, R.id.goToWearListActivity, R.id.goToCityListActivity, R.id.goToStatusActivity};
		for (int id : buttonsIds)
			((LinearLayout) findViewById (id)).setOnClickListener (this);

		
		
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(WearItem.NAME))
	        addNewTask (new UpdateDatabase ((WearItem) bundle.getSerializable (WearItem.NAME)));

		Intent myServiceIntent = new Intent (this, MyService.class);

		startService (myServiceIntent);
		bindService (myServiceIntent, sConnection, 0);
		Log.i (LOG_TAG, "onCreate");
    }

	private class UpdateDatabase implements Runnable {
		WearItem item;

		private UpdateDatabase (WearItem item) {
			this.item = item;
		}

		@Override
		public void run () {
			myService.addWearItem (item);
		}
	}
	
	private class SendWearItemsToIntent implements Runnable {
		Intent intent;

		private SendWearItemsToIntent (Intent intent) {
			this.intent = intent;
		}
		@Override
		public void run () {
			intent.putExtra ("wearList", myService.getWearItems ());
			startActivity (intent);
		}
	}

	public static void executeServiceTasks (Queue <Runnable> tasksPool) {
		Executor executor = Executors.newSingleThreadExecutor ();
		while (!tasksPool.isEmpty ())
			executor.execute (tasksPool.poll ());
	}

	private void addNewTask (Runnable runnable) {
		tasksPool.add (runnable);
		if (bound)
			executeServiceTasks (tasksPool);
	}

	public void onClick (View view) {
        switch (view.getId()) {
            case R.id.addNewItem:
                Intent chooseIconIntent = new Intent(MainActivity.this, ChooseIconActivity.class);
                startActivity(chooseIconIntent);
                break;
            case R.id.goToWearListActivity:
	            Intent wearListIntent = new Intent (this, WearListActivity.class);
	            addNewTask (new SendWearItemsToIntent (wearListIntent));
                break;
	        case R.id.goToCityListActivity:
		        Intent cityListIntent = new Intent (this, CityListActivity.class);
		        startActivity (cityListIntent);
		        break;
	        case R.id.goToStatusActivity:
		        Intent statusIntent = new Intent (this, StatusActivity.class);
		        startActivity (statusIntent);
		        break;
        }
    }

	@Override
	protected void onDestroy () {
		super.onDestroy ();
		unbindService (sConnection);
		bound = false;
	}

	@Override
	public void onBackPressed () {
	}
}
