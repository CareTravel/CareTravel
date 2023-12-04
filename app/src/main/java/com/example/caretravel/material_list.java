package com.example.caretravel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMaterialListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;

public class material_list extends AppCompatActivity {
    ActivityMaterialListBinding binding;
    private FirebaseFirestore db;
    private List<EditText> editTextList = new ArrayList<>();
    private List<CheckBox> checkBoxList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 이름 가져오기
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        binding.mName.setText(name + "'s list");

        initializeCloudFirestore();

        // 데이터 불러오기
//        loadData(name);

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
                addData(name);
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
    private void addRow(){
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

        // 리스트에 추가
        editTextList.add(editText);
        checkBoxList.add(checkBox);


        // TableRow에 CheckBox와 EditText 추가
        newRow.addView(checkBox);
        newRow.addView(editText);

        // TableLayout에 TableRow 추가
        view.addView(newRow);
    }
    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void addData(String name) {
        int lastIndex = 4;

        DocumentReference userMaterialsDocRef = db.collection("rooms")
                .document("c")
                .collection("준비물")
                .document(name);

        TableLayout tableLayout = findViewById(R.id.tablelayout);
        int rowCount = tableLayout.getChildCount();
        if (rowCount > 0) {
            // 마지막 로우의 인덱스를 찾습니다.
            lastIndex = tableLayout.indexOfChild(tableLayout.getChildAt(rowCount - 1));
        }
        for (int i = 0; i <= lastIndex; i++) {
            Log.d("scr", "ddd");
            TableRow lastRow = (TableRow) tableLayout.getChildAt(i);
            CheckBox checkBox = (CheckBox) lastRow.getChildAt(0);
            EditText editText = (EditText) lastRow.getChildAt(1);
            String material_list = editText.getText().toString();
            Map<String, Object> material = new HashMap<>();
            material.put(material_list, Arrays.asList(checkBox.isChecked(), material_list));
        }
    }



//        if (db != null && name != null) {
//            // Firestore 경로 설정
//            DocumentReference userMaterialsDocRef = db.collection("rooms")
//                    .document("c")
//                    .collection("준비물")
//                    .document(name);
//
//            TableLayout tableLayout = findViewById(R.id.tablelayout);
//            int rowCount = tableLayout.getChildCount();
//            if (rowCount > 0) {
//                // 마지막 로우의 인덱스를 찾습니다.
//                lastIndex = tableLayout.indexOfChild(tableLayout.getChildAt(rowCount - 1));
//            }
//            for (int i=0; i <= lastIndex; i++) {
//                TableRow lastRow = (TableRow) tableLayout.getChildAt(i);
//                CheckBox chekBox = (CheckBox) lastRow.getChildAt(0);
//                EditText editText = (EditText) lastRow.getChildAt(1);
//                String material_list = editText.getText().toString();
//                Map<String, Object> meterial = new HashMap<>();
//                material.put()
//            }
//
//            // 데이터 준비
//            List<Map<String, Object>> materialsList = new ArrayList<>();
//
//            for (int i = 0; i < editTextList.size(); i++) {
//                CheckBox checkBox = checkBoxList.get(i);
//                EditText editText = editTextList.get(i);
//                Log.d("scr", "add data");
//
//                // 데이터 맵 생성
//                Map<String, Object> materialData = new HashMap<>();
//                materialData.put("checkbox", checkBox.isChecked());
//                materialData.put("material", editText.getText().toString());
//
//                // 리스트에 추가
//                materialsList.add(materialData);
//            }
//
//            // Firestore에 업로드
//            userMaterialsDocRef.set(Collections.singletonMap("materials", materialsList))
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("scr", "Firestore에 데이터가 추가되었습니다.");
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w("scr", "Firestore에 데이터 추가 실패", e);
//                        }
//                    });
//        }
//    }

    private void loadData(String name) {
        // Firestore 초기화
        Log.d("scr", "load data");

        if (db != null && name != null) {
            // "materials" 컬렉션에서 사용자의 이름으로 문서 가져오기
            DocumentReference userMaterialsDocRef = db.collection("rooms").document("c").collection("준비물").document(name);

            // Firestore에서 데이터 불러오기
            userMaterialsDocRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                // 문서가 존재할 때 데이터 불러오기
                                List<Map<String, Object>> materialsList =
                                        (List<Map<String, Object>>) documentSnapshot.get("materials");

                                // 데이터를 UI에 적용
                                applyDataToUI(materialsList);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("scr", "Firestore에서 데이터 불러오기 실패", e);
                        }
                    });
        }
    }

    private void applyDataToUI(List<Map<String, Object>> materialsList) {
        // UI에 데이터를 적용하는 로직을 구현
        // materialsList에서 데이터를 읽어와서 UI에 적용하는 방식으로 구현
        TableLayout tableLayout = findViewById(R.id.tablelayout);

        for (Map<String, Object> materialData : materialsList) {
            boolean isChecked = (boolean) materialData.get("isChecked");
            String text = (String) materialData.get("text");

            // 새로운 TableRow 생성
            TableRow newRow = new TableRow(this);

            // CheckBox와 EditText 추가
            CheckBox checkBox = new CheckBox(this);
            checkBox.setChecked(isChecked);
            newRow.addView(checkBox);

            EditText editText = new EditText(this);
            editText.setText(text);
            newRow.addView(editText);

            // TableLayout에 추가
            tableLayout.addView(newRow);
        }
    }
}
