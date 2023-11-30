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

//    //aaaaaaa
//    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            if (dialog == regDialog && which == DialogInterface.BUTTON_POSITIVE) {
//                showToast(roomName + "으로 방이 생성되었습니다.");
//            }
//        }
//    };
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
                    String roomName = getIntent().getStringExtra("name");
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
//                        String roomName = getIntent().getStringExtra("roomName");
                        if (result.getResultCode() == RESULT_OK) {
                            //String roomName = getIntent().getStringExtra("roomName");
//                            Log.d("phj", "result 돌아옴" + roomName);
//                            list.add(roomName);
//                            Log.d("phj", "리스트에 데이터 추가" + list);
//
//                            RecyclerView recyclerView = findViewById(R.id.addButtonView);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(activity_register.this));
//                            // LayoutManager 설정
//
//                            MyAdapter myAdapter = new MyAdapter(list);
//                            Log.d("phj", "어댑터 속 " + list + list.size());
//                            recyclerView.setAdapter(myAdapter); //어댑터 설정
//
//                            myAdapter.notifyDataSetChanged();

                        }
                    }
                });

        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add(String.format("TEXT %d", i)) ;
        }

        RecyclerView recyclerView = findViewById(R.id.addButtonView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity_register.this));
        // LayoutManager 설정


        MyAdapter myAdapter = new MyAdapter(list);
        recyclerView.setAdapter(myAdapter); //어댑터 설정
        Log.d("phj", "어댑터 속 "+list.size());

        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //static void showpsdDialog() {
//            LayoutInflater inflater = (LayoutInflater) activity_register..getSystemService(activity_register.LAYOUT_INFLATER_SERVICE);
//            View dialogView = inflater.inflate(R.layout.regdialog_layout, null);
//
//            AlertDialog regDialog = new AlertDialog.Builder(activity_register.class.newInstance())
//                    .setView(dialogView)
//                    .setPositiveButton("확인", null)
//                    .setNegativeButton("취소", null)
//                    .create();
//
//            regDialog.show();
//        }
                startActivity(new Intent(activity_register.this, home.class));
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

}
