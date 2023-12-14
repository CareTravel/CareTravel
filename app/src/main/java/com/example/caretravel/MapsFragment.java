package com.example.caretravel;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {
    private FirebaseFirestore db;
    private GoogleMap mMap;
    private Geocoder geocoder;

    String roomName;
    String documentName;
    List<Map<String, Object>> locateList;
    int n;



    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            geocoder = new Geocoder(getActivity());

            Log.d("phj", roomName);
            Log.d("phj", documentName);

            db.collection("rooms").document(roomName).collection("경로").document(documentName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("phj", "if task");
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("phj", "document exists");

                                // "List" 필드의 존재 여부 확인
                                if (document.contains("List")) {
                                    Log.d("phj", "contain list");

                                    // price 필드에 저장된 리스트 가져오기
                                    locateList = (List<Map<String, Object>>) document.get("List");
                                    n = locateList.size();


                                    // price 값만 추출하여 더하기
                                    for (Map<String, Object> list : locateList) {
                                        String locate = (String) list.get("locate");
                                        if (locate.equals("")) {
                                        } else {
                                        }
                                    }

                                    // locateList가 null이 아닌 경우에만 실행
                                    if (locateList != null) {
                                        Log.d("phj", String.valueOf(n));

                                        // 여러개 동시에 마커 찍을 수 있게 리스트로 가져와서 i는 장소 개수(행 수)
                                        for (int i = 0; i < n; i++) {
                                            // 리스트에서 장소 포문 돌면서 가져오기
                                            String str = locateList.get(i).toString();
                                            Log.d("phj", str);

                                            List<Address> addressList = null;
                                            try {
                                                // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                                                addressList = geocoder.getFromLocationName(
                                                        str, // 주소
                                                        10); // 최대 검색 결과 개수
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            System.out.println(addressList.get(0).toString());
                                            String address = addressList.get(0).getAddressLine(0); // 주소
                                            System.out.println(address);

                                            if(addressList.size() > 0) { // 주소를 변환한 결과가 있을 경우
                                                Address location = addressList.get(0);
                                                double latitude = location.getLatitude(); // 위도
                                                double longitude = location.getLongitude(); // 경도
                                                System.out.println(latitude);
                                                System.out.println(longitude);

                                                // 좌표(위도, 경도) 생성
                                                LatLng point = new LatLng(latitude, longitude);
                                                // 마커 생성
                                                MarkerOptions mOptions2 = new MarkerOptions();
                                                mOptions2.title(address);
                                                mOptions2.snippet(address);
                                                mOptions2.position(point);
                                                // 마커 추가
                                                mMap.addMarker(mOptions2);
                                                // 해당 좌표로 화면 줌
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            Log.e("phj", "Error getting document", e);
                        }
                    });
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        roomName = getArguments().getString("roomname");
        documentName = getArguments().getString("day");
        return inflater.inflate(R.layout.fragment_maps_path, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();


        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);}


        Button button = getView().findViewById(R.id.fragment_off);
        button.setOnClickListener(buttonListener);

    }
    View.OnClickListener buttonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.beginTransaction().remove(MapsFragment.this).commit();
            manager.popBackStack();
        }};

//    public void loadLocate(){
//    db = FirebaseFirestore.getInstance();
//            db.collection("rooms").document(roomName).collection("경로").document(day)
//                    .get()
//                    .addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DocumentSnapshot document = task.getResult();
//                if (document.exists()) {
//                    // "List" 필드의 존재 여부 확인
//                    if (document.contains("List")) {
//                        // price 필드에 저장된 리스트 가져오기
//                        locateList = (List<Map<String, Object>>) document.get("List");
//
//                        // price 값만 추출하여 더하기
//                        for (Map<String, Object> list : locateList) {
//                            int a;
//                            String price = (String) list.get("locate");
//                            if (price.equals("")) {
//                                a = 0;
//                            } else {
//                                a = Integer.parseInt(price);
//                            }
//                        }
//                    }
//                }
//        }
//                    });
//    }
}