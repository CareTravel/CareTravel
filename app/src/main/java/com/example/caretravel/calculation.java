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

import com.example.caretravel.databinding.ActivityCalculationBinding;
import com.example.caretravel.databinding.CalculateLayoutBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class calculation extends AppCompatActivity {
    private ActivityCalculationBinding binding;
    private String roomName;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RelativeLayout rootLayout;
    private RelativeLayout relativeLayoutTable;
    private int userCount;
    int totalPrice; // 현재 로그인 되어 있는 계정의 금액
    int a;
    int total=0; // 다른 사람 금액
    int totalAll=0; // 총계

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rootLayout = binding.relativeLayoutTable;

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



        binding.calculateSave.setOnClickListener(v -> addData(roomName));

        initFirebaseAuth();
        FirebaseUser user = mAuth.getCurrentUser();

        TextView myName = findViewById(R.id.myName);
        String name = user.getDisplayName();
        myName.setText(name);

        FirebaseFirestore.getInstance();
        loadData(db, roomName);

        // 계산하기 버튼
        binding.calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countUser(roomName, name);
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

        // GridLayout에 새로운 ID 설정
        GridLayout newGridLayout = binding.gridlayoutTable2;  // binding을 사용하여 참조 가져오기
        newGridLayout.setId(View.generateViewId());  // 새로운 ID 설정

        // 새로운 contentPlus 버튼 설정
        Button contentPlusBtn = binding.contentPlus;
        contentPlusBtn.setId(View.generateViewId());
        contentPlusBtn.setOnClickListener(v -> {
            addNewRow(newGridLayout);  // 클릭 리스너를 설정합니다.
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

    // 계산하기 버튼 눌렀을 때 호출하는 함수
    private void countUser (String roomName, String name) {
        List<Integer> userExpenses = new ArrayList<>();
        List<String> userName = new ArrayList<>();
        db.collection("rooms").document(roomName).collection("정산")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // 총 몇 명인지 (n)
                    userCount = queryDocumentSnapshots.size();
                    Log.d("scr", "사용자 : " + userCount);

                    // 각 문서에서 price 값을 가져와서 더하기
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        if (document.exists()) {
                            // "List" 필드의 존재 여부 확인
                            if (document.contains("List")) {
                                // price 필드에 저장된 리스트 가져오기
                                List<Map<String, Object>> priceList = (List<Map<String, Object>>) document.get("List");

                                // price 값만 추출하여 더하기
                                total = 0;
                                for (Map<String, Object> list : priceList) {
                                    int a;
                                    String price = (String) list.get("price");
                                    if (price.equals("")){
                                        a = 0;
                                    }
                                    else {
                                        a = Integer.parseInt(price);
                                    }
                                    total += a;
                                }
                                totalAll += total;
                                Log.d("scr", "총계는" + totalAll);
                                // 개별 사용자의 총 지출 금액을 n으로 나누어 배열에 저장
                                total /= userCount;
                                userExpenses.add(total);
                            }
                            if (document.contains("name")){
                                String allName = document.getString("name");
                                Log.d("scr", "사용자 이름: " + allName);
                                userName.add(allName);
                            }
                        }
                    }
                    for (int i=0; i < userName.size(); i++){
                        if (name.equals(userName.get(i))){
                            totalPrice = userExpenses.get(i);
                            break;
                        }
                    }
                    // 기존 뷰에 있는 텍스트뷰 없애기
                    binding.first.setVisibility(View.GONE);
                    binding.second.setVisibility(View.GONE);
                    // 정산 내역 구하기
                    for (int i=0; i<userCount; i++){
                        if (totalPrice < userExpenses.get(i)){
                            int deposit = (userExpenses.get(i) - totalPrice);
                            String depositName = userName.get(i);
                            Log.d("scr", name + "이" + depositName+ "에게" + deposit + "원 줘야함");
                            loadDeposit(depositName, deposit, name); // 화면에 출력
                        }
                    }
                    loadAllPrice(totalAll);
                })
                .addOnFailureListener(e -> {
                    Log.d("scr", "개수를 가져오지 못했음");
                });

    }
    private void loadDeposit(String depositName, int deposit, String name){
        LinearLayout rootView = findViewById(R.id.deposit_layout);
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.deposit_layout, null);
        TextView myName = linearLayout.findViewById(R.id.myName);
        myName.setText(name);
        TextView another = linearLayout.findViewById(R.id.anotherName);
        another.setText(depositName);
        TextView price = linearLayout.findViewById(R.id.price);
        price.setText(String.valueOf(deposit));
        rootView.addView(linearLayout);

    }
    private void loadAllPrice(int totalAll){
        String allString = String.valueOf(totalAll);
        binding.total.setText(allString);
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
                        if (name.equals("")){
                            showToast("이름이 비워져있습니다.");
                        }
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
                if(!name.equals("")){
                    data.put("name", name);
                    data.put("List", priceList);
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
        }
    }
    private void loadData(FirebaseFirestore db, String roomName) {
        db.collection("rooms")
                .document(roomName)
                .collection("정산")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        if (!result.isEmpty()) {
                            LinearLayout mainLayout = findViewById(R.id.linearLayout); // 메인 레이아웃을 찾습니다.
                            // 기존 레이아웃 삭제
                            mainLayout.removeAllViews();

                            for (DocumentSnapshot document : result.getDocuments()) { // 모든 문서를 순회합니다.
                                Map<String, Object> data = document.getData();
                                List<HashMap<String, Object>> priceList = (List<HashMap<String, Object>>) data.get("price");

                                if (priceList != null) {
                                    addNewRelativeLayout(mainLayout); // 새로운 RelativeLayout을 생성하고 메인 레이아웃에 추가합니다.

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

                                    EditText nameEdit = newRelativeLayout.findViewById(R.id.calculate_name);

                                    for (HashMap<String, Object> price : priceList) {
                                        if (price != null) {
                                            String name = (String) data.get("name");
                                            String content = (String) price.get("content");
                                            String priceValue = String.valueOf(price.get("price"));

                                            // null 값을 확인합니다.
                                            if (name != null && content != null && priceValue != null) {
                                                EditText contentEdit = createNewEditText(false);
                                                EditText priceEdit = createNewEditText(false);

                                                // contentEdit의 왼쪽 마진을 설정합니다.
                                                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                                params.setMargins(dpToPx(55), 0, 0, 0);  // 마진 적용
                                                params.width = dpToPx(150);  // 너비 설정
                                                params.height = dpToPx(48);  // 높이 설정
                                                contentEdit.setLayoutParams(params);

                                                nameEdit.setText(name);
                                                contentEdit.setText(content);
                                                priceEdit.setText(priceValue);

                                                newGridLayout.addView(contentEdit); // 새로운 GridLayout에 뷰 추가.
                                                newGridLayout.addView(priceEdit); // 새로운 GridLayout에 뷰 추가.
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Log.d("loadData", "정산 정보를 모두 불러왔습니다.");
                });
    }
}