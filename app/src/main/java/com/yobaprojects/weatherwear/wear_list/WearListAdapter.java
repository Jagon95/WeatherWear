package com.yobaprojects.weatherwear.wear_list;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.yobaprojects.weatherwear.R;
import com.yobaprojects.weatherwear.wear_adder.ColorName;
import com.yobaprojects.weatherwear.wear_item.WearItem;

import java.util.ArrayList;

class WearListAdapter extends ArrayAdapter<WearItem> {
//	private ArrayList <WearItem> wearItems;
	private final Context context;

	WearListAdapter (Context context, ArrayList <WearItem> wearItems) {
		super(context, R.layout.layout_wear_item, wearItems);
		this.context = context;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		//потенциальная возможность ошибки тут ↓
		LayoutInflater layoutInflater = LayoutInflater.from (context);
		Typeface iconsFont = Typeface.createFromAsset(context.getAssets (), "fonts/hallheroes.ttf");

		WearItem item = this.getItem (position);

		View rowView = layoutInflater.inflate (R.layout.layout_wear_item, parent, false);
		TextView wearName = (TextView) rowView.findViewById (R.id.name_item);
		Button wearIcon = (Button) rowView.findViewById (R.id.wear_icon);
		TextView layerTextView = (TextView) rowView.findViewById (R.id.layer);
		TextView levelTextView = (TextView) rowView.findViewById (R.id.level);

		wearName.setText (item.getName ());
		wearIcon.setText (Character.toString (item.getIcon ()));
		wearIcon.setTypeface (iconsFont);
		wearIcon.setTextColor (item.getColor ());
		wearIcon.setShadowLayer(5, 0, 0, ColorName.shadowComputing (item.getColor ()));


		Log.i ("WEAR_LIST", "layer_" + item.getLayer ().toString ());

		//тут левой пяткой чешу правое ухо. Нужно найти более красивое решение
		String layerName = context.getString (context.getResources ().getIdentifier ("layer_" + item.getLayer ().toString (), "string", context.getPackageName ()));
		String levelName = context.getString (context.getResources ().getIdentifier ("level_" + item.getLevel ().toString (), "string", context.getPackageName ()));

		layerTextView.setText (layerTextView.getText ().toString () + ": " + layerName);
		levelTextView.setText (levelTextView.getText ().toString () + ": " + levelName);

		return rowView;
	}
}
