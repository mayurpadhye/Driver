package com.ccube9.driver.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccube9.driver.MyCar.MyCarActivity;
import com.ccube9.driver.R;
import com.ccube9.driver.network.BaseUrl;
import com.ccube9.driver.profile.ProfileActivity;
import com.ccube9.driver.registration.ChooseRegistrationRoleActivity;
import com.ccube9.driver.service.DriverLatLangService;
import com.ccube9.driver.util.CustomUtil;
import com.ccube9.driver.util.PrefManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;

import java.util.Map;

public class HomeActivity extends AppCompatActivity implements
        OnMapReadyCallback {
    Toolbar toolbar;
    LinearLayout rl_drawer;
    private Switch sw_takeride;
    ImageView iv_menu, nv_user_profile;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private      LocationRequest locationRequest;
    TextView tv_home,

    //tv_profile,
    //tv_payment,
    tv_ride_history, tv_support, tv_logout, nv_user_mail, tv_profile, tv_mycar;
    StringRequest stringRequest,stringRequest1;
    RequestQueue requestQueue;
    Button btn_become_driver;
    private String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        startService(new Intent(HomeActivity.this, DriverLatLangService.class));
        requestQueue = Volley.newRequestQueue(HomeActivity.this);
        FirebaseApp.initializeApp(this);
        Log.d("hgfhgh", PrefManager.getTakeRideStatus(HomeActivity.this));
        if(PrefManager.getTakeRideStatus(HomeActivity.this).equals("0")) {
            sw_takeride.setChecked(false);
        }
        else if(PrefManager.getTakeRideStatus(HomeActivity.this).equals("1")){
            sw_takeride.setChecked(true);
        }
        if (PrefManager.getCompanyId(HomeActivity.this).equals("null") || PrefManager.getCompanyId(HomeActivity.this).equals("")) {
            tv_mycar.setVisibility(View.GONE);
        }



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        googleApiClient = new GoogleApiClient.Builder(HomeActivity.this)

                .addApi(LocationServices.API).build();

        googleApiClient.connect();

        GPSenableDialog();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
        if (checkPermission() == false) {
            requestPermission();
        } else {
                getLastKnownLocation();
                  getUpdatedLocation();
            if (fusedLocationClient != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        }


        SetDrawer();


        String mailid = PrefManager.getUserEmail(HomeActivity.this);
        String profpic = PrefManager.getProfImg(HomeActivity.this);
        Log.d("dsad", profpic);
        Log.d("dsad", mailid);


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Token=instanceIdResult.getToken();
                Log.d("ASas",Token);
        stringRequest1 = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL.concat("driver/fcm_token"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("gfdgfdgf", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("gfdgfdgf", volleyError.toString());



            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();

                param.put("language", "en");
                param.put("user_type", "2");
                param.put("user_id", PrefManager.getUserId(HomeActivity.this));
                param.put("api_token", PrefManager.getApiToken(HomeActivity.this));
                param.put("fcm_token", Token);
                return param;
            }
        };

        requestQueue.add(stringRequest1);

    }
});

        if (mailid != null) {
            nv_user_mail.setText(mailid);
        }

        if (profpic != null && !profpic.equals("")) {
            Picasso.with(HomeActivity.this).load(BaseUrl.IMAGE_URL.concat(profpic)).fit().into(nv_user_profile);
        } else {
            Picasso.with(HomeActivity.this).load("http://driver.ccube9.com/public/profile/placeholder-profile.jpg").fit().into(nv_user_profile);
        }
        onClick();
        //getSupportFragmentManager().beginTransaction().replace(R.id.main,new MapsActivity()).commit();
//       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//             .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

//        MapFragment mMapFragment = MapFragment.newInstance();
//        FragmentTransaction fragmentTransaction =
//                getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.main, mMapFragment);
//        fragmentTransaction.commit();

    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (checkPermission() == false) {
            requestPermission();
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//            mMap.getUiSettings().setAllGesturesEnabled(true);
//            mMap.getUiSettings().setMapToolbarEnabled(true);
//            mMap.getUiSettings().setCompassEnabled(true);
//            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

    }

    private void onClick() {

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(rl_drawer);
            }
        });
        tv_mycar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, MyCarActivity.class));
            }
        });
        nv_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        tv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                finish();
            }
        });
        tv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                PrefManager.LogOut(HomeActivity.this);
                startActivity(new Intent(HomeActivity.this, ChooseRegistrationRoleActivity.class));
                finish();
            }
        });

        sw_takeride.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final String status;
                if (isChecked) {
                    status = "1";
                } else {
                    status = "0";
                }

                CustomUtil.ShowDialog(HomeActivity.this);
                stringRequest = new StringRequest(Request.Method.POST, BaseUrl.BASE_URL.concat("driver/is_able_to_ride"), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("gfdgfdgf", response);
                        CustomUtil.DismissDialog(HomeActivity.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("1")) {

                                JSONObject object=jsonObject.getJSONObject("data");

                                PrefManager.setTakeRideStatus(HomeActivity.this,object.getString("is_able_to_ride"));
                                Toast.makeText(HomeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").equals("0")) {
                                JSONObject object=jsonObject.getJSONObject("message");
                                JSONArray jsonArray=object.getJSONArray("is_able_to_ride");

                                Toast.makeText(HomeActivity.this, (CharSequence) jsonArray.get(0), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("gfdgfdgf", volleyError.toString());
                        CustomUtil.DismissDialog(HomeActivity.this);
                        requestQueue.cancelAll(stringRequest);
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        if (message != null) {
                            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(HomeActivity.this, "An error occured", Toast.LENGTH_SHORT).show();


                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();

                        param.put("language", "en");
                        param.put("user_type", "2");
                        param.put("user_id", PrefManager.getUserId(HomeActivity.this));
                        param.put("api_token", PrefManager.getApiToken(HomeActivity.this));
                        param.put("is_able_to_ride", status);
                        return param;
                    }
                };

                requestQueue.add(stringRequest);

            }
        });
    }

    private void SetDrawer() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(HomeActivity.this, drawer,
                // Navigation menu toggle icon
                R.string.navigation_drawer_open, // Navigation drawer open description
                R.string.navigation_drawer_close // Navigation drawer close description
        );
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        // setOnGroupClickListener listener for group heading click

    }

    public void closeDrawer() {
        drawer.closeDrawer(Gravity.LEFT);
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_menu = (ImageView) toolbar.findViewById(R.id.iv_menu);
        rl_drawer = findViewById(R.id.rl_drawer);
        nv_user_profile = findViewById(R.id.nv_user_profile);
        nv_user_mail = findViewById(R.id.nv_user_mail);
        tv_home = findViewById(R.id.tv_home);
        sw_takeride = findViewById(R.id.sw_takeride);
        nv_user_profile = findViewById(R.id.nv_user_profile);
        tv_profile = findViewById(R.id.tv_profile);
        tv_mycar = findViewById(R.id.tv_mycar);

        //  tv_payment=findViewById(R.id.tv_payment);
        tv_ride_history = findViewById(R.id.tv_ride_history);
        tv_logout = findViewById(R.id.tv_logout);
        tv_support = findViewById(R.id.tv_support);
    }//initViewClose

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
             ) {
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private boolean checkPermission() {
        int result = (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION));
        int result2 = (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION));
        int result3 = (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE));
        int result4 = (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE));
        if (result == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                      getLastKnownLocation();

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.d("sdsd", String.valueOf(location));
                        if (location != null) {
                            // Logic to handle location object
                            location.getLatitude();
                            location.getLatitude();
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                           // Toast.makeText(HomeActivity.this, "lastloc"+latLng.toString(), Toast.LENGTH_SHORT).show();
                            CameraPosition cp = new CameraPosition.Builder()
                                    .target(latLng) // your initial co-ordinates here. like, LatLng initialLatLng
                                    .zoom(15)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                            Log.d("dsds", String.valueOf(location));
                        }

                        if(location==null){
                            getLastKnownLocation();
                        }


                    }
                });
    }


    private void getUpdatedLocation(){
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        location.getLatitude();
                        location.getLatitude();
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                       // Toast.makeText(HomeActivity.this,"updateloc"+ latLng.toString(), Toast.LENGTH_SHORT).show();
                        CameraPosition cp = new CameraPosition.Builder()
                                .target(latLng) // your initial co-ordinates here. like, LatLng initialLatLng
                                .zoom(15)
                                .build();
                       // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                        Log.d("dsds", String.valueOf(location));
                    }
                }
            }
        };

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {


                GPSenableDialog();
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
                if (checkPermission() == false) {
                    requestPermission();
                } else {
                    getLastKnownLocation();
                    getUpdatedLocation();
                    if (fusedLocationClient != null) {
                        fusedLocationClient.removeLocationUpdates(locationCallback);
                    }
                }



            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(this, "Please enable Location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void GPSenableDialog() {
         locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(HomeActivity.this, 1);

                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);

    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.disconnect();

        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
