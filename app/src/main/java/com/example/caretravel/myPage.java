package com.example.caretravel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        //회원 정보 불러오기
        EditText myPage_name = (EditText) findViewById(R.id.mypage_name);
        EditText myPage_email = (EditText) findViewById(R.id.mypage_email);
        String userName = user.getDisplayName();
        String userEmail = user.getEmail();
        myPage_name.setText(userName);
        myPage_email.setText(userEmail);


        // 로그아웃 버튼
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                startActivity(new Intent(myPage.this, MainActivity.class));
            }
        });

        // 정보수정 버튼
       binding.changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInfo(user);
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
//    public void onStart(){
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//    }

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
    private void changeInfo(FirebaseUser user){
//        FirebaseUser user = mAuth.getCurrentUser();
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

        Toast.makeText(getApplicationContext(), "이름이 변경되었습니다.",
                Toast.LENGTH_SHORT).show();
    }
}