package com.example.caretravel;

import android.content.Context;
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

//    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            if (dialog == regDialog && which == DialogInterface.BUTTON_POSITIVE) {
//                showToast("으로 방이 생성되었습니다.");
//            }
//        }
//    };
    ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        String roomName = getIntent().getStringExtra("roomName");
                        if (result.getResultCode() == RESULT_OK) {
                            Log.d("phj", "result 돌아옴"+ roomName);
                            list.add(roomName);
                            Log.d("phj", "리스트에 데이터 추가" + list);

                            RecyclerView recyclerView = findViewById(R.id.addButtonView);
                            Log.d("phj", "뷰 고름 "+ recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(activity_register.this));
                            // LayoutManager 설정

                            MyAdapter myAdapter = new MyAdapter(list);
                            Log.d("phj", "어댑터 속 "+list);
                            recyclerView.setAdapter(myAdapter); //어댑터 설정

                        }
                    }
                });

//        ArrayList<String> list = new ArrayList<>();
//        for (int i=0; i<100; i++) {
//            list.add(String.format("TEXT %d", i)) ;
//        }

//        RecyclerView recyclerView = findViewById(R.id.addButtonView);
//        Log.d("phj", "뷰 고름 "+ recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        // LayoutManager 설정
//
//        MyAdapter myAdapter = new MyAdapter(list);
//        Log.d("phj", "어댑터 속 "+list);
//        recyclerView.setAdapter(myAdapter); //어댑터 설정

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

//        binding.roomButton.setOnClickListener(view -> {
//            //custom dialog를 위한 layout xml 초기화
//            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//            View dialogView = inflater.inflate(R.layout.regdialog_layout, null);
//
//            AlertDialog regDialog = new AlertDialog.Builder(this)
//                    .setView(dialogView)
//                    .setPositiveButton("확인", dialogListener)
//                    .setNegativeButton("취소", null)
//                    .create();
//
//            regDialog.show();
//        });

        //아이템 클릭리스너
//            MyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClicked(int position, String data) {
//
//                    Toast.makeText(getApplicationContext(), "Position:" + position + ", Data:" + data, Toast.LENGTH_SHORT).show();
//                }
//            });
    }
    //onCreate 끝남

    //리사이클러뷰 아이템 클릭 리스너
//    public interface OnItemClickListener {
//        void onItemClicked(int position, String data);
//    }
//
//    private OnItemClickListener itemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        itemClickListener = listener;
//    }

//    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//
//        //리사이클러뷰 뷰홀더
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            TextView textView;
//
//            public MyViewHolder(@NonNull View itemView) {
//                super(itemView);
//                textView = itemView.findViewById(R.id.buttonText);
//                Log.d("phj", "뷰 홀더 들어옴");
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        String data = "";
//                        int position = getAbsoluteAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            Log.d("phj", "버튼 클릭됨");
//                            if (itemClickListener != null)
//                                itemClickListener.onItemClicked(position, data);
//                        }
//                    }
//                });
//                textView = itemView.findViewById(R.id.buttonText) ;
//            }
//        }

        //리사이클러뷰 어댑터
//        private ArrayList<String> list;
//
//        private MyAdapter(ArrayList<String> list) {
//            this.list = list;
//        }
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.room_button, parent, false);
//
//            return new MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//            String text = list.get(position);
//            //String roomName = getIntent().getStringExtra("documentName");
//            holder.textView.setText(text);
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//    }
}
