package com.example.caretravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMaterialListBinding;

public class material_list extends AppCompatActivity {
    ActivityMaterialListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 이름 가져오기
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        binding.mName.setText(name + "'s list");

        EditText material_Edit = findViewById(R.id.new_material);
        String material = material_Edit.getText().toString();


        // 뒤로가기 버튼
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(material_list.this, material.class));
            }
        });

    }
//    private void addRow(){
//        Log.d("scr", "addNewRow");
//        TableLayout view = findViewById(R.id.tablelayout);
//        TableRow row = findViewById(R.id.plus_material);
//        TableRow newRow = new TableRow(material_list.this);
//        newRow = row;
//        view.addView(newRow);
//
//    }
}