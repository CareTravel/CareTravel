package com.example.caretravel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakePlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rootLayout = findViewById(R.id.rootLayout);

        // 뒤로가기 버튼
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(make_Plan.this, home.class));
            }
        });

        button1 = binding.planTime1;
        button2 = binding.planTime2;
        // day_plus 버튼 선언
        Button day_plus = findViewById(R.id.day_plus);

        Calendar.getInstance();
        new SimpleDateFormat("HH:mm", Locale.getDefault());

        tableLayout = findViewById(R.id.tableLayout);
        emptyRowTemplate = findViewById(R.id.empty_row_template);

        // 기존에 있던 첫 번째 빈 Row를 삭제
        tableLayout.removeView(emptyRowTemplate);

        button1.setOnClickListener(v -> showTimeInputDialog(button1));
        button2.setOnClickListener(v -> showTimeInputDialog(button2));
        day_plus.setOnClickListener(v -> addNewTableLayout());  // 클릭 이벤트 리스너 설정
    }

    private int dayCounter = 1;
    // 새로운 TableLayout을 추가하는 메서드
    private void addNewTableLayout() {
        // table_layout.xml을 inflate하여 새로운 TableLayout 생성
        TableLayout newTableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.table_layout, null);

        // dp 단위의 마진 값을 픽셀 단위로 변환
        int horizontalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());

        // 새로운 TableLayout의 LayoutParams 설정
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(horizontalMargin, topMargin, horizontalMargin, 0);
        newTableLayout.setLayoutParams(layoutParams);

        // plan_day1 아이디를 가진 텍스트 뷰의 텍스트를 설정
        TextView dayTextView = newTableLayout.findViewById(R.id.plan_day1);
        dayCounter++; // 일수를 1 증가
        dayTextView.setText(dayCounter + "일차");

        // 각 TableRow의 첫 번째 Button에 클릭 이벤트 리스너 설정
        for (int i = 0; i < newTableLayout.getChildCount(); i++) {
            View view = newTableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                View buttonView = row.getChildAt(0);
                if (buttonView instanceof Button) {
                    Button button = (Button) buttonView;
                    button.setOnClickListener(v -> showTimeInputDialog(button));
                }
            }
        }

        // 새로운 TableLayout을 rootLayout에 추가
        rootLayout.addView(newTableLayout);
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

                // 현재 클릭한 버튼이 속한 TableRow를 찾음
                TableRow currentRow = (TableRow) button.getParent();
                // 그 TableRow가 속한 TableLayout를 찾음
                TableLayout currentTableLayout = null;
                View parentView = (View) currentRow.getParent();
                while (parentView != null) {
                    if (parentView instanceof TableLayout) {
                        currentTableLayout = (TableLayout) parentView;
                        break;
                    }
                    parentView = (View) parentView.getParent();
                }

                if (currentTableLayout != null && isLastRowFilled(currentTableLayout)) {
                    // 새로운 로우를 만들어 TableLayout에 추가
                    addNewRow(currentTableLayout);
                }
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());

        Dialog dialog = builder.create();
        dialog.show();

        //중요
        addNewRow((TableLayout) button.getParent().getParent());
    }

    // isLastRowFilled 메서드 수정
    private boolean isLastRowFilled(TableLayout tableLayout) {
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
                    // Button의 텍스트가 "시간 선택"이거나 EditText1이 비어있거나 EditText2가 비어있으면 false 반환
                    if ("시간 선택".equals(button.getText().toString())) {
                        return false; // "시간 선택"인 경우
                    }

                    if (editText1.getText().toString().trim().isEmpty() || editText2.getText().toString().trim().isEmpty()) {
                        return false; // EditText1이나 EditText2가 비어있는 경우
                    }

                    // 모든 조건을 만족하면 true 반환
                    return true;
                }
            }
        }

        // 마지막 로우가 없거나 예외 상황이라면 false 반환
        return false;
    }



    private void addNewRow(TableLayout tableLayout) {
        // 새로운 TableRow 생성
        TableRow newRow = new TableRow(this);
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
        button.setOnClickListener(v -> showTimeInputDialog(button));

        // 새로운 EditText 생성
        EditText editText1 = createNewEditText();
        EditText editText2 = createNewEditText();

        // Button과 EditText를 TableRow에 추가
        newRow.addView(button);
        newRow.addView(editText1);
        newRow.addView(editText2);

        // TableRow를 TableLayout에 추가
        tableLayout.addView(newRow);
    }

    private EditText createNewEditText() {
        EditText editText = new EditText(this);
        editText.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
        editText.setHint("내용 입력");
        editText.setBackgroundColor(Color.WHITE);
        editText.setPadding(0, 0, 0, 0);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setId(View.generateViewId()); // 새로운 ID 생성
        return editText;
    }
}