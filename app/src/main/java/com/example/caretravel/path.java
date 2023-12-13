package com.example.caretravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.caretravel.databinding.ActivityPathBinding;
import com.example.caretravel.databinding.PathlayoutBinding;


public class path extends AppCompatActivity {

    //private GoogleMap map;
    String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        ActivityPathBinding binding = ActivityPathBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("phj", "지도 방 들어옴");
        //서버 연결해서 같은 방으로 이어줌
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);
        Log.d("phj", "지도 방이름 " + roomName);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(path.this, home.class));
            }

        });

        binding.locationAdd.setOnClickListener(v -> addNewRow(binding.pathGridlayoutTable));
        binding.dayAdd.setOnClickListener(v -> {
            LinearLayout linearLayout = binding.pathLinearlayout;
            addNewRelativeLayout(linearLayout);
        });

        Button regiBtn = findViewById(R.id.path_day);
        regiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MapsFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.path_mapLayout, fragment);
                fragmentTransaction.commit();
            }
        });

    }
    private int dayCounter = 1;

    private void addNewRelativeLayout(LinearLayout linearLayout) {
        // LayoutInflater 생성
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 기존의 RelativeLayout 복제
        PathlayoutBinding binding = PathlayoutBinding.inflate(inflater);
        RelativeLayout newRelativeLayout = binding.getRoot();

        // 복제한 RelativeLayout에 새로운 ID 설정
        newRelativeLayout.setId(View.generateViewId());

        // 새로운 RelativeLayout의 LayoutParams 설정
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 마진 설정
        int marginInDp = 30;  // 30dp
        int marginInPixels = (int) (marginInDp * getResources().getDisplayMetrics().density);
        params.setMargins(0, marginInPixels, 0, 0);  // 상단에 30dp 마진 설정
        newRelativeLayout.setLayoutParams(params);

        // GridLayout에 새로운 ID 설정
        GridLayout newGridLayout = binding.pathGridlayoutTable;  // binding을 사용하여 참조 가져오기
        newGridLayout.setId(View.generateViewId());  // 새로운 ID 설정

        // 새로운 contentPlus 버튼 설정
        Button contentPlusBtn = binding.locationAdd;
        contentPlusBtn.setId(View.generateViewId());
        contentPlusBtn.setOnClickListener(v -> {
            addNewRow(newGridLayout);  // 클릭 리스너를 설정합니다.
        });

        // LinearLayout에 복제한 RelativeLayout 추가
        linearLayout.addView(newRelativeLayout);

        // plan_day1 텍스트 뷰의 텍스트를 설정
        TextView dayTextView = newGridLayout.findViewById(R.id.path_day);
        dayCounter++; // 일수를 1 증가
        dayTextView.setText(dayCounter + "일차");
    }

    private void addEditTextToRelativeLayout(RelativeLayout relativeLayout, boolean applyMargin) {
        // GridLayout 찾기
        GridLayout gridLayout = relativeLayout.findViewById(R.id.gridlayout_table);

        // GridLayout의 최대 열 수 설정
        int columnCount = 2;  // GridLayout의 최대 열 수
        gridLayout.setColumnCount(columnCount);

        // GridLayout의 현재 뷰 수
        int childCount = gridLayout.getChildCount();

        // GridLayout의 현재 행과 열 인덱스 계산
        int currentRow = childCount / columnCount;
        int currentColumn = childCount % columnCount;

        EditText newEditText = new EditText(this);
//        newEditText.setId(View.generateViewId());  // EditText에 고유한 ID 할당

        // 속성 설정
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        if (applyMargin) {
            params.setMargins(dpToPx(55), 0, 0, 0);  // 마진 적용
        }

        params.width = dpToPx(150);  // 너비 설정
        params.height = dpToPx(48);  // 높이 설정

        // EditText 위치 설정 (새로운 열에 추가)
        params.columnSpec = GridLayout.spec(currentColumn, 1);
        params.rowSpec = GridLayout.spec(currentRow, 1);

        newEditText.setLayoutParams(params);
        newEditText.setHint("내용 입력");
        newEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        newEditText.setHintTextColor(Color.parseColor("#828282"));
        newEditText.setGravity(Gravity.CENTER);
        newEditText.setBackground(ContextCompat.getDrawable(this, R.drawable.cell2));

        // GridLayout에 추가
        gridLayout.addView(newEditText);
    }

    private void addNewRow(GridLayout gridLayout) {
        // 새로운 텍스트를 입력할 수 있는 EditText 생성
        EditText newEditText1 = createNewEditText(true);
        EditText newEditText2 = createNewEditText(false);

        // GridLayout에 추가
        gridLayout.addView(newEditText1);
        gridLayout.addView(newEditText2);
    }

    private EditText createNewEditText(boolean applyMargin) {
        EditText newEditText = new EditText(this);

        // 속성 설정
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        if (applyMargin) {
            params.setMargins(dpToPx(55), 0, 0, 0);  // 마진 적용
        }
        params.width = dpToPx(150);  // 너비 설정
        params.height = dpToPx(48);  // 높이 설정

        newEditText.setLayoutParams(params);
        newEditText.setHint("내용 입력");
        newEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        newEditText.setHintTextColor(Color.parseColor("#828282"));
        newEditText.setGravity(Gravity.CENTER);
        newEditText.setBackground(ContextCompat.getDrawable(this, R.drawable.cell2));

        return newEditText;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

//    private void addNewRow(TableLayout tableLayout) {
//        // 새로운 TableRow 생성
//        TableRow newRow = new TableRow(this);
//        newRow.setLayoutParams(new TableRow.LayoutParams(
//                TableRow.LayoutParams.MATCH_PARENT,
//                TableRow.LayoutParams.WRAP_CONTENT));
//
//        // 새로운 Button 생성
//        Button button = new Button(this);
//        button.setLayoutParams(new TableRow.LayoutParams(
//                0, // 너비 0으로 설정하여 가중치를 사용
//                TableRow.LayoutParams.MATCH_PARENT, 0.7f)); // 가중치 조절
//        button.setText("시간 선택");
//        button.setBackgroundColor(Color.WHITE);
//        button.setOnClickListener(v -> showTimeInputDialog(button));
//
//        // 새로운 EditText 생성
//        EditText editText1 = createNewEditText();
//        EditText editText2 = createNewEditText();
//
//        // Button과 EditText를 TableRow에 추가
//        newRow.addView(button);
//        newRow.addView(editText1);
//        newRow.addView(editText2);
//
//        // TableRow를 TableLayout에 추가
//        tableLayout.addView(newRow);
//    }
//
//    private EditText createNewEditText() {
//        EditText editText = new EditText(this);
//        editText.setLayoutParams(new TableRow.LayoutParams(
//                0,
//                TableRow.LayoutParams.MATCH_PARENT, 1.0f));
//        editText.setHint("내용 입력");
//        editText.setBackgroundColor(Color.WHITE);
//        editText.setPadding(0, 0, 0, 0);
//        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        editText.setInputType(InputType.TYPE_CLASS_TEXT);
//        return editText;
//    }
}