package com.example.amma.myweatherapp;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double pLat ;
    private double pLong;
    private String mCity;
    private String mTemperature;
    private double mWindSpeed;
    private Marker mMarker;
    private String Sunrise;
    private String Sunset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        pLat = WeatherAppMain.latit;
        pLong = WeatherAppMain.longit;
        mCity = WeatherDataFromJSON.oldCity;
        Sunrise  =WeatherAppMain.formattedTime;
        Sunset = WeatherAppMain.formatTime;
        mTemperature = WeatherDataFromJSON.oldCityTemperature;
        mWindSpeed = WeatherDataFromJSON.oldCityWindSpeed;

        String snippet = "Temperature :"+mTemperature+"°\nWindSpeed :"+mWindSpeed+"\nSunrise :"+Sunrise+"\nSunset :"+Sunset;
        //Toast.makeText(getApplicationContext(),"Latitude :"+pLat+" Longitude"+pLong,Toast.LENGTH_SHORT).show();
        LatLng currentLoc = new LatLng(pLat, pLong);

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        MarkerOptions options = new MarkerOptions()
                .position(currentLoc)
                .title(mCity)
                .snippet(snippet);
        mMap.addMarker(options).showInfoWindow();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,14));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}
