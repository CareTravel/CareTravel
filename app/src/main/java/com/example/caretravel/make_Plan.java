package com.example.caretravel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMakePlanBinding;
import com.example.caretravel.databinding.DialogTimeInputBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class make_Plan extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private ActivityMakePlanBinding binding;
    private EditText inputStartTime;
    private EditText inputEndTime;
    private TableLayout tableLayout;
    private TableRow emptyRowTemplate;
    private LinearLayout rootLayout;
    private String roomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakePlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // SharedPreferences에서 선택된 방의 이름을 가져옵니다.
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);
        Log.d("lay", "Room name is: " + roomName);

        FirebaseApp.initializeApp(this);

        binding.planSave.setOnClickListener(v -> savePlanToFirestore());

        // 뒤로가기 버튼
        binding.backButton.setOnClickListener(v -> startActivity(new Intent(make_Plan.this, home.class)));

        button1 = binding.planTime1;
        button2 = binding.planTime2;
        // day_plus 버튼 선언
        Button day_plus = binding.dayPlus;

        Calendar.getInstance();
        new SimpleDateFormat("HH:mm", Locale.getDefault());

        tableLayout = binding.tableLayout;
        emptyRowTemplate = binding.emptyRowTemplate;
        rootLayout = binding.rootLayout;


        // 기존에 있던 첫 번째 빈 Row를 삭제
        tableLayout.removeView(emptyRowTemplate);

        button1.setOnClickListener(v -> showTimeInputDialog(button1));
        button2.setOnClickListener(v -> showTimeInputDialog(button2));
        day_plus.setOnClickListener(v -> addNewTableLayout());  // 클릭 이벤트 리스너 설정

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        loadData(firestore, roomName);
    }

    // 데이터 가져오는 메소드
    private void loadData(FirebaseFirestore firestore, String roomId) {
        firestore.collection("rooms")
                .document(roomId)
                .collection("plans")
                .orderBy(FieldPath.documentId()) // 문서 ID로 정렬
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String currentTableId = "";
                            TableLayout currentTableLayout = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                String[] split = documentId.split("table|row");
                                String tableId = split[1];
                                if (!tableId.equals(currentTableId)) {

                                    // 새로운 테이블인 경우 기존 테이블 레이아웃 가져오기
                                    if (rootLayout.getChildCount() > Integer.parseInt(tableId)) {
                                        currentTableLayout = (TableLayout) rootLayout.getChildAt(Integer.parseInt(tableId));
                                    } else {
                                        // TableLayout이 존재하지 않는 경우 새로 생성
                                        addNewTableLayout();
                                        currentTableLayout = (TableLayout) rootLayout.getChildAt(rootLayout.getChildCount() - 1);
                                    }
                                    currentTableId = tableId;
                                }

                                // 기존 로우 가져오고 데이터 설정
                                int rowIndex = Integer.parseInt(split[2]);
                                TableRow row = null;
                                if (currentTableLayout != null && currentTableLayout.getChildCount() > rowIndex) {
                                    row = (TableRow) currentTableLayout.getChildAt(rowIndex);
                                } else {
                                    // TableRow가 존재하지 않는 경우 새로 생성
                                    addNewRow(currentTableLayout);
                                    row = (TableRow) currentTableLayout.getChildAt(currentTableLayout.getChildCount() - 1);
                                }

                                Button button = (Button) row.getChildAt(0);
                                EditText editText1 = (EditText) row.getChildAt(1);
                                EditText editText2 = (EditText) row.getChildAt(2);
                                Map<String, Object> plan = (Map<String, Object>) document.getData();
                                String time = (String) plan.get("time");
                                String content1 = (String) plan.get("content1");
                                String content2 = (String) plan.get("content2");
                                button.setText(time);
                                editText1.setText(content1);
                                editText2.setText(content2);
                            }
                            Log.d("lay", "계획정보를 모두 불러왔습니다.");
                        } else {
                            Log.d("lay", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    // Firestore 저장 메서드
    private void savePlanToFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        getRoomIdFromOtherJavaFile(roomName, roomId -> {
            // roomId를 받아왔을 때 실행되는 코드
            savePlanToFirestore(firestore, roomId);
        });
    }

    // Firestore 저장 메서드
    private void savePlanToFirestore(FirebaseFirestore firestore, String roomId) {
        // roomId 값이 null인 경우 처리
        if (roomId == null) {
            Toast.makeText(make_Plan.this, "방 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 방 정보가 저장된 다큐먼트 참조
        DocumentReference roomDocRef = firestore.collection("rooms").document(roomId);

        // 각 TableLayout에 대한 반복
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            View view = rootLayout.getChildAt(i);
            if (view instanceof TableLayout) {
                TableLayout tableLayout = (TableLayout) view;

                // 각 TableRow에 대한 반복
                for (int j = 0; j < tableLayout.getChildCount(); j++) {
                    View rowView = tableLayout.getChildAt(j);
                    if (rowView instanceof TableRow) {
                        TableRow row = (TableRow) rowView;

                        // 버튼과 EditText 가져오기
                        view = row.getChildAt(0);
                        if (view instanceof Button) {
                            Button button = (Button) view;
                            EditText editText1 = (EditText) row.getChildAt(1);
                            EditText editText2 = (EditText) row.getChildAt(2);

                        // 텍스트 가져오기
                            String time = button.getText().toString();
                            String content1 = editText1.getText().toString().trim();
                            String content2 = editText2.getText().toString().trim();

                        // 빈 값 체크
                          //  if (!time.equals("시간 선택") && !content1.isEmpty() && !content2.isEmpty()) {
                                // 데이터 만들기
                                Map<String, Object> plan = new HashMap<>();
                                plan.put("time", time);
                                plan.put("content1", content1);
                                plan.put("content2", content2);

                                // Firestore에 데이터 추가
                                roomDocRef.collection("plans")
                                        .document("table" + i + "row" + j)  // 각 행을 별도의 문서로 저장
                                        .set(plan)
                                        .addOnSuccessListener(documentReference -> {
                                            // 성공적으로 저장되었을 때 토스트 메시지 표시
                                            Toast.makeText(make_Plan.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // 저장 실패 시 토스트 메시지 표시
                                            Toast.makeText(make_Plan.this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        });
                            //}
                        }
                    }
                }
            }
        }
    }

    private void getRoomIdFromOtherJavaFile(String roomName, OnRoomIdReceivedListener listener) {
        // Firebase Firestore에 접근하여 방 정보의 다큐먼트 ID를 조회하는 로직 구현
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference roomsCollection = firestore.collection("rooms");

        roomsCollection.whereEqualTo("name", roomName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    String roomId = documentSnapshot.getId();
                    listener.onRoomIdReceived(roomId);  // roomId를 Listener를 통해 반환
                } else {
                    Toast.makeText(make_Plan.this, "방 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(make_Plan.this, "방 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface OnRoomIdReceivedListener {
        void onRoomIdReceived(String roomId);
    }


    // 새로운 TableLayout을 추가하는 메서드
    private int dayCounter = 1;
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

        // plan_day1 텍스트 뷰의 텍스트를 설정
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

    // 다이얼로그 메서드
    private void showTimeInputDialog(final Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("시간 입력");

        // 다이얼로그에 입력xml 바인딩
        DialogTimeInputBinding binding = DialogTimeInputBinding.inflate(getLayoutInflater());
        builder.setView(binding.getRoot());

        inputStartTime = binding.inputStartTime;
        inputEndTime = binding.inputEndTime;
        // 다이얼로그의 확인 버튼 누를 시 클릭 리스너
        builder.setPositiveButton("확인", (dialog, which) -> {
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