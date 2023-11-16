package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.caretravel.databinding.ActivityRegisterBinding;

public class activity_register extends AppCompatActivity {
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == regDialog && which == DialogInterface.BUTTON_POSITIVE) {
                showToast("(방이름)으로 방이 생성되었습니다.");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.roomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_register.this,make_room.class));
            }
        });

        binding.myInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_register.this,myPage.class));
            }
        });

        binding.regButton.setOnClickListener(view -> {
            //custom dialog를 위한 layout xml 초기화
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.regdialog_layout, null);

            regDialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setPositiveButton("확인", dialogListener)
                    .setNegativeButton("취소", null)
                    .create();

            regDialog.show();
        });
    }
    public void createBtn(View view){
        Button regButton = findViewById(R.id.register_register);

//        if () 서버에서 방 가져오기
    }
}