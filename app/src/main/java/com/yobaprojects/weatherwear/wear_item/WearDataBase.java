package com.yobaprojects.weatherwear.wear_item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class WearDataBase {

	private static SQLiteOpenHelper openHelper;
	private static ArrayList <WearItem> wearItems;
//	private static boolean dataBaseIsCurrent = false;

	public WearDataBase (Context context) {
		openHelper = new DBHelper (context);
		wearItems = new ArrayList<> ();

		SQLiteDatabase database = openHelper.getReadableDatabase ();

		Cursor cursor = database.query (DBHelper.TABLE_NAME, new String[] {DBHelper.UID, DBHelper.NAME, DBHelper.ICON, DBHelper.LAYER, DBHelper.LEVEL, DBHelper.GENUS, DBHelper.ICON_COLOR}, null, null, null, null, null);

		if (cursor.moveToFirst ()) {

			int idIndex = cursor.getColumnIndex (DBHelper.UID);
			int nameIndex = cursor.getColumnIndex (DBHelper.NAME);
			int genusIndex = cursor.getColumnIndex (DBHelper.GENUS);
			int levelIndex = cursor.getColumnIndex (DBHelper.LEVEL);
			int layerIndex = cursor.getColumnIndex (DBHelper.LAYER);
			int iconIndex = cursor.getColumnIndex (DBHelper.ICON);
			int iconColorIndex = cursor.getColumnIndex (DBHelper.ICON_COLOR);

			WearItem wearItem;

			do {
				wearItem = new WearItem ();
				wearItem.setName (cursor.getString (nameIndex));
				wearItem.setIconColor (cursor.getInt (iconColorIndex));
				wearItem.setIcon (cursor.getString (iconIndex));
				wearItem.setLayer (cursor.getString (layerIndex));
				wearItem.setLevel (cursor.getString (levelIndex));
				wearItem.setGenus (cursor.getInt (genusIndex));

				wearItems.add (wearItem);
			} while (cursor.moveToNext ());
		}

		database.close ();
	}

	public ArrayList <WearItem>  getAll () {
		return wearItems;
	}

	public void add (WearItem wearItem) {
		SQLiteDatabase database = openHelper.getWritableDatabase ();

		ContentValues cv = new ContentValues ();
		cv.put (DBHelper.NAME, wearItem.getName ());
		cv.put (DBHelper.GENUS, wearItem.getGenus ());
//		cv.put (DBHelper.ICON, Character.toString (wearItem.getIcon ()));
		cv.put (DBHelper.ICON, String.valueOf (wearItem.getIcon ()));
		cv.put (DBHelper.ICON_COLOR, wearItem.getColor ());
		cv.put (DBHelper.LAYER, wearItem.getLayer ().toString ());
		cv.put (DBHelper.LEVEL, wearItem.getLevel ().toString ());

		database.insert (DBHelper.TABLE_NAME, null, cv);
		database.close ();

		wearItems.add (wearItem);
	}

	static class DBHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME;
		private static final int dataBaseVersion = 1;
		public static final String TABLE_NAME = "wear_items";
		public static final String UID = "_id";
		public static final String NAME = "name";
		public static final String GENUS = "genus";
		public static final String LEVEL = "level";
		public static final String LAYER = "layer";
		public static final String ICON = "icon";
		public static final String ICON_COLOR = "color";

		//TODO: Писать icon в VARCHAR или типа того
		private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
				UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + GENUS + " INTEGER, " +
				LEVEL + " TEXT, " + LAYER + " TEXT, " + ICON + " TEXT, " + ICON_COLOR + " INTEGER" +
				");";

		static {
			DATABASE_NAME = WearDataBase.class.toString ();
		}

		DBHelper (Context context) {
			super(context, DATABASE_NAME, null, dataBaseVersion);
		}

		@Override
		public void onCreate (SQLiteDatabase db) {
			Log.i (getClass ().toString (), "Create database");
			Log.i (getClass ().toString (), "Command: " + SQL_CREATE_ENTRIES);
			db.execSQL (SQL_CREATE_ENTRIES);
		}

		@Override
		public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
			//TODO: описать onUpgrade
		}
	}
}
