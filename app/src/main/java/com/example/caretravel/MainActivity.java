package com.example.caretravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.caretravel.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebaseAuth();

        EditText emailEditText = findViewById(R.id.email);
        EditText nameEditText = findViewById(R.id.name);
        EditText passwordEditText = findViewById(R.id.password);

        //회원 가입 버튼
        binding.loginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                signUp(email, name, password);
            }
        });

        //로그인 버튼
        binding.loginSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                signIn(email, name, password);
            }
        });
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    // 회원 가입 함수
    private void signUp(String email, String name, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 회원 가입 성공 했을 때
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //이름 프로필 업데이트
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            updateUI(user); // 화면 이동
                        }
                        // 회원 가입 실패 했을 때
                        else {
                            Log.d(TAG, "signInWithEmail:failure");
                            Toast.makeText(getApplicationContext(), "회원 가입 실패했습니다.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    // 로그인 함수
    private void signIn(String email, String name, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // 로그인 성공 했을 때
                        if(task.isSuccessful()) {
                           Log.d(TAG, "signInWithEmail:success");
                           FirebaseUser user = mAuth.getCurrentUser();
                            if(name.equals(user.getDisplayName())){
                                updateUI(user);
                            }
                            // 이름 확인
                            else {
                                Log.d(TAG, "signInWithEmail:failure");
                                Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                       }
                        // 로그인 실패 했을 때
                        else {
                           Log.d(TAG, "signInWithEmail:failure");
                           Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.",
                                   Toast.LENGTH_SHORT).show();
                           updateUI(null);
                       }
                    }
                });
    }

    // 화면 이동
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, activity_register.class);

            startActivity(intent);
        }
    }
}
