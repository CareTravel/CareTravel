package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;


import android. app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import com.example.caretravel.databinding.ActivityMainBinding;

import com.example.caretravel.databinding.ActivityMakeRoomBinding;
import java.util.Calendar;

public class make_room extends AppCompatActivity {
    public ActivityMakeRoomBinding binding;


    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.calender1.setOnClickListener(view -> {

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayofMonth) {
                    date_picker_actions1.this.final(year + ": " + (monthOfYear + 1) + ":" + dayofMonth);
                }
            }, year, month, day);
            dateDialog.show();
        });

        binding.calender2.setOnClickListener(view -> {

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayofMonth) {
                    date_picker_actions1.text(year + ": " + (monthOfYear + 1) + ":" + dayofMonth);
                }
            }, year, month, day);
            dateDialog.show();
        });
    }
}

