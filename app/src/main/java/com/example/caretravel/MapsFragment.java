package com.example.caretravel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment {
    //private FirebaseFirestore db;

    private GoogleMap mMap;
    private Geocoder geocoder;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
//            int n = getArguments().size(); //리스트
//            //여러개 동시에 마커 찍을 수 있게 리스트로 가져와서 i는 장소 개수(행 수)
//            for(int i=0; i<n; i++) {
//                double lat = ;//저장된 변수 이름
//                double lng = ;//저장된 변수 이름

            mMap = googleMap;
            geocoder = new Geocoder(getActivity());

            String str = "숭실대학교 정보과학관";
            //String str=editText.getText().toString();
            List<Address> addressList = null;
            try {
                // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                addressList = geocoder.getFromLocationName(
                        str, // 주소
                        10); // 최대 검색 결과 개수
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(addressList.get(0).toString());
            // 콤마를 기준으로 split
            String []splitStr = addressList.get(0).toString().split(",");
            String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
            System.out.println(address);

            String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
            String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
            System.out.println(latitude);
            System.out.println(longitude);

            // 좌표(위도, 경도) 생성
            LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            // 마커 생성
            MarkerOptions mOptions2 = new MarkerOptions();
            mOptions2.title(str);
            mOptions2.snippet(address);
            mOptions2.position(point);
            // 마커 추가
            mMap.addMarker(mOptions2);
            // 해당 좌표로 화면 줌
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,20));

//                LatLng day = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(day).title("Marker in Sydney"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(day));
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
}