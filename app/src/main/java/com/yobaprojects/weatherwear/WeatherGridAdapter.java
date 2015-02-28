package com.yobaprojects.weatherwear;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by Jagon on 28.02.2015.
 */
public class WeatherGridAdapter extends BaseAdapter {
	private ArrayList <Bundle> weatherForecast;
	private Context context;

	public WeatherGridAdapter (Context context, ArrayList<Bundle> weatherForecast) {
		this.weatherForecast = weatherForecast;
		this.context = context;
	}

	@Override
	public int getCount () {
		return weatherForecast.size ();
	}

	@Override
	public Object getItem (int position) {
		return null;
	}

	@Override
	public long getItemId (int position) {
		return position;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {


		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate (R.layout.layout_weather_cell, null);
		}

		TextView timeTextView = (TextView) convertView.findViewById (R.id.time);


		long time = weatherForecast.get (position).getLong ("time");
		Date date = new Date(time);
		Format format = new SimpleDateFormat ("HH:mm");
		timeTextView.setText (format.format(date));
/*
		WearItem item = levelArrayListHashMap.get (levels [groupPosition]).get (childPosition);
		Typeface iconsFont = Typeface.createFromAsset(context.getAssets (), "fonts/hallheroes.ttf");

		TextView wearName = (TextView) convertView.findViewById (R.id.name_item);
		Button wearIcon = (Button) convertView.findViewById (R.id.wear_icon);
		TextView layerValueTextView = (TextView) convertView.findViewById (R.id.layer_value);

		String layerName = context.getString (context.getResources ().getIdentifier ("layer_" + item.getLayer ().toString (), "string", context.getPackageName ()));

		layerValueTextView.setText (layerName);
		wearName.setText (item.getName ());
		wearIcon.setText (Character.toString (item.getIcon ()));
		wearIcon.setTypeface (iconsFont);
		wearIcon.setTextColor (item.getColor ());
		wearIcon.setShadowLayer(5, 0, 0, ColorName.shadowComputing (item.getColor ()));

		RatingBar thermalInsulation = (RatingBar) convertView.findViewById (R.id.thermalInsulation);
		thermalInsulation.setRating (item.getThermalInsulation ());
*/
		return convertView;
	}
}
