package com.yobaprojects.weatherwear.city_settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_list);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        EditText searchText = (EditText) findViewById(R.id.cityEditText);
        searchText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                JsonW lookingForCity = new JsonW(getAssets(), "json/cities.json");
               // cityNames = new ArrayList<String>(Arrays.asList(lookingForCity.LookingForCity(s.toString())));
                cityNames.clear();
                cityNames.addAll(Arrays.asList(lookingForCity.LookingForCity(s.toString())));
                // cityNames.add(lookingForCity.LookingForCity(searchText.getText().toString()));
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
        adapter = new ArrayAdapter<> (this, android.R.layout.simple_list_item_1, cityNames);
        lv.setAdapter(adapter);

        //==========================================================================================

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,long id) {


                progressBar.setVisibility(View.VISIBLE);

	            Toast.makeText(getApplicationContext(), ("Выбран город " + ((TextView) itemClicked).getText()),
                        Toast.LENGTH_SHORT).show();

	            Intent intent = new Intent(getApplicationContext (), ShowWeatherActivity.class);
	            intent.putExtra("cityName", ((TextView) itemClicked).getText());
	            progressBar.setVisibility(View.GONE);
	            startActivity(intent);

                        // в ключ username пихаем текст из первого текстового поля

                        //startActivity(new Intent(this, city_full_info.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_city_list, menu);
        return true;
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

    public void searchButtonClick(View view)
    {
        EditText cityEditText = (EditText) findViewById(R.id.cityEditText);
        cityEditText.setVisibility(View.VISIBLE);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setVisibility(View.GONE);


    }

}
