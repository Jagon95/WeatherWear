package com.yobaprojects.weatherwear.wear_adder;

import android.content.res.AssetManager;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

public class ColorName {

    private JSONArray colors;
    ColorName (AssetManager assetManager, String filePath) {
        try (Reader f = new InputStreamReader(assetManager.open(filePath))) {
            JSONParser parser = new JSONParser();
            colors = (JSONArray) parser.parse(f);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private String findName (final long color, final int genus)  {
        Iterator<JSONObject> it = colors.iterator();
        JSONObject innerObj;
        while (it.hasNext()) {
            innerObj = (JSONObject) it.next ();
            if (innerObj.get("value").equals(color))
                return (String) ((JSONArray) innerObj.get ("names")).get (genus);
        }
        //TODO: throw ("нихера не найдено")
        return "";
    }

    public String GetName (final int color) {
        return GetName(color, 0);
    }

    public String GetName (final int color, final int genus) {
        try {
            return findName (RoundColor (color), genus);
        } catch (Exception e) {			//TODO: уточнить эксэпшены
            //e.printStackTrace(arg0); 	//TODO: воткнуть поток для логирования
            //e.printStackTrace();
            return e.toString();
        }

        //return "";
    }

    public static int RoundColor (final int color) {
        return RoundColor (color, 1);
    }

    private static int RoundColor (int color, final int er) {
        //TODO: запилить округление для альфа-канала
        color &= 0x00ffffff;
        int res = (int) Math.round((double) (color >> (16 - er)) / 0xff) * 0xff << (16 - er) & 0xff0000;
        res += (int) Math.round((double) ((color & 0x00ffff) >> (8 - er)) / 0xff) * 0xff << (8 - er) & 0x00ff00;
        res += (int) Math.round((double) ((color & 0x0000ff) << er) / 0xff) * 0xff >> er;
        return res;
    }

    public static int shadowComputing (int color) {
//        int shadowColor;
        int red = (color & 0x00ff0000) >> 16;
        int green = (color & 0x0000ff00) >> 8;
        int blue = color & 0x000000ff;


        int alpha = (int) (Math.sqrt(Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2)) / 1.7320508);        //1.7320508 -- sqrt (0xFF**2*3) / 0xFF

        Log.i ("COLOR", "Alpha: " + alpha);
        Log.i ("COLOR", "Red: " + red);
        Log.i ("COLOR", "Green: " + green);
        Log.i ("COLOR", "Blue: " + blue);

        red = red >> 1 << 16; //red/2
        green = green >> 1 << 8;   //green/2
        blue >>= 1;   //blue/2
        return ((alpha > 0x80) ? alpha << 24 : 0) + red + green + blue;
    }
}
