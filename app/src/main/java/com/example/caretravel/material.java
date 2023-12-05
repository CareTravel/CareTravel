package com.example.caretravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMaterialBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class material extends AppCompatActivity {

    public ActivityMaterialBinding binding;
    AlertDialog nameDialog;
    private String roomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // 입장한 방 이름 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("selectedRoomName", null);
        if (roomName == null) {
            // 선택된 방의 이름이 없는 경우, 사용자에게 메시지를 표시하고 다른 화면으로 이동합니다.
            Toast.makeText(this, "선택된 방이 없습니다. 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // 먼저 등록한 버튼 출력
        saveNameToFirestore();

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
                        String nameI = name.getText().toString();
                        saveNameToFirestore(nameI);
                        addButton(nameI);
                    }
                }
            });
            builder.setNegativeButton("취소", null);
            nameDialog = builder.create();
            nameDialog.show();
        });
    }

    // 먼저 등록 되어있는 리스트 가져올 때 방 이름 얻는 메소드
    private void saveNameToFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        getRoomIdFromOtherJavaFile(roomName, roomId -> {
            // roomId를 받아왔을 때 실행되는 코드
            loadData(firestore, roomId);
        });
    }

    // 이름 저장
    private void saveNameToFirestore(String nameI) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        getRoomIdFromOtherJavaFile(roomName, roomId -> {
            // roomId를 받아왔을 때 실행되는 코드
            saveNameToFirestore(firestore, roomId, nameI);
        });
    }
    private void saveNameToFirestore(FirebaseFirestore firestore, String roomId, String nameI) {
        Map<String, Object> listname = new HashMap<>();
        listname.put("name", nameI);

        // 데이터 베이스에 저장
        firestore.collection("rooms").document(roomId).collection("준비물").document(nameI)
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
    private void loadData(FirebaseFirestore firestore, String roomId){
        firestore.collection("rooms")
                .document(roomId)
                .collection("준비물")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                addButton(name);
                                Log.d("scr", "리스트를 모두 불러왔습니다.");
                            }
                        } else {
                            Log.d("scr", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getRoomIdFromOtherJavaFile(String roomName, material.OnRoomIdReceivedListener listener) {
        // Firebase Firestore에 접근하여 방 정보의 다큐먼트 ID를 조회하는 로직 구현
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference roomsCollection = firestore.collection("rooms");

        roomsCollection.whereEqualTo("name", roomName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    String roomId = documentSnapshot.getId();
                    listener.onRoomIdReceived(roomId);  // roomId를 Listener를 통해 반환
                } else {
                    Toast.makeText(material.this, "방 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(material.this, "방 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface OnRoomIdReceivedListener {
        void onRoomIdReceived(String roomId);
    }

    // 버튼 추가
    private void addButton(String name){
        LinearLayout view = findViewById(R.id.linearlayout);
        Button listButton = new Button(material.this);
        listButton.setText(name + "'s list");
        listButton.setTextSize(30);
        listButton.setHeight(300);
        view.addView(listButton);

        // 개인 리스트 화면으로 이동
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(material.this, material_list.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
    }
}