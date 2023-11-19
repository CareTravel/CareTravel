package com.example.caretravel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityMyPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class myPage extends AppCompatActivity {
    public ActivityMyPageBinding binding;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebaseAuth();
        FirebaseUser user = mAuth.getCurrentUser();

        // 회원 정보 불러오기
        EditText myPage_name = findViewById(R.id.name);
        EditText myPage_email = findViewById(R.id.email);
        String name = user.getDisplayName();
        String email = user.getEmail();
        myPage_name.setText(name);
        myPage_email.setText(email);

        // 로그아웃 버튼
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                finish();
            }
        });

        // 정보수정 버튼
        binding.changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInfo();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(myPage.this, activity_register.class));
            }
        });
    }
    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    // 로그아웃 함수
    private void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        // Check if there is no current user.
        if (firebaseAuth.getCurrentUser() == null)
            Log.d(TAG, "signOut:success");
        else
            Log.d(TAG, "signOut:failure");
    }

    // 정보수정 함수
    private void changeInfo(){
        FirebaseUser user = mAuth.getCurrentUser();
        // 이름 변경
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(binding.mypageName.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "name updated.");
                        }
                    }
                });
        // 이메일 변경
        user.updateEmail(binding.mypageEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });

    }


}