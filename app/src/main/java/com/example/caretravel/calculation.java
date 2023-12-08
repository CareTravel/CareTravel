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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.caretravel.databinding.ActivityCalculationBinding;
import com.example.caretravel.databinding.CalculateLayoutBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
public class calculation extends AppCompatActivity {
    private ActivityCalculationBinding binding;
    private String roomName;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeCloudFirestore();

        // SharedPreferences에서 선택된 방의 이름을 가져옵니다.
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);
        Log.d("lay", "Room name is: " + roomName);

        FirebaseApp.initializeApp(this);

        // 뒤로가기 버튼
        binding.backButton.setOnClickListener(v -> startActivity(new Intent(calculation.this, home.class)));
        binding.contentPlus.setOnClickListener(v -> addNewRow(binding.gridlayoutTable));
        binding.calculateAdd.setOnClickListener(v -> {
            LinearLayout linearLayout = binding.linearLayout;
            addNewRelativeLayout(linearLayout);
        });

        binding.calculateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(roomName);
            }
        });

        initFirebaseAuth();
        FirebaseUser user = mAuth.getCurrentUser();

        TextView myName = findViewById(R.id.myName);
        String name = user.getDisplayName();
        myName.setText(name);
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }
    private void addNewRelativeLayout(LinearLayout linearLayout) {
        // LayoutInflater 생성
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 기존의 RelativeLayout 복제
        CalculateLayoutBinding binding = CalculateLayoutBinding.inflate(inflater);
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

        Button button = binding.contentPlus;
        button.setId(View.generateViewId());
        button.setOnClickListener(v -> {
            addEditTextToRelativeLayout(newRelativeLayout, true);  // 첫 번째 EditText, 마진 적용
            addEditTextToRelativeLayout(newRelativeLayout, false);  // 그 외 EditText, 마진 미적용

        });

        // LinearLayout에 복제한 RelativeLayout 추가
        linearLayout.addView(newRelativeLayout);
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

    private void addData(String roomName) {
        LinearLayout rootLayout = binding.linearLayout;
        String name = null;
        String[] content = new String[2];
        Map<String, Object> data = new HashMap<>();

        // rootLayout의 각 자식 뷰에 대해 반복
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            ArrayList<Map<String, Object>> priceList = new ArrayList<>();
            View view = rootLayout.getChildAt(i);
            // RelativeLayout을 포함하고 있는 경우
            if (view instanceof RelativeLayout) {
                RelativeLayout relativeLayout = (RelativeLayout) view;
                // RelativeLayout 내의 각 뷰에 대해 반복
                for (int j = 0; j < relativeLayout.getChildCount(); j++) {
                    View innerView = relativeLayout.getChildAt(j);
                    // EditText인 경우
                    if (innerView instanceof EditText) {
                        EditText nameEdit = (EditText) innerView;
                        name = nameEdit.getText().toString();
                    }

                    // GridLayout인 경우
                    if (innerView instanceof GridLayout) {
                        GridLayout gridLayout = (GridLayout) innerView;

                        // GridLayout 내의 각 셀에 대해 반복
                        for (int row = 1; row < gridLayout.getRowCount(); row++) {
                            for (int column = 0; column < 2; column++) {
                                View cellView = gridLayout.getChildAt(row * gridLayout.getColumnCount() + column);
                                // 셀이 EditText인 경우
                                if (cellView instanceof EditText) {
                                    EditText editText1 = (EditText) cellView;
                                    content[column] = editText1.getText().toString();
                                }
                            }
                            // 데이터 맵을 생성하고 priceList에 추가
                            Map<String, Object> price = new HashMap<>();
                            price.put("content", content[0]);
                            price.put("price", content[1]);
                            priceList.add(price);
                        }
                    }
                }
                // Firestore에 저장할 데이터 맵 생성
                data.put("name", name);
                data.put("price", priceList);
            }
            // Firestore에 데이터 저장
            db.collection("rooms").document(roomName).collection("정산").document(name)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("저장했습니다.");
                            Log.d("scr", "저장했습니다.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("scr", "저장에 실패했습니다");
                        }
                    });
        }
    }

    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }
    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}