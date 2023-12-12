package com.example.caretravel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultOwner;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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

import com.example.caretravel.databinding.ActivityHomeBinding;
import com.example.caretravel.databinding.ActivityPathBinding;
import com.example.caretravel.databinding.CalculateLayoutBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

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

//        binding.locationAdd.setOnClickListener(v -> addNewRow(binding.pathGridlayoutTable));
//        binding.dayAdd.setOnClickListener(v -> {
//            LinearLayout linearLayout = binding.pathLinearlayout;
//            addNewGridLayout(linearLayout);
//        });
    }

    //표 추가
 //   private void addNewGridLayout(LinearLayout linearlayout) {
        // LayoutInflater 생성
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        // 기존의 RelativeLayout 복제
//        pathlayoutBinding binding = pathlayoutBinding.inflate(inflater);
//        LinearLayout newPathLinearLayout = binding.getRoot();
//
//        // 복제한 RelativeLayout에 새로운 ID 설정
//        newPathLinearLayout.setId(View.generateViewId());
//
//        // 새로운 RelativeLayout의 LayoutParams 설정
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        // 마진 설정
//        int marginInDp = 30;  // 30dp
//        int marginInPixels = (int) (marginInDp * getResources().getDisplayMetrics().density);
//        params.setMargins(0, marginInPixels, 0, 0);  // 상단에 30dp 마진 설정
//        newPathLinearLayout.setLayoutParams(params);
//
//            Button button = findViewById(R.id.location_add);
//            button.setId(View.generateViewId());
//            button.setOnClickListener(v -> {
//                addEditTextToRelativeLayout(newPathLinearLayout, true);  // 첫 번째 EditText, 마진 적용
//                addEditTextToRelativeLayout(newPathLinearLayout, false);  // 그 외 EditText, 마진 미적용
//
//            });
//
//        // LinearLayout에 복제한 RelativeLayout 추가
//        linearlayout.addView(newPathLinearLayout);
//    }
//
//    private void addEditTextToRelativeLayout(LinearLayout linearLayout, boolean applyMargin) {
//        // GridLayout 찾기
//        GridLayout gridLayout = linearLayout.findViewById(R.id.path_gridlayout_table);
//
//        // GridLayout의 최대 열 수 설정
//        int columnCount = 2;  // GridLayout의 최대 열 수
//        gridLayout.setColumnCount(columnCount);
//
//        // GridLayout의 현재 뷰 수
//        int childCount = gridLayout.getChildCount();
//
//        // GridLayout의 현재 행과 열 인덱스 계산
//        int currentRow = childCount / columnCount;
//        int currentColumn = childCount % columnCount;
//
//        EditText newEditText = new EditText(this);
//        newEditText.setId(View.generateViewId());  // EditText에 고유한 ID 할당
//
//        // 속성 설정
//        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//
//        if (applyMargin) {
//            params.setMargins(dpToPx(55), 0, 0, 0);  // 마진 적용
//        }
//
//        params.width = dpToPx(150);  // 너비 설정
//        params.height = dpToPx(48);  // 높이 설정
//
//        // EditText 위치 설정 (새로운 열에 추가)
//        params.columnSpec = GridLayout.spec(currentColumn, 1);
//        params.rowSpec = GridLayout.spec(currentRow, 1);
//
//        newEditText.setLayoutParams(params);
//        newEditText.setHint("내용 입력");
//        newEditText.setInputType(InputType.TYPE_CLASS_TEXT);
//        newEditText.setHintTextColor(Color.parseColor("#828282"));
//        newEditText.setGravity(Gravity.CENTER);
//        newEditText.setBackground(ContextCompat.getDrawable(this, R.drawable.cell2));
//
//        // GridLayout에 추가
//        gridLayout.addView(newEditText);
//    }
//
//    private void addNewRow(GridLayout gridLayout) {
//        // 새로운 텍스트를 입력할 수 있는 EditText 생성
//        EditText newEditText1 = createNewEditText(true);
//        EditText newEditText2 = createNewEditText(false);
//
//        // GridLayout에 추가
//        gridLayout.addView(newEditText1);
//        gridLayout.addView(newEditText2);
//    }
//
//    private EditText createNewEditText(boolean applyMargin) {
//        EditText newEditText = new EditText(this);
//
//        // 속성 설정
//        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//        if (applyMargin) {
//            params.setMargins(dpToPx(55), 0, 0, 0);  // 마진 적용
//        }
//        params.width = dpToPx(150);  // 너비 설정
//        params.height = dpToPx(48);  // 높이 설정
//
//        newEditText.setLayoutParams(params);
//        newEditText.setHint("내용 입력");
//        newEditText.setInputType(InputType.TYPE_CLASS_TEXT);
//        newEditText.setHintTextColor(Color.parseColor("#828282"));
//        newEditText.setGravity(Gravity.CENTER);
//        newEditText.setBackground(ContextCompat.getDrawable(this, R.drawable.cell2));
//
//        return newEditText;
//    }
//
//    private int dpToPx(int dp) {
//        float density = getResources().getDisplayMetrics().density;
//        return Math.round(dp * density);
//    }
        //여기까지

//        Bundle result = new Bundle();
//        // 번들 키 값과 전달 할 데이터 입력
//        result.putString("bundleKey", "1일차 같은 누른 버튼");
//        // setFragmentResult 메소드의 리퀘스트 키 값과 전달 할 데이터(번들) 입력
//        getMapsFragmentManager().setFragmentResult("requestKey", result);





//    private FragmentResultOwner getMapsFragmentManager() {
//        return null;
//    }
}