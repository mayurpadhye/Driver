package com.ccube9.driver.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by ABHISHEK on 8/17/2017.
 */

public class DriverLatLangService extends Service {

    LocationManager lm ;
    LocationListener lListener;
    @Override
    public void onCreate() {

        super.onCreate();

        getLocation();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
    public void getLocation() {
        boolean status=checkPermission();
        if (status == false) {

           Log.d("sdsd","Permission Required");
        }
        else{
            lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            lListener = new mylocationlistener();

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lListener);

        }
    }
private class mylocationlistener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {


        if (location != null) {
            final double lat= Double.parseDouble(String.valueOf(location.getLatitude()));
            final double lng= Double.parseDouble(String.valueOf(location.getLongitude()));


            Log.d("latfgfdgfdg", String.valueOf(lat));
            Log.d("latfgfdgfdg", String.valueOf(lng));



        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}

    private boolean checkPermission() {
        int result = (ActivityCompat.checkSelfPermission(DriverLatLangService.this, Manifest.permission.ACCESS_FINE_LOCATION));
        int result2 = (ActivityCompat.checkSelfPermission(DriverLatLangService.this, Manifest.permission.ACCESS_COARSE_LOCATION));
        int result3 = (ActivityCompat.checkSelfPermission(DriverLatLangService.this, Manifest.permission.READ_EXTERNAL_STORAGE));


        if (result == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED ) {
            return true;
        } else {
            return false;
        }
    }

}
