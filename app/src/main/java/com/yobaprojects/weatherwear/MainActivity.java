package com.yobaprojects.weatherwear;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yobaprojects.weatherwear.city_settings.CityListActivity;
import com.yobaprojects.weatherwear.wear_adder.ChooseIconActivity;
import com.yobaprojects.weatherwear.wear_item.WearDataBase;
import com.yobaprojects.weatherwear.wear_item.WearItem;
import com.yobaprojects.weatherwear.wear_list.WearListActivity;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button addNewItem, goToWearList, goToCitysList;
    //private static ArrayList<WearItem> wearItems;// = new ArrayList<>();
	private static WearDataBase wearDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

	    wearDataBase = new WearDataBase (getApplicationContext ());
        addNewItem = (Button) findViewById(R.id.addNewItem);
        addNewItem.setOnClickListener (this);

        goToWearList = (Button) findViewById(R.id.goToWearListActivity);
        goToWearList.setOnClickListener (this);

	    goToCitysList = (Button) findViewById (R.id.goToCityListActivity);
	    goToCitysList.setOnClickListener (this);

//        Toast.makeText(getApplicationContext(), Boolean.toString(savedInstanceState == null), Toast.LENGTH_SHORT).show();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("wearItem")) {
	            //wearItems.add ((WearItem) bundle.getSerializable ("wearItem"));
	            wearDataBase.add ((WearItem) bundle.getSerializable ("wearItem"));
	         //   DB
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClick (View view) {
//        Toast.makeText(getApplicationContext(), "Lol", Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.addNewItem:
                Intent intent = new Intent(MainActivity.this, ChooseIconActivity.class);
                startActivity(intent);
                break;
            case R.id.goToWearListActivity:
	            Intent wearListIntent = new Intent (this, WearListActivity.class);
	            wearListIntent.putExtra ("wearList", wearDataBase.getAll ());
	            startActivity (wearListIntent);
                break;
	        case R.id.goToCityListActivity:
		        Intent cityListIntent = new Intent (this, CityListActivity.class);
		        startActivity (cityListIntent);
		        break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
