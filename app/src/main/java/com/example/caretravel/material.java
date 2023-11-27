package com.example.caretravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMaterialBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class material extends AppCompatActivity {
    ActivityMaterialBinding binding;
    AlertDialog nameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

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
                    }
                }
            });
            builder.setNegativeButton("취소", null);

        });
    }
    private void nameData(String name){
        FirebaseFirestore db = FirebaseFirestore.getInstance();


    }
}