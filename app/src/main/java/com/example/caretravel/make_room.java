package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.caretravel.databinding.ActivityMainBinding;

public class make_room extends AppCompatActivity {
    public ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}