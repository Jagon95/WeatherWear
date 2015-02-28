package com.yobaprojects.weatherwear;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yobaprojects.weatherwear.wear_adder.ColorName;
import com.yobaprojects.weatherwear.wear_item.Level;
import com.yobaprojects.weatherwear.wear_item.WearItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jagon on 23.02.2015.
 */
public class WearExpandableListAdapter extends BaseExpandableListAdapter {
	private ArrayList<WearItem> wearItems;
	private Context context;
	private	HashMap <Level, ArrayList <WearItem>> levelArrayListHashMap;
	private Level [] levels;

	public WearExpandableListAdapter ( Context context, ArrayList<WearItem> wearItems) {
		this.wearItems = wearItems;
		this.context = context;

		levelArrayListHashMap = new HashMap<> (Level.values ().length);

		levels = new Level[Level.values ().length];

		for (int i = 0; i < levels.length; i++) {
			levels [i] = Level.values () [i];
			levelArrayListHashMap.put (levels [i], new ArrayList<WearItem> ());
		}

		for (WearItem item : wearItems)
			levelArrayListHashMap.get (item.getLevel ()).add (item);
	}


	@Override
	public void notifyDataSetChanged () {
		for (ArrayList <WearItem> items : levelArrayListHashMap.values ())
			items.clear ();

		for (WearItem item : wearItems)
			levelArrayListHashMap.get (item.getLevel ()).add (item);
		super.notifyDataSetChanged ();
	}

	@Override
	public View getChildView (int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//		return super.getChildView (groupPosition, childPosition, isLastChild, convertView, parent);
//		return new View (context);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate (R.layout.layout_wear_item2, null);
		}

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

		return convertView;
	}

	@Override
	public boolean isChildSelectable (int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public int getGroupCount () {
		return levelArrayListHashMap.size ();
	}

	@Override
	public int getChildrenCount (int groupPosition) {
		return levelArrayListHashMap.get (levels [groupPosition]).size ();
	}

	@Override
	public Object getGroup (int groupPosition) {
		return levelArrayListHashMap.get (levels [groupPosition]);
	}

	@Override
	public Object getChild (int groupPosition, int childPosition) {
		return levelArrayListHashMap.get (levels [groupPosition]).get (childPosition);
	}

	@Override
	public long getGroupId (int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId (int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds () {
		return false;
	}

	@Override
	public View getGroupView (int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.wear_group_view, null);

		TextView levelGroupName = (TextView) convertView.findViewById(R.id.textGroupName);
		levelGroupName.setText (levels[groupPosition].toString ());

		float groupThermalInsulation = 0;
		for (WearItem item : levelArrayListHashMap.get (levels [groupPosition]))
			groupThermalInsulation += item.getThermalInsulation ();

		TextView thermalInsulationGroupValueView = (TextView) convertView.findViewById (R.id.thermalInsulationGroupValue);
		thermalInsulationGroupValueView.setText (Float.toString (groupThermalInsulation));

		return convertView;

	}
}
