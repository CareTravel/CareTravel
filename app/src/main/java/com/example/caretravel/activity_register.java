package com.example.caretravel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private String userPsd;
    ArrayList<String> list = new ArrayList<>();

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void queryRoomsCollection(String userPsd, int num) {//userPsd
        //position으로 받아서 같은 포지션의 방 이름 구하기
        Log.d("phj", "커리 들어옴" + userPsd);
        String roomnameText = list.get(num);
        Log.d("phj", "클릭한 방 이름" + roomnameText);

        initializeCloudFirestore();
        //방 이름 같은 문서 가져오기
        db.collection("rooms")
//                //비밀번호 같은 문서 찾기
                .document(roomnameText)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    Log.d("phj", "방 있는 것 확인");
                    if (documentSnapshots.exists()) {
                        Log.d("phj", "포문 들어옴");
                        // 각 문서의 password,roomName 값 가져오기
                        String serverPsd = documentSnapshots.getString("password");
                        String roomName = documentSnapshots.getId();

                        Log.d("phj", roomName);
                        Log.d("phj", "sever Password: " + serverPsd);
                        Log.d("phj", "user Password: " + userPsd);
                        Log.d("phj","커리안에서 포지션"+ num);

////                      //비교해서 홈화면으로 화면 넘기기, 방 이름 같이 넘김 -> 기능으로 들어갈때 방 이름 맞춰서 서버 연결
                    if (serverPsd.equals(userPsd)) {
                        Log.d("phj", "비교문 같음");
                        showToast("방으로 들어왔습니다.");
                        startActivity(new Intent(activity_register.this, home.class));
                    } else {
                        Log.d("phj", "비교문 다름");
                        showToast("비밀번호가 틀렸습니다. 비밀번호를 확인하거나, 방 이름이 맞는지 다시 확인해 주세요.");
                    }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error getting documents: ", e);
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                                myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int position) {
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(activity_register.LAYOUT_INFLATER_SERVICE);
                                        View dialogView = inflater.inflate(R.layout.regdialog_layout, null);
                                        editText = dialogView.findViewById(R.id.writePassword);

                                        AlertDialog regDialog = new AlertDialog.Builder(activity_register.this)
                                                .setView(dialogView)
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        userPsd = editText.getText().toString();
                                                        Log.d("phj", "userPsd: " + userPsd);
                                                        queryRoomsCollection(userPsd, position);
                                                    }
                                                })
                                                .setNegativeButton("취소", null)
                                                .create();

                                        regDialog.show();
                                    }
                                });

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

                            myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(activity_register.LAYOUT_INFLATER_SERVICE);
                                    View dialogView = inflater.inflate(R.layout.regdialog_layout, null);

                                    AlertDialog regDialog = new AlertDialog.Builder(activity_register.this)
                                            .setView(dialogView)
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    userPsd = editText.getText().toString();
                                                    Log.d("phj", "userPsd: " + userPsd);
                                                    queryRoomsCollection(userPsd,position);
                                                }
                                            })
                                            .setNegativeButton("취소", null)
                                            .create();

                                    regDialog.show();
                                }
                            });
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
