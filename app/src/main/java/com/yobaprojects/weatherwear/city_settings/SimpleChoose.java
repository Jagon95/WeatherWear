package com.yobaprojects.weatherwear.city_settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yobaprojects.weatherwear.MainActivity;
import com.yobaprojects.weatherwear.R;

public class SimpleChoose extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

	public static final String APP_PREFERENCES_W = "roomWeather_save"; //для сохранения данных
	SharedPreferences roomWeather_save; //фигачим переманную. Я комментирую, просто во всем этом дерьме потерялся, чтобы заново не перепиливать пишу такие простыни

	SeekBar seekBar;
	boolean flag = true;
	int wValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_choose);

		roomWeather_save = getSharedPreferences(APP_PREFERENCES_W, Context.MODE_PRIVATE); //инициализируемебучую переменную


		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(this);
		int valueTemp = 22;
		TextView textView = (TextView) findViewById(R.id.textView5);
		if (roomWeather_save.contains (APP_PREFERENCES_W)){
			valueTemp = roomWeather_save.getInt (APP_PREFERENCES_W, valueTemp);
			seekBar.setProgress ((valueTemp - 10) * 5);
			textView.setText (String.valueOf(valueTemp));
		} else
			textView.setText("По умолчанию " + String.valueOf(valueTemp));

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,                        //ОБРАБОТКА SEEKBAR
	                              boolean fromUser) {
		TextView textView = (TextView) findViewById(R.id.textView5);
		int valueTemp = progress/5 + 10;
		wValue = valueTemp;
		textView.setText(String.valueOf(valueTemp));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}


	public void onClickHint (View view)
	{
		TextView textView = (TextView) findViewById(R.id.textView4);
		if (flag) {

			textView.setVisibility(View.VISIBLE);
			flag = false;
		}
		else{
			textView.setVisibility(View.INVISIBLE);
			flag = true;
		}
	}

	public void onClickNext (View view) {                           //СОХРАНЕНИЕ ДАННЫХ ОТИМАЛЬНОЙ ТЕМПЕРАТУРЫ
		SharedPreferences.Editor editor = roomWeather_save.edit();
		editor.putInt(APP_PREFERENCES_W, wValue);
		editor.apply();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity (intent);
	}

	public void onClickBack (View view) {
		Intent intent = new Intent(this, CityListActivity.class);
		startActivity(intent);
	}
}
