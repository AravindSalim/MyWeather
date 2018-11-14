package com.example.amma.myweatherapp;

import android.util.Log;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class WeatherDataFromJSON {


    public static String mTemperature,oldCityTemperature;
    public static String mCity,oldCity;
    private static String mIconName;
    private static String mCondition;
    private static String oldCityIconName;
    public static double mWindSpeed,oldCityWindSpeed;
    private static int count = 0;
    private static int mSunrise,oldSunrise;
    private static int mSunset,oldSunset;



    public static WeatherDataFromJSON fromJson(JSONObject jsonObject) {


        try {

            WeatherDataFromJSON weatherData = new WeatherDataFromJSON();
            weatherData.mCity = jsonObject.getString("name");
            WeatherDataFromJSON.mSunrise = jsonObject.getJSONObject( "sys" ).getInt( "sunrise" );
            WeatherDataFromJSON.mSunset = jsonObject.getJSONObject( "sys" ).getInt( "sunset" );
            weatherData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
            WeatherDataFromJSON.mIconName = updateWeatherIcon(weatherData.mCondition);
            weatherData.mWindSpeed = jsonObject.getJSONObject("wind").getDouble("speed");
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempResult);
            weatherData.mTemperature = Integer.toString(roundedValue);

            if(count==0){
                passOldCityDetails();
                passOldCityIconWind();
                count++;
            }
            return weatherData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(String condition) {

        Log.d("id","id "+condition);

        switch (condition) {

            case "01n" : return "o1n";

            case "01d" : return "o1d";
            case "02n" : return "o2n";
            case "02d" : return "o2d";
            case "03n" : return "o3n";
            case "03d" : return "o3d";
            case "04n" : return "o4n";
            case "04d" : return "o4d";
            case "09n" : return "o9n";
            case "09d" : return "o9d";
            case "10n" : return "tend";
            case "10d" : return "tend";
            case "11n" : return "o11n";
            case "11d" : return "o11d";
            case "13n" : return "thirteenn";
            case "13d" : return "thirteend";
            case "15n" : return "015n";
            case "15d" : return "o15d";
            case "50n" : return "fiftyd";
            case "50d" : return "fiftyd";
            default: return "cantfind";



        }



    }

    // Getter methods for temperature, city, and icon name:
    public static void passOldCityIconWind(){
        //oldCityCondition = mCondition;
        oldCityIconName = mIconName;
    }

    public String getTemperature() {
        return mTemperature+"°";
    }

    public String getCity() {
        return mCity;
    }

    public static void passOldCityDetails(){

        oldCity = mCity;
        oldCityTemperature = mTemperature;
        oldCityWindSpeed = mWindSpeed;
        oldSunrise = mSunrise;
        oldSunset = mSunset;
        Log.d( "sun","sun "+oldSunrise );
        Log.d( "sun","sun "+oldSunset );

    }
    public String getOldCity() {

        return oldCity;
    }
    public String getOldTemperature() {

        return oldCityTemperature+"°";

    }
    public double getOldWindSpeed() {

        return oldCityWindSpeed;
    }
    public String getIconName() {
        Log.d("id","id "+mIconName);
        return mIconName;
    }

    public double getmWindSpeed() {
        return mWindSpeed;
    }
    public int getOldSunrise(){
        return oldSunrise;
    }
    public int getOldSunset(){
        return oldSunset;
    }

    //FUTURE USE

   /* public int getmCondition(){
        return mCondition;
    }*/
    public String getOldCityIconName(){
        return oldCityIconName;
    }
}
