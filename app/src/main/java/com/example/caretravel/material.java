package com.example.caretravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMaterialBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class material extends AppCompatActivity {
    private FirebaseFirestore db;
    public ActivityMaterialBinding binding;
    AlertDialog nameDialog;
    private String roomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 입장한 방 이름 가져오기
        Intent intent = getIntent();
        roomName = intent.getStringExtra("name");

        // 데이터 베이스
        initializeCloudFirestore();

        // 먼저 등록한 버튼 출력
        loadData(roomName);

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
                        String userName = name.getText().toString();
                        addData(roomName, userName);
                        addButton(userName);
                    }
                }
            });
            builder.setNegativeButton("취소", null);
            nameDialog = builder.create();
            nameDialog.show();
        });
    }
    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    // 데이터 저장
    private void addData(String roomName, String userName) {
        Map<String, Object> listname = new HashMap<>();
        listname.put("name", userName);

        // 데이터 베이스에 저장
        db.collection("rooms").document(roomName).collection("준비물").document(userName)
                .set(listname)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("scr", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("scr", "Error writing document", e);
                    }
                });

    }

    // 데이터 가져오는 메소드
    private void loadData(String roomName){
        db.collection("rooms")
                .document(roomName)
                .collection("준비물")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String userName = document.getString("name");
                                addButton(userName);
                                Log.d("scr", "리스트를 모두 불러왔습니다.");
                            }
                        } else {
                            Log.d("scr", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // 버튼 추가
    private void addButton(String userName){
        LinearLayout view = findViewById(R.id.linearlayout);
        Button listButton = new Button(material.this);
        listButton.setText(userName + "'s list");
        listButton.setTextSize(30);
        listButton.setHeight(300);
        view.addView(listButton);

        // 개인 리스트 화면으로 이동
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(material.this, material_list.class);
                intent.putExtra("userName", userName);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }
        });
    }
}