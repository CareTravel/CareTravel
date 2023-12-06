package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.caretravel.databinding.ActivityHomeBinding;
import com.example.caretravel.databinding.ActivityMainBinding;

public class home extends AppCompatActivity {
    private String roomName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent receivedIntent = getIntent();
        roomName = receivedIntent.getStringExtra("name");
        Log.d("phj","홈화면에서 방이름"+roomName);

        if(roomName != null){
            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("currentRoomName", roomName);
            editor.apply();
        }

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { startActivity(new Intent(home.this,activity_register.class));}
        });

        binding.myInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,myPage.class));
            }
        });

        binding.travelInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { startActivity(new Intent(home.this,activity_travel_info.class));}
        });

        //위 까지는 다른 설정 필요없음. 기능으로 갈때는 서버 연결해서 가야함.

        binding.materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, material.class);
                intent.putExtra("name",roomName);
                startActivity(intent);
                finish();
            }
        });

        binding.planButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, make_Plan.class);
                intent.putExtra("name",roomName);
                startActivity(intent);
                finish();
            }
        });

        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, calculation.class);
                intent.putExtra("name",roomName);
                startActivity(intent);
                finish();
            }
        });

        binding.pathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, path.class);
                intent.putExtra("name",roomName);
                startActivity(intent);
                finish();
            }
        });

    }

}