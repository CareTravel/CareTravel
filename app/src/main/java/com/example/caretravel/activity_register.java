package com.example.caretravel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caretravel.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class activity_register extends AppCompatActivity {
    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void fetchDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // "rooms" 컬렉션에서 데이터 가져오기
        db.collection("rooms")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", document.getId() + " => " + document.getData());
                            // 여기서 가져온 데이터를 사용하거나 처리할 수 있습니다.
                        }
                    } else {
                        Log.d("TAG", "Error getting documents.", task.getException());
                    }
                });
    }private void getDocumentsWidthOrderData() {
        db.collection("rooms").orderBy("name").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot result = task.getResult();
                List<DocumentSnapshot> documents = result.getDocuments();
                for (DocumentSnapshot doc : documents) {
                    Log.d("TAG", doc.getId() + ": " + doc.getData());
                }
            }
        });
    }


//    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            if (dialog == regDialog && which == DialogInterface.BUTTON_POSITIVE) {
//                showToast("${roomName}으로 방이 생성되었습니다.");
//            }
//        }
//    };

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

//        binding.regButton.setOnClickListener(view -> {
//            //custom dialog를 위한 layout xml 초기화
//            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//            View dialogView = inflater.inflate(R.layout.regdialog_layout, null);
//
//            regDialog = new AlertDialog.Builder(this)
//                    .setView(dialogView)
//                    .setPositiveButton("확인", dialogListener)
//                    .setNegativeButton("취소", null)
//                    .create();
//
//            regDialog.show();
//        });
    }
//    public void createBtn(View view){
//        LinearLayout View = (LinearLayout) findViewById(R.id.roomRegister_button);
//        Button roombutton = new Button(this);
//        View.addView(roombutton);
//    }
}
