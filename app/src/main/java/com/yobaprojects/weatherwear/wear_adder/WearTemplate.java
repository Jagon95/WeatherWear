package com.yobaprojects.weatherwear.wear_adder;

import android.content.res.AssetManager;
import android.util.Log;

import com.yobaprojects.weatherwear.wear_item.Layer;
import com.yobaprojects.weatherwear.wear_item.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class WearTemplate implements Serializable {
    public final static int EMPTY_GENUS = -1;
    private final String name;
    private final int genus;		//род, в лингвистическом смысле
    private final Layer layer;
    private final Level level;
    private final char icon;

    static JSONArray templates;
    static AssetManager assetManager;

    private static WearTemplate allElem [];
    public static WearTemplate [] GetAll () {
        return allElem;
    }
    public static WearTemplate [] GetAll (AssetManager am, String filePath) {
        assetManager = am;
        if (templates == null) {
            try (Reader f = new InputStreamReader(assetManager.open(filePath))) {
                JSONParser parser = new JSONParser();
                templates = (JSONArray) parser.parse(f);
            } catch (ParseException | IOException e) {
                Log.e (Thread.currentThread().getStackTrace()[1].getClass ().getSimpleName (), e.getLocalizedMessage ());
            }
        }

        if (allElem == null) {
            allElem = new WearTemplate [templates.size()];
            //TODO: нарыть JSON-библиотеку, которая могла бы парсить-читаться потоком. Так круче
            Iterator<JSONObject> it = templates.iterator();

            int count = 0;
            while (it.hasNext()) {
                allElem [count++] = new WearTemplate(it.next ());
            }
        }
        return allElem;
    }

    public static WearTemplate [] findByName (String name) {
        ArrayList <WearTemplate> elements = new ArrayList<>();
        for (WearTemplate elem : allElem) {
            if (elem.getName().toLowerCase().contains(name.toLowerCase()))
                elements.add(elem);
        }
        return elements.toArray(new WearTemplate [0]);
    }

    protected WearTemplate (final JSONObject obj) {
        icon = (char) ((Long) obj.get("icon")).intValue();
        name = (String) obj.get("name");
        layer = Layer.valueOf(obj.get("layer").toString());
        level = Level.valueOf(obj.get("level").toString());

        Object tmp;

        tmp = obj.get("genus");
        if (tmp != null)
            genus = ((Long) tmp).intValue();
	    else
	        genus = EMPTY_GENUS;
    }


    public char getIcon() {
        return icon;
    }

    public String getName() {return name;}

    public int getGenus() {
        return genus;
    }
    public Layer getLayer() {
        return layer;
    }
    public Level getLevel() {
        return level;
    }
}
