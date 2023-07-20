package com.example.gps_semestralka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    TextView tv_lat, tv_lon, tv_altitude, tv_speed, tv_accuracy, tv_address;
    EditText et_description;
    Button btn_addNewCoordinates, btn_showCoordinates;
    DBHelper DB;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = new DBHelper(this);

        tv_lat = (TextView) findViewById(R.id.tv_lat);
        tv_lon = (TextView) findViewById(R.id.tv_lon);
        tv_altitude = (TextView) findViewById(R.id.tv_altitude);
        tv_speed = (TextView) findViewById(R.id.tv_speed);
        tv_accuracy = (TextView) findViewById(R.id.tv_accuracy);
        //tv_address = (TextView) findViewById(R.id.tv_address);
        btn_addNewCoordinates = (Button) findViewById(R.id.btn_addNewCoordinates);
        btn_showCoordinates = (Button) findViewById(R.id.btn_showCoordinates);
        et_description = (EditText) findViewById(R.id.et_description);

        btn_addNewCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Button", "add new coordinates");
                Boolean checkInsertData;
                if (TextUtils.isEmpty(et_description.getText())){
                    checkInsertData = DB.insertUserData(tv_lat.getText().toString(), tv_lon.getText().toString(),
                            tv_altitude.getText().toString(), getString(R.string.locationName_default));
                }else {
                    checkInsertData = DB.insertUserData(tv_lat.getText().toString(), tv_lon.getText().toString(), tv_altitude.getText().toString(), et_description.getText().toString());
                }
                if (checkInsertData){
                    Toast.makeText(MainActivity.this, getString(R.string.savedToDB), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, getString(R.string.failedToSave), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_showCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Button", "show coordinates");
                Intent i = new Intent(MainActivity.this, ShowSavedCoordinates.class);
                startActivity(i);
            }
        });

        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateUI(locationResult.getLastLocation());
            }
        };
        startUpdate();

        printGPSCoordinates();
    }

    public void startUpdate() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @SuppressLint("MissingPermission")
    public void printGPSCoordinates(){
        if (checkPermissions()){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUI(location);
                }
            });
        }else{
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void updateUI(Location location){
        tv_lat.setText(location.getLatitude() + "");
        tv_lon.setText(location.getLongitude() + "");
        tv_accuracy.setText(location.getAccuracy() + "");
        if(location.hasAltitude()){
            String alt = String.format("%.02f", Double.parseDouble(String.valueOf(location.getAltitude())));
            tv_altitude.setText(alt + "");
        }else{
            tv_altitude.setText(getString(R.string.notAvailable));
        }
        if(location.hasSpeed()){
            tv_speed.setText(location.getSpeed() + "");
        }else{
            tv_speed.setText(getString(R.string.notAvailable));
        }
    }

    public boolean checkPermissions(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        DB.close();
        super.onDestroy();
    }
}