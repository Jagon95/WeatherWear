package com.yobaprojects.weatherwear.city_settings;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yobaprojects.weatherwear.MainActivity;
import com.yobaprojects.weatherwear.R;

import java.util.ArrayList;
import java.util.Arrays;


public class ShowWeatherActivity extends ActionBarActivity {

    private ArrayList<String> weatherStat;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_search_complete);

        String city;

        city = getIntent().getExtras().getString("cityName");

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText("Выбранный город: " + city);

        if (city.equals("Санкт-Петербург")){
            //экземпляр элемента ListView
            ListView lv = (ListView) findViewById(R.id.weatherListView);
            JsonW jSONReader = new JsonW(getAssets(), "json/WeatherData.json");
            //final String[] citiesInList = jSONReader.GetAll();
            weatherStat = new ArrayList<>(Arrays.asList(jSONReader.GetWeatherData()));
            // используем адаптер данных
            adapter = new ArrayAdapter<> (this, android.R.layout.simple_list_item_1, weatherStat);
            lv.setAdapter(adapter);
        }
        else {
            TextView textViewError = (TextView)findViewById(R.id.textView2);
            textViewError.setText("Для города " + city + " нет данных");
            Toast.makeText(getApplicationContext(), ("Ошибка"),
                    Toast.LENGTH_LONG).show();
        }

        }


	public void onClickBackToMain (View view) {
		startActivity (new Intent (getApplicationContext (), MainActivity.class));
	}
}
