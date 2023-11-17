package com.example.caretravel;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMakeRoomBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import android.app.DatePickerDialog;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class make_room extends AppCompatActivity {
    private static final String TAG = "make_room";

    private ActivityMakeRoomBinding binding;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);
        selectedDate = Calendar.getInstance();

        binding.calender1.setOnClickListener(v -> showDatePickerDialog());
        binding.calender2.setOnClickListener(v -> showDatePickerDialogForSecondButton());
        binding.registerButton.setOnClickListener(v -> {
            // 사용자가 "방 만들기" 버튼을 클릭했을 때 Firestore에 데이터 추가
            saveDataToFirestore();

            // Firestore에서 데이터를 가져오는 메서드 호출
            fetchDataFromFirestore();
        });
    }
    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // "rooms" 컬렉션에서 데이터 가져오기
        db.collection("rooms")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            // 여기서 가져온 데이터를 사용하거나 처리할 수 있습니다.
                        }
                    } else {
                        Log.d(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void showDatePickerDialog() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // 날짜 선택 시 호출되는 콜백
                    selectedDate.set(year1, monthOfYear, dayOfMonth);
                    updateSelectedDateTextView();
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void showDatePickerDialogForSecondButton() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // 날짜 선택 시 호출되는 콜백
                    selectedDate.set(year1, monthOfYear, dayOfMonth);
                    updateSecondButtonDateTextView();
                },
                year, month, day);

        datePickerDialog.show();
    }


    private String updateSelectedDateTextView() {
        // 선택한 날짜를 텍스트 뷰에 표시
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());
        binding.datePickerActions1.setText("시작 : " + formattedDate);

        // 텍스트를 그대로 반환
        return formattedDate;
    }

    private String updateSecondButtonDateTextView() {
        // 선택한 날짜를 텍스트 뷰에 표시
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());
        binding.datePickerActions2.setText("종료 : " + formattedDate);

        // 텍스트를 그대로 반환
        return formattedDate;
    }

    private void saveDataToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 데이터 추가 예시
        Map<String, Object> data = new HashMap<>();
        data.put("name", binding.registerName.getText().toString());
        data.put("password", binding.registerPassword.getText().toString());
        data.put("location", binding.registerLocation.getText().toString());
        data.put("startDate", updateSelectedDateTextView());
        data.put("endDate", updateSecondButtonDateTextView());
        data.put("member", binding.registerMember.getText().toString());
        data.put("memo", binding.registerMemo.getText().toString());

        // "rooms" 컬렉션에 데이터 추가
        db.collection("rooms")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    // 추가 성공
                    Toast.makeText(make_room.this, "데이터가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // 추가 실패
                    Toast.makeText(make_room.this, "데이터 등록 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}