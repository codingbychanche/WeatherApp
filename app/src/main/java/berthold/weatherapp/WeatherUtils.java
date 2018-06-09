package berthold.weatherapp;

/**
 * Weather Utils
 *
 * Created by Berthold on 6/5/18.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherUtils {

    static String   city;
    static String   description;
    static String   icon;
    static float    pressure;
    static float    huminity;
    static float    temprature;
    static float    tempMax;
    static float    tempMin;


    /**
     * Extract weather data from json string
     *
     * @param jsonWeatherData
     * @return
     */
    public static WeatherData extractData(String jsonWeatherData){

        try{
            JSONObject jo=new JSONObject(jsonWeatherData);
            if (jo.has("name")) city=jo.getString("name");

            if (jo.has ("weather")){
                JSONArray joArray=jo.getJSONArray("weather");
                if (joArray.length()>0){
                    JSONObject jsWeather=joArray.getJSONObject(0);
                    if (jsWeather.has("description")) description=jsWeather.getString("description");

                    if (jsWeather.has("icon")) icon=jsWeather.getString("icon");

                }
            }

        } catch (JSONException je){}

        return new WeatherData(city,icon,description,pressure,huminity,temprature,tempMax,tempMin);
    }
}
