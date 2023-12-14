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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.caretravel.databinding.ActivityPathBinding;
import com.example.caretravel.databinding.PathlayoutBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class path extends AppCompatActivity {

    //private GoogleMap map;
    String roomName;
    private FirebaseFirestore db;
    private RelativeLayout rootLayout;
    private ActivityPathBinding binding;
    private Intent intent;
    private int dayCounter = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        binding = ActivityPathBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rootLayout = binding.pathRelative;

        Log.d("phj", "지도 방 들어옴");
        //서버 연결해서 같은 방으로 이어줌
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);
        Log.d("phj", "지도 방이름 " + roomName);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        initializeCloudFirestore();
        loadData(roomName);

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
        binding.mapSave.setOnClickListener(v -> addData(roomName));
//            Intent intent = new Intent(path.this, MapsFragment.class);
//            intent.putExtra("정산", collection);
//            startActivity(intent);

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

    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }


    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }


    public void addNewRelativeLayout(LinearLayout linearLayout) {
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
        dayCounter++;
        Log.d("scr", "일차" + dayCounter);
        dayTextView.setText(dayCounter + "일차");
        int daycount = dayCounter -1;
        String documentName = daycount +"일차";
        Log.d("scr", documentName+"일차");


        //날짜 버튼
        binding.pathDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MapsFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("roomname",roomName);
                bundle.putString("day", documentName );
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.path_mapLayout, fragment);
                fragmentTransaction.commit();
            }
        });
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
        LinearLayout rootLayout = binding.pathLinearlayout;
        String documentName = null;
        String[] content = new String[2];
        Map<String, Object> data = new HashMap<>();

        // rootLayout의 각 자식 뷰에 대해 반복
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            ArrayList<Map<String, Object>> LocateList = new ArrayList<>();
            View view = rootLayout.getChildAt(i);
            // RelativeLayout을 포함하고 있는 경우
            if (view instanceof RelativeLayout) {
                RelativeLayout relativeLayout = (RelativeLayout) view;
                // RelativeLayout 내의 각 뷰에 대해 반복
                for (int j = 0; j < relativeLayout.getChildCount(); j++) {
                    View innerView = relativeLayout.getChildAt(j);
                    if(innerView instanceof Button) {
                        Button button = (Button) innerView;
                        documentName = button.getText().toString();
                    }
                    // 현재 뷰가 GridLayout인 경우
                    if (innerView instanceof GridLayout) {
                        GridLayout gridLayout = (GridLayout) innerView;

                        // GridLayout의 각 행에 대해 반복
                        for (int row = 1; row < gridLayout.getRowCount(); row++) {
                            for (int column = 0; column < 2; column++) {
                                // 현재 셀을 참조
                                View cellView = gridLayout.getChildAt(row * gridLayout.getColumnCount() + column);
                                // 두번째 행부터 각 행의 EditText를 필드 값의 리스트로 저장
                                if (cellView instanceof EditText) {
                                    EditText editText1 = (EditText) cellView;
                                    content[column] = editText1.getText().toString();
                                }
                            }
                            // 행 데이터를 리스트에 추가

                            Map<String, Object> map = new HashMap<>();
                            map.put("locate", content[0]);
                            map.put("content", content[1]);
                            LocateList.add(map);
                            }
                        }
                    }
                }
                // 문서 이름이 있는 경우에만 Firestore에 데이터를 저장
                if (documentName != null && !documentName.isEmpty()) {
                    data.put("List", LocateList);
                    db.collection("rooms").document(roomName).collection("경로").document(documentName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showToast("저장했습니다.");
                                    Log.d("lay", "저장했습니다.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("lay", "저장에 실패했습니다");
                                }
                            });
                }
            }
        }

    private void loadData(String roomName) {
        db.collection("rooms").document(roomName).collection("경로")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        if (!result.isEmpty()) {
                            LinearLayout mainLayout = findViewById(R.id.path_linearlayout); // 메인 레이아웃을 찾습니다.
                            // 기존 레이아웃 삭제
                            mainLayout.removeAllViews();
                            for (DocumentSnapshot document : result.getDocuments()) { // 모든 문서를 순회합니다.
                                Map<String, Object> data = document.getData();
                                List<HashMap<String, Object>> locateList = (List<HashMap<String, Object>>) data.get("List");

                                if (locateList != null) {
                                    addNewRelativeLoad(mainLayout); // 새로운 RelativeLayout을 생성하고 메인 레이아웃에 추가합니다.
                                    RelativeLayout newRelativeLayout = (RelativeLayout) mainLayout.getChildAt(mainLayout.getChildCount() - 1); // 방금 생성한 RelativeLayout을 찾습니다.

                                    GridLayout newGridLayout = null;
                                    for (int i = 0; i < newRelativeLayout.getChildCount(); i++) {  // RelativeLayout의 모든 자식 뷰를 순회하며 GridLayout을 찾습니다.
                                        View child = newRelativeLayout.getChildAt(i);
                                        if (child instanceof GridLayout) {
                                            newGridLayout = (GridLayout) child;
                                            // GridLayout의 기존 EditText row를 삭제합니다.
                                            List<View> toRemove = new ArrayList<>();
                                            for (int j = 0; j < newGridLayout.getChildCount(); j++) {
                                                View gridChild = newGridLayout.getChildAt(j);
                                                if (gridChild instanceof EditText) {
                                                    toRemove.add(gridChild);
                                                }
                                            }
                                            for (View view : toRemove) {
                                                newGridLayout.removeView(view);
                                            }
                                            break;
                                        }
                                    }

                                    for (HashMap<String, Object> locate : locateList) {
                                        if (locate != null) {

                                            String content = (String) locate.get("content");
                                            String locateValue = String.valueOf(locate.get("locate"));

                                            // null 값을 확인합니다.
                                            if (content != null && locateValue != null) {
                                                EditText locateEdit = createNewEditText(false);
                                                EditText contentEdit = createNewEditText(false);

                                                //contentEdit의 왼쪽 마진을 설정합니다.
                                                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                                params.setMargins(dpToPx(55), 0, 0, 0);  // 마진 적용
                                                params.width = dpToPx(150);  // 너비 설정
                                                params.height = dpToPx(48);  // 높이 설정
                                                locateEdit.setLayoutParams(params);

                                                locateEdit.setText(locateValue);
                                                contentEdit.setText(content);

                                                newGridLayout.addView(locateEdit); // 새로운 GridLayout에 뷰 추가.
                                                newGridLayout.addView(contentEdit); // 새로운 GridLayout에 뷰 추가.
                                            }
                                        }
                                    }
                                }
                            }
                            dayCounter--;
                        }
                    }

                    Log.d("loadData", "정산 정보를 모두 불러왔습니다.");
                });
    }

    public void addNewRelativeLoad(LinearLayout linearLayout) {
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
        dayTextView.setText(dayCounter + "일차");
        dayCounter++;
        int daycount = dayCounter -1;
        String documentName = daycount +"일차";
        Log.d("scr", documentName+"일차");

        //날짜 버튼
        binding.pathDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MapsFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("roomname",roomName);
                bundle.putString("day", documentName );
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.path_mapLayout, fragment);
                fragmentTransaction.commit();
            }
        });
    }

}