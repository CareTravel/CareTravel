package com.example.caretravel;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMakeRoomBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class make_room extends AppCompatActivity {

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


    @SuppressLint("SetTextI18n")
    private String updateSelectedDateTextView() {
        // 선택한 날짜를 텍스트 뷰에 표시
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());
        binding.datePickerActions1.setText("시작 : " + formattedDate);

        // 텍스트를 그대로 반환
        return formattedDate;
    }

    @SuppressLint("SetTextI18n")
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
        String documentName = binding.registerName.getText().toString(); // registerName 필드에서 사용자 이름 가져오기

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
                .document(documentName)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    // 추가 성공
                    Toast.makeText(make_room.this, "방이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // 추가 실패
                    Toast.makeText(make_room.this, "데이터 등록 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}