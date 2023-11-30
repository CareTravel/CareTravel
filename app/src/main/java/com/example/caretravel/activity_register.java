package com.example.caretravel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caretravel.databinding.ActivityRegisterBinding;

import java.util.ArrayList;
import java.util.List;

public class activity_register extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    //String roomName = getIntent().getStringExtra("roomName");
    //aaaaaaa
    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        //String serverPsd = getIntent().getStringExtra("password");
        //@SuppressLint("ResourceType")
        //String userPsd = (String) getText(R.id.writePassword);
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //if (userPsd.equals(serverPsd)) {
                showToast( "방으로 들어왔습니다.");
                startActivity(new Intent(activity_register.this, home.class));
            //}
            //else showToast("비밀번호가 틀렸습니다. 비밀번호를 확인하거나, 방 이름이 맞는지 다시 확인해 주세요.");
        }
    };
//    //aaaaaa

    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                                Log.d("phj", "result 돌아옴" + roomName);
                                list.add(roomName);
                                Log.d("phj", "리스트에 데이터 추가" + list);
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
                                            .setPositiveButton("확인", dialogListener)
                                            .setNegativeButton("취소", null)
                                            .create();

                                    regDialog.show();
                                }
                            });
                            //어댑터

                        }
                    }
                });

//        테스트용
//        ArrayList<String> list = new ArrayList<>();
//        for (int i=0; i<100; i++) {
//            list.add(String.format("TEXT %d", i)) ;
//        }
//
//        RecyclerView recyclerView = findViewById(R.id.addButtonView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(activity_register.this));
//        // LayoutManager 설정
//
//
//        MyAdapter myAdapter = new MyAdapter(list);
//        recyclerView.setAdapter(myAdapter); //어댑터 설정
//        Log.d("phj", "어댑터 속 "+list.size());


//        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//            LayoutInflater inflater = (LayoutInflater) getSystemService(activity_register.LAYOUT_INFLATER_SERVICE);
//            View dialogView = inflater.inflate(R.layout.regdialog_layout, null);
//
//            AlertDialog regDialog = new AlertDialog.Builder(activity_register.this)
//                    .setView(dialogView)
//                    .setPositiveButton("확인", dialogListener)
//                    .setNegativeButton("취소", null)
//                    .create();
//
//            regDialog.show();
//            }
//        });

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

}
