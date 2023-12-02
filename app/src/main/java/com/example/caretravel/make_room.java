package com.example.caretravel;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    // Add the ActivityResultLauncher
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 등록 성공에 대한 처리
                    Toast.makeText(this, "방이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 등록 실패 또는 취소에 대한 처리
                    Toast.makeText(this, "방 등록이 실패하거나 취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 뒤로가기 버튼
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(make_room.this, activity_register.class));
            }
        });

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
        String documentName = binding.registerName.getText().toString();

        db.collection("rooms")
                .whereEqualTo("name", documentName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        // 중복되지 않은 경우에만 추가
                        Map<String, Object> data = new HashMap<>();
                        data.put("name", documentName);
                        data.put("password", binding.registerPassword.getText().toString());
                        data.put("location", binding.registerLocation.getText().toString());
                        data.put("startDate", updateSelectedDateTextView());
                        data.put("endDate", updateSecondButtonDateTextView());
                        data.put("member", binding.registerMember.getText().toString());
                        data.put("memo", binding.registerMemo.getText().toString());

                        db.collection("rooms")
                                .document(documentName)
                                .set(data)
                                .addOnSuccessListener(aVoid -> {
                                    // 추가 성공
                                    Toast.makeText(make_room.this, "방이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    // activity_register.xml로 화면 전환
                                    Intent intent = new Intent(make_room.this, activity_register.class);
                                    intent.putExtra("name",documentName);
                                    intent.putExtra("password",binding.registerPassword.getText().toString());
                                    setResult(RESULT_OK,intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    // 추가 실패
                                    Toast.makeText(make_room.this, "데이터 등록 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // 중복된 경우 사용자에게 알림
                        Toast.makeText(make_room.this, "이미 존재하는 방 이름입니다. 다른 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // 쿼리 실패 처리
                    Toast.makeText(make_room.this, "중복 확인 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
