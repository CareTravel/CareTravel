package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class path extends AppCompatActivity {
    private GoogleMap map;
    Button searchButton;
    EditText searchbar;
    String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        Log.d("phj","지도 방 들어옴");
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);
        Log.d("phj", "지도 방이름 " + roomName);

        searchButton = findViewById(R.id.search_button);
        searchbar = findViewById(R.id.search_bar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        //mapFragment.getMapAsync(path.this);
        try {
            mapFragment.getMapAsync(map.class.newInstance());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }

        //저장하는 표 만들어서 저장하기, 일수 추가 등등
    }

}