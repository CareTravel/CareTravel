package com.example.caretravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityTravelInfoBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class activity_travel_info extends AppCompatActivity {

    private ActivityTravelInfoBinding binding;
    private TextView memberTextView, locationTextView, dateTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistration;
    private String roomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTravelInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // SharedPreferences에서 선택된 방의 이름을 가져옵니다.
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);

        // 뒤로가기 버튼
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_travel_info.this, home.class));
            }
        });

        memberTextView = binding.member;
        locationTextView = binding.location;
        dateTextView = binding.Date;

        // 데이터를 가져오는 메서드 호출
        loadRoomData();
    }

    private void loadRoomData() {
        // 파이어스토어에서 데이터 가져오기
        listenerRegistration = db.collection("rooms")
                .document(roomName)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // 에러 처리
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String memberValue = documentSnapshot.getString("member");
                        String locationValue = documentSnapshot.getString("location");
                        String startDateValue = documentSnapshot.getString("startDate");
                        String endDateValue = documentSnapshot.getString("endDate");

                        // startDate와 endDate를 합쳐서 dateTextView에 표시
                        String combinedDate = "Start: " + startDateValue + "\n End: " + endDateValue;

                        memberTextView.setText(memberValue);
                        locationTextView.setText(locationValue);
                        dateTextView.setText(combinedDate);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 종료될 때 ListenerRegistration을 해제합니다.
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
