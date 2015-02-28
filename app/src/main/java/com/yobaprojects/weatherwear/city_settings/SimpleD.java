package com.yobaprojects.weatherwear.city_settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yobaprojects.weatherwear.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SimpleD extends ActionBarActivity {
	public static final String APP_PREFERENCES = "city_save";
	SharedPreferences city_save;

	private ArrayList<String> weatherStat;
	private ArrayAdapter<String> adapter;
	boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_d);

		city_save = getSharedPreferences (APP_PREFERENCES, MODE_PRIVATE);


		String city;
		//получаем значение выбранного города из предыдущего активити
		city = (city_save.getString(APP_PREFERENCES, ""));

		TextView textView = (TextView)findViewById(R.id.textView);
		textView.setText("Выбранный город: " + city);

		//пока, только спб берет. Вообще, в идеале их надо по айди идентифицировать,
		// который тоже есть в джейсончике, но не в этот раз
		if (city.equals("Санкт-Петербург")){
			//экземпляр элемента ListView
			ListView lv = (ListView) findViewById(R.id.weatherListView);
			JsonW jSONReader = new JsonW(getAssets(), "json/WeatherData.json");
			//final String[] citiesInList = jSONReader.GetAll();
			weatherStat = new ArrayList<>(Arrays.asList (jSONReader.GetWeatherData ()));
			// используем адаптер данных
			adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weatherStat);
			lv.setAdapter(adapter);
		}
		else {
			TextView textViewError = (TextView)findViewById(R.id.textView2);
			textViewError.setText("Для города " + city + " нет данных");
			Toast.makeText (getApplicationContext (), ("Ошибка"),
					Toast.LENGTH_LONG).show();
		}

	}


	public void onClickForecast (View v){

		ListView lv = (ListView) findViewById(R.id.weatherListView);
		TextView tw = (TextView) findViewById(R.id.textView2);

		if (flag) {

			lv.setVisibility(View.VISIBLE);
			tw.setVisibility(View.VISIBLE);
			flag = false;
		}
		else{
			lv.setVisibility(View.INVISIBLE);
			tw.setVisibility(View.INVISIBLE);
			flag = true;
		}



	}

	public void onClickNext (View v){
		Intent intent = new Intent(this, SimpleChoose.class);
		startActivity(intent);

	}

	public void onClickPrevious (View v){

		Intent intent = new Intent(this, CityListActivity.class);
		startActivity(intent);

	}
}
