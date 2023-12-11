package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultOwner;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.caretravel.databinding.ActivityHomeBinding;
import com.example.caretravel.databinding.ActivityPathBinding;
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
    String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        ActivityPathBinding binding = ActivityPathBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("phj","지도 방 들어옴");
        //서버 연결해서 같은 방으로 이어줌
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);
        Log.d("phj", "지도 방이름 " + roomName);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(path.this,home.class));}});


        //저장하는 표 만들어서 저장하기, 일수 추가 등등


//        Bundle result = new Bundle();
//        // 번들 키 값과 전달 할 데이터 입력
//        result.putString("bundleKey", "1일차 같은 누른 버튼");
//        // setFragmentResult 메소드의 리퀘스트 키 값과 전달 할 데이터(번들) 입력
//        getMapsFragmentManager().setFragmentResult("requestKey", result);

        //버튼 누르면 지도 띄우는 코드
        Button regiBtn = findViewById(R.id.path_day);
        regiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MapsFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.path_mapLayout, fragment);
                fragmentTransaction.commit();
            }
        });


    }

//    private FragmentResultOwner getMapsFragmentManager() {
//        return null;
//    }
}