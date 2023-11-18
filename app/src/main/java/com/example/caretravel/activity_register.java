package com.example.caretravel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private ActivityRegisterBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == regDialog && which == DialogInterface.BUTTON_POSITIVE) {
                showToast("으로 방이 생성되었습니다.");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.roomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_register.this, make_room.class));
            }
        });

        binding.myInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity_register.this, myPage.class));
            }
        });

        binding.roomButton.setOnClickListener(view -> {
            //custom dialog를 위한 layout xml 초기화
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.regdialog_layout, null);

            AlertDialog regDialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setPositiveButton("확인", dialogListener)
                    .setNegativeButton("취소", null)
                    .create();

            regDialog.show();
        });
    }
    public void createBtn(View view) {
        String documentName = binding.registerName.getText().toString();
        db.collection("rooms")
                .whereEqualTo("name", documentName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        //방없으면 그냥 아무것도 없는 화면
                    } else {
                        //방 있으면 버튼 만들고 이름 넣기
                        String roomName = documentName;

                        LinearLayout View = (LinearLayout) findViewById(R.id.roomRegister_button);
                        Button roombutton = new Button(this);
                        roombutton.setText(documentName);
                        View.addView(roombutton);

                    }
                });
    }
}
