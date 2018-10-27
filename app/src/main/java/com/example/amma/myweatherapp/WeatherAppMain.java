package com.example.amma.myweatherapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;


public class WeatherAppMain extends AppCompatActivity {

    // Constants:
    final int NEW_CITY_CODE = 133  ; // Request code for starting new activity for result callback
    final int REQUEST_CODE = 111;
    final String OpenWeatherMapURL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 100;
    static double latit,longit;
    protected String WindSpeed = "";
    private int Unix_SunriseTime;
    private int Unix_SunsetTime;
    public static String formattedTime;
    public static String formatTime;

    // Set LOCATION_PROVIDER here:
    // WE CAN USE NETWORK PROVIDER INSTEAD FOR GETTING LOCATION INFO FROM TOWERS,WIFI ETC
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;


    // Member Variables:
    boolean mUseLocation = true;
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;
    TextView mWindSpeed;

    //   LocationManager and a LocationListener:
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);


        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        mWindSpeed = (TextView) findViewById(R.id.WindSpeed);
        ImageButton changeCityButton = findViewById(R.id.changeCityButton);
        ImageButton CurrentMap = findViewById(R.id.MapButton);



        //   OnClickListener to the changeCityButton :

        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(WeatherAppMain.this, ChangeCity.class);

                startActivityForResult(myIntent, NEW_CITY_CODE);
            }
        });
    }

    //  onResume() here: life cylce method gets executed just after onCreate and just before user can interact..

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("weather", "onResume() called");
        if(mUseLocation) getWeatherForCurrentLocation();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("weather", "onActivityResult() called");

        if (requestCode == NEW_CITY_CODE) {
            if (resultCode == RESULT_OK) {
                String city = data.getStringExtra("City");
                Log.d("weather", "New city is " + city);

                mUseLocation = false;
                getWeatherForNewCity(city);
            }
        }
    }

    //  getWeatherForNewCity(String city)
    private void getWeatherForNewCity(String  city) {

        RequestParams params = new RequestParams();
        params.put("q",city);
        params.put("appid",APP_ID);
        GetDataFromHtpps(params);
    }


    // getWeatherForCurrentLocation() here:
    private void getWeatherForCurrentLocation() {
        //instance, getsystemservice return object of type object ,we  need a LM so we cast it..
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("weather","onLocationChanged Called");
                //location changed

                longit = location.getLongitude();
                latit = location.getLatitude();

                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                RequestParams params = new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid",APP_ID);
                GetDataFromHtpp(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

                Log.d("weather", "onStatusChanged() Status: " + status);
                Log.d("weather", "2 means AVAILABLE, 1: TEMPORARILY_UNAVAILABLE, 0: OUT_OF_SERVICE");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("weather", "onProviderEnabled: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("weather", "onProviderDisabled(): " + provider);
            }
        };
        //checking permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            return;

        }
        //checking for  updates
        locationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                Log.d("WeatherApp","Permission granted");
                getWeatherForCurrentLocation();
            }
            else {
                Log.d("WeatherApp","No Permission");
            }
        }
    }
//  ASYNC HTTP  GetDataFromHtpp(RequestParams params) here:

    private void GetDataFromHtpp(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(OpenWeatherMapURL,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){

                Log.d("WeatherApp","Sucess "+response.toString());
                WeatherDataFromJSON weatherData = WeatherDataFromJSON.fromJson(response);
                updateUI(weatherData);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e ,JSONObject response){

                Log.e("WeatherAPP","Failed"+e.toString());
                Toast.makeText(WeatherAppMain.this,"Request cant be Processed",Toast.LENGTH_SHORT).show();
                Log.d("WeatherApp", "Status code " + statusCode);
                Log.d("WeatherApp", "JSON FILE " + response.toString());
            }

        });
    }
    private void GetDataFromHtpps(RequestParams params)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(OpenWeatherMapURL,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){

                Log.d("WeatherApp","Sucess "+response.toString());
                WeatherDataFromJSON weatherData = WeatherDataFromJSON.fromJson(response);
                updateNewUI(weatherData);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e ,JSONObject response){

                Log.e("WeatherAPP","Failed"+e.toString());
                Toast.makeText(WeatherAppMain.this,"Request cant be Processed",Toast.LENGTH_SHORT).show();
                Log.d("WeatherApp", "Status code " + statusCode);
                Log.d("WeatherApp", "JSON FILE " + response.toString());
            }

        });
    }


    public void ButtonMap(View view)
    {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

    //  updateUI() :
    private void updateUI(WeatherDataFromJSON weather) {
        mTemperatureLabel.setText(weather.getOldTemperature());
        mCityLabel.setText(weather.getOldCity());
        WindSpeed = Double.toString(weather.getOldWindSpeed()).concat(" m/s");
        mWindSpeed.setText(WindSpeed);
        Unix_SunriseTime = weather.getOldSunrise();
        Date date = new Date(Unix_SunriseTime*1000);
        String hours = String.valueOf( date.getHours() );
        String minutes = "0" + date.getMinutes();
// Seconds part from the timestamp
        String seconds = "0" + date.getSeconds();
        formattedTime = hours + ':' + minutes.substring(1) + ':' + seconds.substring(1);


        Unix_SunsetTime = weather.getOldSunset();
        Date dates = new Date(Unix_SunsetTime*1000);
        String hour = String.valueOf( dates.getHours() );
        String minute = "0" + dates.getMinutes();
// Seconds part from the timestamp
        String second = "0" + dates.getSeconds();
        formatTime = hour + ':' + minute.substring(1) + ':' + second.substring(1);


        // Update the icon based on the resource id of the image in the drawable folder.
        int resourceID = getResources().getIdentifier(weather.getOldCityIconName(), "drawable", getPackageName());
        mWeatherImage.setImageResource(resourceID);
    }
    private void updateNewUI(WeatherDataFromJSON weather) {
        mTemperatureLabel.setText(weather.getTemperature());
        mCityLabel.setText(weather.getCity());
        WindSpeed = Double.toString(weather.getmWindSpeed()).concat(" m/s");
        mWindSpeed.setText(WindSpeed);

        // Update the icon based on the resource id of the image in the drawable folder.
        int resourceID = getResources().getIdentifier(weather.getIconName(), "drawable", getPackageName());
        mWeatherImage.setImageResource(resourceID);
    }

    //Freeing up space

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager !=null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}