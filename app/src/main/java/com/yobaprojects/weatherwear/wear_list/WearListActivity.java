package com.yobaprojects.weatherwear.wear_list;

import android.app.ListActivity;
import android.os.Bundle;

import com.yobaprojects.weatherwear.wear_item.WearItem;

import java.util.ArrayList;


public class WearListActivity extends ListActivity {


	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate (savedInstanceState);
		//setContentView (R.layout.activity_wear_list);

		ArrayList <WearItem> wearItems = null;

		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			if (bundle.containsKey("wearList")) {
				wearItems = (ArrayList<WearItem>) bundle.getSerializable ("wearList");
				WearListAdapter adapter = new WearListAdapter (this, wearItems);
				setListAdapter (adapter);
			}


	}
}
