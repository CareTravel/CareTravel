package com.example.caretravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMaterialBinding;

public class material extends AppCompatActivity {


    public ActivityMaterialBinding binding;
    AlertDialog nameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 뒤로가기 버튼
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(material.this, home.class));
            }
        });

        // 인원 추가 버튼
        binding.materialAdd.setOnClickListener(view -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.name_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(dialogView);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(dialog==nameDialog && which==DialogInterface.BUTTON_POSITIVE){
                        EditText name = nameDialog.findViewById(R.id.list_name);
                        String listName = name.getText().toString();
                        nameData(listName);
                        addButton(listName);
                    }
                }
            });
            builder.setNegativeButton("취소", null);
            nameDialog = builder.create();
            nameDialog.show();
        });
    }
    private void nameData(String name) {

    }
    private void addButton(String name){
        LinearLayout view = findViewById(R.id.linearlayout);
        Button listButton = new Button(material.this);
        listButton.setText(name + "'s list");
        listButton.setTextSize(30);
        listButton.setHeight(300);
        view.addView(listButton);
    }
}