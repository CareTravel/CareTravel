package com.example.caretravel;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMakeRoomBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.DatePickerDialog;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

        binding.calender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        binding.calender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogForSecondButton();
            }
        });

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFirebase();
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


    private void saveDataToFirebase() {
        // Firebase Realtime Database의 "rooms" 노드에 데이터 저장
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rooms");

        // 데이터 모델 클래스를 사용하거나 Map을 만들어서 데이터를 저장
        String name = binding.registerName.getText().toString();
        String password = binding.registerPassword.getText().toString();
        String location = binding.registerLocation.getText().toString();

        // 변경된 부분
        String startDate = updateSelectedDateTextView();
        String endDate = updateSecondButtonDateTextView();

        String member = binding.registerMember.getText().toString();
        String memo = binding.registerMemo.getText().toString();

        if (startDate != null && endDate != null) {
            // 데이터베이스에 저장할 데이터 생성
            RoomData roomData = new RoomData(name, password, location, startDate, endDate, member, memo);

            // "rooms" 노드에 데이터 추가
            String roomId = databaseReference.push().getKey();
            databaseReference.child(roomId).setValue(roomData.toMap());

            Toast.makeText(make_room.this, "데이터가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            // 날짜 파싱 오류 처리
            Toast.makeText(make_room.this, "날짜 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}