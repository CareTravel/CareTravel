package com.example.caretravel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsFragment extends Fragment {
    private FirebaseFirestore db;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
//            int n = getArguments().size(); //리스트
//            //여러개 동시에 마커 찍을 수 있게 리스트로 가져와서 i는 장소 개수(행 수)
//            for(int i=0; i<n; i++) {
//                double lat = ;//저장된 변수 이름
//                double lng = ;//저장된 변수 이름

                LatLng day = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(day).title("Marker in Sydney"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(day));
            }
//        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps_path, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        //n일차에 맞는 장소 정보를 파바에서 가져오기
//        db = FirebaseFirestore.getInstance();
//        db.collection("rooms").document(roomId).collection(path)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@io.reactivex.rxjava3.annotations.NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot document : task.getResult()) {
//                                String roomName = document.getId();
//                            }
//                        }
//                    }
//                });
//        //여기까지

//        //저장해둔 값 가져오기
//        getParentFragmentManager().setFragmentResultListener("requestKey", this,
//                new FragmentResultListener() {
//                    @Override
//                    public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
//                        // 번들 키 값 입력
//                        String day = bundle.getString("bundleKey");
//                        // 전달 받은 result 이용하여 코딩
//                    }
//                });
//        // 여기까지

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);


        }
    }
}