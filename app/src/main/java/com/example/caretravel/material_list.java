package com.example.caretravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMaterialListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class material_list extends AppCompatActivity {
    ActivityMaterialListBinding binding;
    private FirebaseFirestore db;
    private String roomName;
    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 이름 가져오기
        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        roomName = sharedPreferences.getString("currentRoomName", null);


        // 데이터 베이스
        initializeCloudFirestore();

        // 데이터 불러오기
        loadData(roomName, userName);

        // 준비물 추가하기 버튼
        binding.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow();
            }
        });

        // 저장하기 버튼
        binding.materialSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(roomName, userName);
            }
        });

        // 뒤로가기 버튼
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(material_list.this, material.class));
            }
        });

    }

    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    // 새로운 로우를 추가하는 메소드
    private void addRow() {
        Log.d("scr", "addNewRow");
        TableLayout view = findViewById(R.id.tablelayout);
        TableRow newRow = new TableRow(this);
        CheckBox checkBox = new CheckBox(this);
        EditText editText = new EditText(this);

        // CheckBox와 EditText 마진
        TableRow.LayoutParams checkBoxParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        checkBoxParams.setMargins(40, 0, 0, 0);

        TableRow.LayoutParams editTextParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        editTextParams.setMargins(15, 0, 0, 0);

        checkBox.setLayoutParams(checkBoxParams);
        editText.setLayoutParams(editTextParams);

        // EditText 설정
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        editText.setHint("준비물을 입력하세요");


        // TableRow에 CheckBox와 EditText 추가
        newRow.addView(checkBox);
        newRow.addView(editText);

        // TableLayout에 TableRow 추가
        view.addView(newRow);
    }

    // 데이터 불러오기
    private void loadData(String roomName, String userName){
        db.collection("rooms").document(roomName).collection("준비물").document(userName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // 문서가 존재하는 경우
                                // "materials" 필드의 존재 여부 확인
                                if (document.contains("materials")) {
                                    // "materials" 필드에 저장된 리스트 가져오기
                                    List<Map<String, Object>> materialsList = (List<Map<String, Object>>) document.get("materials");

                                    // 이전에 정의한 0번부터 4번 인덱스까지의 EditText와 CheckBox에 데이터 설정
                                    for (int i = 0; i < 5; i++) {
                                        Log.d("scr", "기존데이터");
                                        Map<String, Object> material = materialsList.get(i);
                                        String editTextContent = (String) material.get("edittext");
                                        boolean isChecked = (boolean) material.get("checkbox");

                                        EditText[] editTexts = new EditText[5];
                                        CheckBox[] checkBoxes = new CheckBox[5];
                                        editTexts[i] = findViewById(getResources().getIdentifier("material_" + i, "id", "com.example.caretravel"));
                                        checkBoxes[i] = findViewById(getResources().getIdentifier("checkbox_" + i, "id", "com.example.caretravel"));

                                        if (!editTextContent.equals("")) {
                                            editTexts[i].setText(editTextContent);
                                        }
                                        checkBoxes[i].setChecked(isChecked);
                                    }

                                    // 나머지 데이터 불러 오기
                                    for (int i = 5; i < materialsList.size(); i++) {
                                        Map<String, Object> material = materialsList.get(i);
                                        String editTextContent = (String) material.get("edittext");
                                        boolean isChecked = (boolean) material.get("checkbox");
                                        loadRow(editTextContent, isChecked);
                                    }
                                } else {
                                    // "materials" 필드가 존재하지 않는 경우
                                    Log.d("FirestoreData", "No 'materials' field in the document");
                                    }
                            } else {
                                // 문서가 존재하지 않는 경우
                                Log.d("FirestoreData", "No such document");
                            }
                        } else {
                            Log.d("FirestoreData", "get failed with ", task.getException());
                        }
                    }
                });

    }
    // 있는 데이터 불러올 때 로우 추가
    private void loadRow(String editTextContent, boolean isChecked) {
        Log.d("scr", "load");
        TableLayout view = findViewById(R.id.tablelayout);
        TableRow newRow = new TableRow(this);
        CheckBox checkBox = new CheckBox(this);
        EditText editText = new EditText(this);

        // CheckBox와 EditText 마진
        TableRow.LayoutParams checkBoxParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        checkBoxParams.setMargins(40, 0, 0, 0);

        TableRow.LayoutParams editTextParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        editTextParams.setMargins(15, 0, 0, 0);

        checkBox.setLayoutParams(checkBoxParams);
        editText.setLayoutParams(editTextParams);

        // EditText 설정
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        // 상태 불러오기
        if (editTextContent.equals("")){
            editText.setHint("준비물을 입력하세요");
        }
        else{
            editText.setText(editTextContent);
        }
        checkBox.setChecked(isChecked);

        // TableRow에 CheckBox와 EditText 추가
        newRow.addView(checkBox);
        newRow.addView(editText);

        // TableLayout에 TableRow 추가
        view.addView(newRow);
    }

    // 데이터를 데이터 베이스에 저장
    private void addData(String roomName, String userName) {
        TableLayout view = findViewById(R.id.tablelayout);
        int rowCount = view.getChildCount();
        boolean isChecked = false;
        String editTextContent = null;

        // 각 준비물의 정보를 담을 ArrayList
        ArrayList<Map<String, Object>> materialsList = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            View tableview = view.getChildAt(i);

            if (tableview instanceof TableRow) {
                TableRow row = (TableRow) tableview;

                for (int j = 0; j < row.getChildCount(); j++) {
                    View childView = row.getChildAt(j);

                    if (childView instanceof EditText) {
                        // EditText를 찾은 경우
                        EditText editText = (EditText) childView;
                        editTextContent = editText.getText().toString();
                    }

                    if (childView instanceof CheckBox) {
                        // CheckBox를 찾은 경우
                        CheckBox checkBox = (CheckBox) childView;
                        isChecked = checkBox.isChecked();
                    }
                }
            }
            Map<String, Object> material = new HashMap<>();
            material.put("edittext", editTextContent);
            material.put("checkbox", isChecked);
            materialsList.add(material);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("name", userName);
        data.put("materials", materialsList);

        db.collection("rooms").document(roomName).collection("준비물").document(userName)
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


