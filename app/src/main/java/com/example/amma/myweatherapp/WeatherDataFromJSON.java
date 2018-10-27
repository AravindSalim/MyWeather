package com.example.amma.myweatherapp;

import android.util.Log;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataFromJSON {


    public static String mTemperature,oldCityTemperature;
    public static String mCity,oldCity;
    private static String mIconName;
    private static int mCondition;
    private static String oldCityIconName;
    private static int oldCityCondition;
    public static double mWindSpeed,oldCityWindSpeed;
    private static int count = 0;
    private static int mSunrise,oldSunrise;
    private static int mSunset,oldSunset;


    DateTime dt = new DateTime();  // current time

    int hours = dt.getHourOfDay(); // gets hour of day


    // Create a WeatherDataFromJSON from a JSON.

    public static WeatherDataFromJSON fromJson(JSONObject jsonObject) {


        try {

            WeatherDataFromJSON weatherData = new WeatherDataFromJSON();
            weatherData.mCity = jsonObject.getString("name");
            WeatherDataFromJSON.mSunrise = jsonObject.getJSONObject( "sys" ).getInt( "sunrise" );
            WeatherDataFromJSON.mSunset = jsonObject.getJSONObject( "sys" ).getInt( "sunset" );
            weatherData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.mIconName = updateWeatherIcon(weatherData.mCondition);
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

    private static String updateWeatherIcon(int condition) {

        DateTime dt = new DateTime();  // current time

        int hours = dt.getHourOfDay(); // gets hour of day


            switch (condition) {

                case 200:
                case 201:
                case 202:
                case 210:
                case 211:
                case 212:
                case 221:
                case 230:
                case 231:
                case 232:
                    return "elevend";
                case 301:
                case 302:
                case 300:
                case 310:
                case 314:
                case 311:
                case 312:
                case 313:
                case 321:
                    return "o9";
                case 500:
                case 501:
                case 502:
                case 503:
                case 504:
                    return "tend";
                case 511:
                    return "thirteend";
                case 520:
                case 521:
                case 522:
                case 531:
                    return "o9d";
                case 600:
                case 601:
                case 602:
                case 611:
                case 612:
                case 615:
                case 616:
                case 620:
                case 621:
                case 622: return "thirteend";
                case 701:
                case 711:
                case 721:
                case 731:
                case 741:
                case 751:
                case 761:
                case 771:
                case 781:
                    return "fiftyd";
                case 800:
                    if(hours >=6 && hours <=18){
                        return "o1d";
                    }
                    else {
                        return "o1n";
                    }
                case 801:
                    if(hours >=6 && hours <=18){
                        return "o2d";
                    }
                    else{
                        return "o2n";
                    }
                case 802:
                    if(hours >=6 && hours <=18){
                        return "o3d";
                    }
                    else{
                        return "o3n";
                    }
                case 803:
                    if(hours >=6 && hours <=18){
                        return "o4d";
                    }
                    else{
                        return "o4n";
                    }
                case 804:
                    if(hours >=6 && hours <=18){
                        return "o4d";
                    }
                    else{
                        return "o4n";
                    }
                    default:
                        return "cantfind";

        }



    }

    // Getter methods for temperature, city, and icon name:
    public static void passOldCityIconWind(){
        oldCityCondition = mCondition;
        oldCityIconName = mIconName;
    }

    public String getTemperature() {
        return mTemperature + "°";
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

        return oldCityTemperature;

    }
    public double getOldWindSpeed() {

        return oldCityWindSpeed;
    }
    public String getIconName() {
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
