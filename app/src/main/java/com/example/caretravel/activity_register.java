package com.example.caretravel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        createBtn();
                    }
                }
            });

    public  void createBtn () {
        String roomName = getIntent().getStringExtra("documentName");

        RelativeLayout View = (RelativeLayout) findViewById(R.id.relativeView);
        Log.d("phj","뷰 정해줌");
        Button roomButton = new Button(activity_register.this);
        Log.d("phj","버튼생성");
        roomButton.findViewById(R.id.roomRegister_button);
        Log.d("phj","findview");
        roomButton.setText(roomName);
        Log.d("phj","setText");
        View.addView(roomButton);
        Log.d("phj","addView");
    }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.roomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_register.this, make_room.class);
                startActivityResult.launch(intent);}
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //for (서버에 있는 방 다 만들때까지)
        createBtn();
        //방 이름 만들때 넘버링 할까????????
    }
}
