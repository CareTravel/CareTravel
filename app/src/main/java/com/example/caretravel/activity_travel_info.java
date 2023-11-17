package com.example.caretravel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityTravelInfoBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class activity_travel_info extends AppCompatActivity {

    private ActivityTravelInfoBinding binding;
    private TextView memberTextView, locationTextView, dateTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTravelInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        memberTextView = binding.member;
        locationTextView = binding.location;
        dateTextView = binding.Date;

        // 파이어스토어에서 데이터 가져오기
        listenerRegistration = db.collection("rooms")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        // 에러 처리
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {

                            String memberValue = document.getString("member");
                            String locationValue = document.getString("location");
                            String startDateValue = document.getString("startDate");
                            String endDateValue = document.getString("endDate");

                            // startDate와 endDate를 합쳐서 dateTextView에 표시
                            String combinedDate = "Start: " + startDateValue + "\nEnd: " + endDateValue;


                            memberTextView.setText(memberValue);
                            locationTextView.setText(locationValue);
                            dateTextView.setText(combinedDate);                        }
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
