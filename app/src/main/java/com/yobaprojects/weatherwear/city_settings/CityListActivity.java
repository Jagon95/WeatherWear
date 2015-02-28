package com.yobaprojects.weatherwear.city_settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yobaprojects.weatherwear.R;

import java.util.ArrayList;
import java.util.Arrays;



public class CityListActivity extends ActionBarActivity {
	private ArrayList<String> cityNames;
	private ArrayAdapter<String> adapter;
	private ProgressBar progressBar;

	public static final String APP_PREFERENCES = "city_save"; //для сохранения данных
	SharedPreferences city_save; //фигачим переманную. Я комментирую, просто во всем этом дерьме потерялся, чтобы заново не перепиливать пишу такие простыни

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		city_save = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE); //инициализируемебучую переменную

		setContentView(R.layout.activity_city_list);

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);

		EditText searchText = (EditText) findViewById(R.id.cityEditText);
		searchText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				//подключаем джейсончик
				JsonW lookingForCity = new JsonW(getAssets(), "json/cities.json");
				//добавляем в cityNames массив
				cityNames.clear ();
				cityNames.addAll(Arrays.asList(lookingForCity.LookingForCity(s.toString())));
				//уведомляем адаптер об изменении
				adapter.notifyDataSetChanged();
			}
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

		} );





		//=================Первоначаальное формирование списка городов==============================

		//экземпляр элемента ListView
		ListView lv = (ListView) findViewById(R.id.cityListView);
		JsonW jSONReader = new JsonW(getAssets(), "json/cities.json");
		//final String[] citiesInList = jSONReader.GetAll();
		cityNames = new ArrayList<>(Arrays.asList(jSONReader.GetAll()));
		// используем адаптер данных
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityNames);
		lv.setAdapter(adapter);

		//==========================================================================================

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View itemClicked, int position,long id) {


				progressBar.setVisibility(View.VISIBLE);

				SharedPreferences.Editor editor = city_save.edit();
				editor.putString (APP_PREFERENCES, ((TextView) itemClicked).getText ().toString ());
				editor.apply();

				//Ну и дальше, по-сути старый кусок кода с проверкой
				Toast.makeText(getApplicationContext(), ("Выбран город " + (city_save.getString(APP_PREFERENCES, ""))),
						Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(CityListActivity.this, SimpleD.class);
				startActivity(intent);
			}
		});


	}


    public void searchButtonClick(View view) {
        EditText cityEditText = (EditText) findViewById(R.id.cityEditText);
        cityEditText.setVisibility(View.VISIBLE);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setVisibility(View.GONE);
    }

}
