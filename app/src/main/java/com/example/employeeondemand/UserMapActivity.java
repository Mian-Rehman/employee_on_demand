package com.example.employeeondemand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.employeeondemand.Activities.UserProfileActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserMapActivity extends AppCompatActivity {

    String ID;
    String img;
    String lon;
    String lat;

    Button btn_map_uplaod;

    //initilize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    double currentLat = 0, currentLong = 0;
    GoogleMap map;


    FirebaseAuth mAuth;
    String userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        btn_map_uplaod=findViewById(R.id.btn_map_uplaod);

//        mAuth = FirebaseAuth.getInstance();
//        userUID = mAuth.getUid();
//
//        if (userUID!=null)
//        {
//            btn_map_uplaod.setEnabled(true);
//        }

        //Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //initilize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        //check permission
        if (ActivityCompat.checkSelfPermission(UserMapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            //when permission granted
            //Call Method
            getCurrentLocation();

        }
        else
        {
            //when permission denied
            //requst permission
            ActivityCompat.requestPermissions(UserMapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }


        btn_map_uplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("userLocation");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String,String> map = new HashMap<>();
                        map.put("userLatitude",lat);
                        map.put("userLongitude",lon);
                        map.put("userUID",userUID);
                        reference.child(userUID).setValue(map);

                        Intent backIntent = new Intent(UserMapActivity.this, UserProfileActivity.class);
                        startActivity(backIntent);
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void getCurrentLocation() {

        //initilize task location
        @SuppressLint("MissingPermission")
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                //when Success
                if (location != null)
                {

                    //when location in not equal to null
                    //Get current Latitude
                    currentLat = location.getLatitude();

                    //get Current Longitude
                    currentLong = location.getLongitude();


                    //synMap
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            //when map is ready

                            map = googleMap;


                            //    Just show current location
                            //initilize lat lag
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            //craete marker option
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("i am there");

                            //zoom map
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat,currentLong),10));

                            //add marker on map
                            googleMap.addMarker(options);

                            lon=String.valueOf(currentLong);
                            lat=String.valueOf(currentLat);

                        }
                    });
                }
            }
        });


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //when permission granted
                //call method
                getCurrentLocation();
            }
        }
    }
}