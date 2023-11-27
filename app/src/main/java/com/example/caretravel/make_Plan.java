package com.example.caretravel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMakePlanBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class make_Plan extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private ActivityMakePlanBinding binding;
    private EditText inputStartTime;
    private EditText inputEndTime;
    private TableLayout tableLayout;
    private TableRow emptyRowTemplate;
    private TableRow tableRow;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakePlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        button1 = binding.planTime1;
        button2 = binding.planTime2;
        Calendar.getInstance();
        new SimpleDateFormat("HH:mm", Locale.getDefault());

        tableLayout = findViewById(R.id.tableLayout);
        emptyRowTemplate = findViewById(R.id.empty_row_template);

        // 기존에 있던 첫 번째 빈 Row를 삭제
        tableLayout.removeView(emptyRowTemplate);


        button1.setOnClickListener(v -> showTimeInputDialog(button1));
        button2.setOnClickListener(v -> showTimeInputDialog(button2));
    }


    private void showTimeInputDialog(final Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("시간 입력");

        // 다이얼로그에 입력 폼을 추가합니다.
        View view = getLayoutInflater().inflate(R.layout.dialog_time_input, null);
        builder.setView(view);

        inputStartTime = view.findViewById(R.id.input_start_time);
        inputEndTime = view.findViewById(R.id.input_end_time);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String startTime = inputStartTime.getText().toString();
                String endTime = inputEndTime.getText().toString();
                String buttonText = startTime + " - " + endTime;
                button.setText(buttonText);

                // 마지막 로우의 모든 값이 채워져 있는지 확인
                if (isLastRowFilled()) {
                    // 새로운 로우를 만들어 TableLayout에 추가
                    addNewRow();
                }
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());

        Dialog dialog = builder.create();
        dialog.show();
    }

    // 수정된 isLastRowFilled 메서드
    private boolean isLastRowFilled() {
        int lastRowIndex = tableLayout.getChildCount() - 1; // 마지막 로우의 인덱스

        if (lastRowIndex >= 0) {
            TableRow lastRow = (TableRow) tableLayout.getChildAt(lastRowIndex);

            if (lastRow != null) {
                // 필요한 뷰가 Button과 EditText 2개인 경우
                Button button = lastRow.getChildAt(0) instanceof Button ? (Button) lastRow.getChildAt(0) : null;
                EditText editText1 = lastRow.getChildAt(1) instanceof EditText ? (EditText) lastRow.getChildAt(1) : null;
                EditText editText2 = lastRow.getChildAt(2) instanceof EditText ? (EditText) lastRow.getChildAt(2) : null;

                // null 체크를 추가하여 NullPointerException 방지
                if (button != null && editText1 != null && editText2 != null) {
                    // EditText1이 비어있거나 EditText2가 비어있으면 false 반환
                    return !(editText1.getText().toString().trim().isEmpty() || editText2.getText().toString().trim().isEmpty());
                }
            }
        }

        // 마지막 로우가 없거나 예외 상황이라면 false 반환
        return false;
    }


    private void addNewRow() {
        // 새로운 TableRow 생성
        TableRow newRow = new TableRow(this);
        tableLayout.setBackgroundColor(Color.TRANSPARENT);

        newRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        // 새로운 Button 생성
        Button button = new Button(this);
        button.setLayoutParams(new TableRow.LayoutParams(
                0, // 너비 0으로 설정하여 가중치를 사용
                TableRow.LayoutParams.MATCH_PARENT, 0.7f)); // 가중치 조절

        button.setText("시간 선택");
        button.setBackgroundColor(Color.WHITE);

        // 새로운 EditText 생성
        EditText editText1 = new EditText(this);
        editText1.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        editText1.setHint("내용 입력");
        editText1.setBackgroundColor(Color.WHITE);
        editText1.setPadding(0, 0, 0, 0);
        editText1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText1.setInputType(InputType.TYPE_CLASS_TEXT);
        editText1.setId(View.generateViewId()); // 새로운 ID 생성

        EditText editText2 = new EditText(this);
        editText2.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        editText2.setHint("내용 입력");
        editText2.setBackgroundColor(Color.WHITE);
        editText2.setPadding(0, 0, 0, 0);
        editText2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText2.setInputType(InputType.TYPE_CLASS_TEXT);
        editText2.setId(View.generateViewId()); // 새로운 ID 생성

        // Button과 EditText를 TableRow에 추가
        newRow.addView(button);
        newRow.addView(editText1);
        newRow.addView(editText2);

        // TableRow를 TableLayout에 추가
        tableLayout.addView(newRow);

        // 새로 추가된 행의 버튼에 대한 클릭 이벤트 리스너 설정
        button.setOnClickListener(v -> showTimeInputDialog(button));
    }

}
