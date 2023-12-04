package com.example.caretravel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caretravel.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class activity_register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseFirestore db;
    private EditText editText;

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    //수정--------------------------------
    public void queryRoomsCollection(String roomName, int userPsd) {
        Log.d("phj","커리 들어옴"+userPsd);
        //방 이름 같은 문서 가져오기
        db.collection("rooms")
                //비밀번호 같은 문서 찾기
                .whereEqualTo("name",roomName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("phj","방 있는 것 확인");
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.d("phj","포문 들어옴");
                        // 각 문서의 "password" 필드 값 가져오기
                        int serverPsd = Math.toIntExact(document.getLong("password"));
                        //방 이름 가져오기
                        //String roomName = document.getId();
                        Log.d("phj", "Password: " + serverPsd);

//                      //비교해서 홈화면으로 화면 넘기기, 방 이름 같이 넘김 -> 기능으로 들어갈때 방 이름 맞춰서 서버 연결
                        if (serverPsd == userPsd) {
                        showToast( "방으로 들어왔습니다.");
                        Intent intent = new Intent(activity_register.this, home.class);
                        intent.putExtra("name",roomName);
                        setResult(RESULT_OK,intent);
                        finish();
                        }
                        else showToast("비밀번호가 틀렸습니다. 비밀번호를 확인하거나, 방 이름이 맞는지 다시 확인해 주세요.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting documents: ", e);
                });
    }
    //수정--------------------------------

    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //수정------------------------
        MyAdapter adapter = new MyAdapter(list);
        adapter.setOnItemClickListener((position, text) -> {
            // 클릭된 아이템의 포지션과 텍스트 값에 대한 작업 수행
            Log.d("phj", "Clicked item at position: " + position + ", Text: " + text);

            LayoutInflater inflater = (LayoutInflater) getSystemService(activity_register.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.regdialog_layout, null);
            editText = dialogView.findViewById(R.id.writePassword);
            AlertDialog regDialog = new AlertDialog.Builder(activity_register.this)
                    .setView(dialogView)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String userPsd = editText.getText().toString();
                            String roomName = String.valueOf(text);
                            Log.d("phj", "userPsd: " + userPsd);
                            queryRoomsCollection(roomName, Integer.parseInt(userPsd));

                        }
                    })
                    .setNegativeButton("취소", null)
                    .create();

            regDialog.show();
        });
        //수정-----------------

        initializeCloudFirestore();
        //원래 있던 방 리스트 불러오기
        db.collection("rooms")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String roomName = document.getId();
                                list.add(roomName);

                                RecyclerView recyclerView = findViewById(R.id.addButtonView);
                                recyclerView.setLayoutManager(new LinearLayoutManager(activity_register.this));

                                MyAdapter myAdapter = new MyAdapter(list);
                                recyclerView.setAdapter(myAdapter);

//                                myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(View v, int position) {
//                                        LayoutInflater inflater = (LayoutInflater) getSystemService(activity_register.LAYOUT_INFLATER_SERVICE);
//                                        View dialogView = inflater.inflate(R.layout.regdialog_layout, null);
//                                        editText = dialogView.findViewById(R.id.writePassword);
//
//                                        AlertDialog regDialog = new AlertDialog.Builder(activity_register.this)
//                                                .setView(dialogView)
//                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        String userPsd = editText.getText().toString();
//                                                        Log.d("phj", "userPsd: " + userPsd);
//                                                        queryRoomsCollection(Integer.parseInt(userPsd));
//                                                    }
//                                                })
//                                                .setNegativeButton("취소", null)
//                                                .create();
//
//                                        regDialog.show();
//                                    }
//                                });

                            }
                        } else {
                            Log.d("scr", "Error getting documents: ", task.getException());
                        }
                    }
                });



        //방만들기 리스너 받고 와서 방 추가
        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                String roomName = data.getStringExtra("name");
                                //Log.d("phj", "result 돌아옴" + roomName);
                                list.add(roomName);
                            }

                            RecyclerView recyclerView = findViewById(R.id.addButtonView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity_register.this));
                            // LayoutManager 설정

                            MyAdapter myAdapter = new MyAdapter(list);
                            Log.d("phj", "어댑터 속 " + list + list.size());
                            recyclerView.setAdapter(myAdapter); //어댑터 설정

                            myAdapter.notifyDataSetChanged();

//                            myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View v, int position) {
//                                    LayoutInflater inflater = (LayoutInflater) getSystemService(activity_register.LAYOUT_INFLATER_SERVICE);
//                                    View dialogView = inflater.inflate(R.layout.regdialog_layout, null);
//
//                                    AlertDialog regDialog = new AlertDialog.Builder(activity_register.this)
//                                            .setView(dialogView)
//                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    String userPsd = editText.getText().toString();
//                                                    Log.d("phj", "userPsd: " + userPsd);
//                                                    queryRoomsCollection(Integer.parseInt(userPsd));
//                                                }
//                                            })
//                                            .setNegativeButton("취소", null)
//                                            .create();
//
//                                    regDialog.show();
//                                }
//                            });
                            //어댑터

                        }
                    }
                });



        binding.roomAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_register.this, make_room.class);
                startActivityResult.launch(intent);
            }
        });

        binding.myInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_register.this, myPage.class));
            }
        });

    }
    //onCreate 끝남
    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }
}
