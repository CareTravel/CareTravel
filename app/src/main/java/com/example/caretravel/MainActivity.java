package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.caretravel.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
    //커밋!
    @Override
    protected void onStart() {
        super.onStart();
    }
}