package com.yobaprojects.weatherwear.city_settings;


import android.content.res.AssetManager;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;




class JsonW {
    JSONArray jsonArray = null;


    JsonW (AssetManager assetManager, String filePath) {         //конструктор объекта
        //тут читаем массивчик из файлика
        try (Reader reader = new InputStreamReader(assetManager.open(filePath))) {
            JSONParser jsonParser = new JSONParser();
            jsonArray = (JSONArray) jsonParser.parse(reader);
        } catch (Exception e) {
            Log.i("YOBA", e.toString());
        }
    }


    public String [] GetAll () {
        Log.i("YOBA", jsonArray.toString());
        Iterator<JSONObject> iterator = jsonArray.iterator();
        JSONObject obj;
        String [] cities = new String [jsonArray.size()];
        int i = 0;
        while (iterator.hasNext()) {
            obj = iterator.next();
             cities[i++] = (String) obj.get("city");
        }

        return cities;
    }



    public String [] LookingForCity (String citySearchWord) {
        Iterator<JSONObject> iterator = jsonArray.iterator();
        JSONObject obj;
       // String [] SearchCities = new String [jsonArray.size()];
        ArrayList <String> SearchCities = new ArrayList<>();
        //int i = 0;

        while (iterator.hasNext()) {
            obj = iterator.next();
//            SearchCities[i++] = (String) obj.get("city"); - пойду таким способом, если не будет работать
            if (((String) obj.get("city")).toLowerCase().contains(citySearchWord.toLowerCase()))
                SearchCities.add((String) obj.get("city"));

            /*{
                SearchCities[i++] = (String) obj.get("city");
            }*/

        }



        return SearchCities.toArray(new String[0]);
    }

    public String [] GetWeatherData () {
        Iterator<JSONObject> iterator = jsonArray.iterator();
        JSONObject obj;
        String [] weather = new String [(jsonArray.size())*5];
        int i = 0;
        while (iterator.hasNext()) {
            obj = iterator.next();
            weather[i++] = (String) obj.get("date");
            weather[i++] = (String) obj.get("day");
            weather[i++] = obj.get("high") + (" / ") + obj.get("low");
            weather[i++] = (String) obj.get("text");
            weather[i++] = ("");
        }

        return weather;
    }



}

