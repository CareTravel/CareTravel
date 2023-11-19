package com.example.caretravel;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityTravelInfoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_travel_info extends AppCompatActivity {

    private ActivityTravelInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTravelInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView memberTextView = binding.member;
        TextView locationTextView = binding.location;
        TextView dateTextView = binding.Date;


        // 파이어베이스에서 데이터 가져오기
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rooms"); //getReference("저장된 노드이름")
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터가 변경될 때 호출되는 메서드
                if (dataSnapshot.exists()) {
                    // 데이터가 존재하면 값을 가져와서 텍스트 뷰에 설정
                    String memberValue = dataSnapshot.child("member").getValue(String.class);
                    String locationValue = dataSnapshot.child("location").getValue(String.class);
                    String dateValue = dataSnapshot.child("date").getValue(String.class);

                    memberTextView.setText(memberValue);
                    locationTextView.setText(locationValue);
                    dateTextView.setText(dateValue);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터 가져오기를 실패한 경우 호출되는 메서드
            }
        });
    }
}